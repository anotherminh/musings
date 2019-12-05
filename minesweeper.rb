require 'pry'
class Minesweeper
  attr_reader :height, :width, :bomb_count, :hidden_board, :board

  def initialize(height = 10, width = 10, bomb_count = 10)
    @height = height
    @width = width
    @bomb_count = bomb_count
    @board = Array.new(height) { Array.new(width) { ' ' } }
    # hidden board is how we keep track of bomb cells and counter cells
    @hidden_board = Array.new(height) { Array.new(width) { 0 } }
    randomly_place_bombs!
  end

  def play
    show_board

    if game_over?
      game_won? ? puts('YAY you won!') : puts('You lost, try again.')
    else
      coords = get_input_from_player
      reveal!(coords)
      play
    end
  end

  private

  NEIGHBOR_CELL_OFFSETS = [[-1,0],[1,0],[0,-1],[0,1]]
  REVEALED_CELL_FORMAT = '%-3i'

  def show_board
    system('clear')
    @board.each do |row|
      print "#{row.map(&:to_s)}\n"
    end
  end

  def reveal!(coords)
    row = coords[0]
    col = coords[1]
    @board[row][col] = @hidden_board[row][col]
    reveal_neighbors!(row, col) if @board[row][col] == 0
  end

  def reveal_neighbors!(row, col)
    neighbors = neighbor_coords(row, col)
    seen_neighbors = Set.new()
    until neighbors.empty? do
      coords = neighbors.pop
      seen_neighbors << coords
      cell = @hidden_board[coords[0]][coords[1]]

      # reveal the neighbor if it's not a bomb
      if cell != 'B'
        @board[coords[0]][coords[1]] = @hidden_board[coords[0]][coords[1]]
      end

      # if it's a zero, then we reveal even more neighbors
      if cell == 0
        # select for eligible neighbors to add to our list
        unrevealed_neighbors = neighbor_coords(coords[0], coords[1]).select do |next_coords|
          n_row = next_coords[0]
          n_col = next_coords[1]
          !seen_neighbors.include?([n_row, n_col]) && # we havent seen it before
          @board[n_row][n_col] == " " && # unrevealed
            @hidden_board[n_row][n_col] != 'B' #has a zero
        end
        neighbors.concat(unrevealed_neighbors)
      end
    end
  end

  def neighbor_coords(row, col)
    NEIGHBOR_CELL_OFFSETS.map do |offset|
      n_row = row + offset[0]
      n_col = col + offset[1]
      valid_coords?(n_row, n_col) ? [n_row, n_col] : nil
    end.reject(&:nil?).select do |coords|
      valid_coords?(coords[0], coords[1])
    end
  end

  # if ANY bomb has been revealed, we lost
  # If all the non-bombs have been revealed, then we won
  def game_over?
    game_won? || game_lost?
  end

  def game_won?
    non_bomb_revealed_count(@board) == non_bomb_revealed_count(@hidden_board)
  end

  # dumb way to end the game but I implemented this thinking a different/wrong losing condition
  def game_lost?
    bomb_revealed_count(@board) > 0
  end

  def non_bomb_revealed_count(board)
    board.map { |row| row.select { |cell| cell.is_a?(Integer) } }.flatten.size
  end

  def bomb_revealed_count(board)
    board.map { |row| row.select { |cell| cell == 'B' } }.flatten.size
  end

  def get_input_from_player
    puts 'Please enter two valid numbers for the cell you want to select, separated by space, eg. "0 3"'
    input = gets.chomp.split(' ').map { |i| Integer(i) }
    if input.size == 2 && valid_coords?(input[0], input[1])
      input
    else
      get_input_from_player
    end
  rescue ArgumentError => e
    puts e
    puts "Invalid input given: #{input}"
    get_input_from_player
  end

  def randomly_place_bombs!
    bomb_coords = []
    until bomb_coords.size == bomb_count do
      bomb_coords.push([rand(0...height), rand(0...width)])
      bomb_coords.uniq
    end

    bomb_coords.each do |coord|
      row = coord[0]
      col = coord[1]
      @hidden_board[row][col] = 'B'
      place_bomb_counter!(row, col)
    end
  end

  def place_bomb_counter!(row, col)
    neighbor_coords(row, col).map do |coords|
      n_row = coords[0]
      n_col = coords[1]
      unless cell_has_bomb(@hidden_board, n_row, n_col)
        @hidden_board[n_row][n_col] += 1
      end
    end
  end

  def cell_has_bomb(board, row, col)
    board[row][col] == 'B'
  end

  def valid_coords?(row, col)
    (0...height).include?(row) && (0...width).include?(col)
  end
end
