import java.util.ArrayList;
import java.util.List;

/**
 * Class Board - An instance represents a grid of pieces from two opposing
 * players in a game of Connect Four. The grid is 0-indexed first by rows
 * starting at the top, then by columns 0-indexed starting at the left.
 *
 * @author Daryl Smith
 */
public class Board {
    /**
     * The number of rows on the Connect Four board.
     */
    public static final int NUM_ROWS = 6;
    /**
     * The number of columns on the Connect Four board.
     */
    public static final int NUM_COLS = 7;

    private static final int FOUR = 4; // four in a line

    /**
     * vertical, horizontal, uphill, downhill, directions from any position
     */
    private static final int[][] deltas = {{1, 0}, {0, 1}, {-1, 1}, {1, 1}};

    /**
     * The grid of Player pieces.
     */
    private Player[][] board;

    /**
     * Constructor: an empty Board.
     */
    public Board() {
        board = new Player[NUM_ROWS][NUM_COLS];
    }


    /**
     * Constructor: a duplicate of Board b.
     */
    public Board(Board b) {
        board = new Player[NUM_ROWS][NUM_COLS];
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                this.board[r][c] = b.board[r][c];
            }
        }
    }

    /**
     * Return the element in row r col c.
     * Precondition: r and c give a position on the board
     */
    public Player getPlayer(int r, int c) {
        assert 0 <= r && r < NUM_ROWS && 0 <= c && c < NUM_COLS;
        return board[r][c];
    }

    /**
     * Constructor: a Board constructed by duplicating b and
     * applying nextMove to the new Board.
     */
    public Board(Board b, Move nextMove) {
        this(b);
        makeMove(nextMove);
    }

    /**
     * Return the Player at board position (row, col). Rows are
     * 0-indexed starting at the top and columns are 0-indexed starting
     * at the left. A null return value indicates an empty tile.
     */
    public Player getTile(int row, int col) {
        return board[row][col];
    }

    /**
     * Apply Move move to this Board by placing a piece from move's
     * player into move's column on this Board.
     * Throw an IllegalArgumentException if move's column is full on this Board.
     */
    public void makeMove(Move move) 
    {
        //check if the proposed move column is already filled 
    	if (getTile(0, move.getColumn()) != null)
        {
        	throw new IllegalArgumentException ("The column you are attempting to move to is already full");
        }

        for(int i = NUM_ROWS-1; i >= 0; i--) 
    	{
    		if (getTile(i, move.getColumn()) == null) 
    		{
    			this.board[i][move.getColumn()] = move.getPlayer();
    			break;
    		}
    	}
    }

    /**
     * Return an array of all moves that can possibly be made by Player p on this
     * board. The moves must be in order of increasing column number.
     * Note: The length of the array must be the number of possible moves.
     * Note: If the board has a winner (four things of the same colour in a row), no
     * move is possible because the game is over.
     * Note: If the game is not over, the number of possible moves is the number
     * of columns that are not full. Thus, if all columns are full, return an
     * array of length 0.
     */
    public Move[] getPossibleMoves(Player p) 
    {
    	if (hasConnectFour() != null) 
    	//board has a winner, return a zero length array
    	{
    		return new Move[0];   		
    	}
    	
    	//setup an arraylist to store the possible moves
    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
    	
    	for (int i = 0; i < NUM_COLS; i++) 
    	{
    		if (getTile(0, i) == null) 
    		{
    			possibleMoves.add(new Move(p,i));
    		}
    	}
    	return possibleMoves.toArray(new Move[0]);
    }
    	
    /**
     * Return a representation of this board
     */
    @Override
    public String toString() {
        return toString("");
    }

    /**
     * Return the String representation of this Board with prefix
     * prepended to each line. Typically, prefix contains space characters.
     */
    public String toString(String prefix) {
        StringBuilder str = new StringBuilder("");
        for (Player[] row : board) {
            str.append(prefix + "|");
            for (Player spot : row) {
                if (spot == null) {
                    str.append(" |");
                } else if (spot == Player.RED) {
                    str.append("R|");
                } else {
                    str.append("Y|");
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Return the Player that has four in a row (or null if no player has).
     */
    public Player hasConnectFour() {
        for (Player[] loc : winLocations()) {
            if (loc[0] != null && loc[0] == loc[1] && loc[0] == loc[2] && loc[0] == loc[3]) {
                return loc[0];
            }
        }
        return null;
    }

    /**
     * Return a list of all locations where it is possible to
     * achieve connect four. In this context, a "win location" is an
     * array of the Player pieces on this Board from four connected tiles.
     */
    public List<Player[]> winLocations() {
        List<Player[]> locations = new ArrayList<>();
        for (int[] delta : deltas) {
            for (int r = 0; r < NUM_ROWS; r++) {
                for (int c = 0; c < NUM_COLS; c++) {
                    Player[] loc = possibleWin(r, c, delta);
                    if (loc != null) {
                        locations.add(loc);
                    }
                }
            }
        }
        return locations;
    }

    /**
     * If the four locations in a row beginning in board[r][c] going in the direction
     * given by [delta[0]][delta[1]] are on the board, return an array of them.
     * Otherwise, return null;
     * <p/>
     * Precondition: board[r][c] is on the board and delta is one of the elements of
     * static variable deltas.
     */
    public Player[] possibleWin(int r, int c, int[] delta) {
        Player[] location = new Player[FOUR];
        for (int i = 0; i < FOUR; i++) {
            int newR = r + i * delta[0];
            int newC = c + i * delta[1];
            if (!(0 <= newR && newR < NUM_ROWS && 0 <= newC && newC < NUM_COLS)) {
                return null;
            }
            location[i] = board[newR][newC];
        }
        return location;
    }
}