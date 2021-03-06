package tetris;

import java.util.Arrays;

/**
 * Class is used to create an empty board, ready to be filled with pentominoes. Char 'o' represents an empty cell.
 * @author
 *
 */
public class Board {
	//instance variables
	private char[][] board; 	//Stores the board state.
	private int width;
	private int height;

	/**
	 * Creates a board of given height and width
	 * @param height height of the board
	 * @param width width of the board
	 */
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.board = new char[height][width];
        // Fill all the board with o's (empty fields).
        emptyBoard();
	}

	/**
	 * make the board empty by replacing all cells with value 'o'
	 */
    public void emptyBoard()
    {
        // Fill all the board with o's (empty fields).
        for( int i = 0 ; i < board.length ; i++)
        {
            for( int j = 0 ; j < board[0].length ; j++)
            {
                board[i][j] = 'o';
            }
        }
    }

	/**
	 * Prints the board.
	 */
	public void printBoard()
	{
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[0].length;j++)
				System.out.print(board[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Sets the board to a given matrix of chars.
	 * @param newboard the given matrix of chars
	 */
	public void setBoard(char[][] newboard){
		this.board = newboard;
		this.height = newboard.length;
		this.width = newboard[0].length;
	}

	/**
	 * Gets the board of a given matrix of chars.
	 * @return board of a given matrix of chars.
	 */
	public char[][] getBoard(){
		return this.board;
	}

	/**
	 * Returns the coordinates of the first empty cell .
	 * @return returns the coordinates of the first empty cell or (-1,-1) if there are no empty cells
	 */
	public int[] firstEmptyCell(){
		for(int i = 0; i < board.length;i++){
			for(int j = 0; j < board[i].length;j++){
				if(isCellEmpty(i, j)){
					int[] coord = {i,j};
					return coord;
				}
			}
		}
		int[] coords = {-1,-1};
		return coords;
	}

	/**
	 * Counts the amount of empty cells.
	 * @return the amount of empty cells
	 */
	public int emptyCells(){
		int counter = 0;
		for(int i = 0; i < board.length;i++){
			for(int j = 0; j < board[i].length;j++){
				if(isCellEmpty(i, j)){
					counter++;
				}
			}
		}
		return counter;
	}

	/**
	 * checks if all cells of the board are empty(have value 'o')
	 * @return true of empty, false if not empty
     */
	public boolean isBoardEmpty() {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                if (!isCellEmpty(i, j)) return false;
            return true;

    }

	/**
	 * Set all cells with the given value back to the empty state, 'o'
	 * @param pentomino the value to be removed from the board and replaced with 'o'
	 */
	public void remove(char pentomino){
		int counter = 0;
		for(int i = 0; i < this.board.length; i++){
			if(counter == 5){
				break;
			}
			for(int j = 0; j < this.board[i].length; j++){
				if(board[i][j] == pentomino){
					board[i][j] = 'o';
					counter++;
				}
			}
		}
	}

	/**
	 * Checks if the pentomino can be placed in the cell of the board in its current state. Returns true if it can.
	 * @param pentMatrix matrix which stores the pentomino
	 * @param x coordinate x of the checked cell
	 * @param y coordinate y of the checked cell
	 * @return true if the pentomino can be placed in the cell, false when it cannot
	 */
	public boolean canPlace(char[][] pentMatrix, int x, int y){
		//y -= checkPad(pentMatrix);
		//Checks whether the given matrix goes out of bounds of the board. Return false if it does.
		if(x < 0 || x + pentMatrix.length > board.length || y + pentMatrix[0].length > board[0].length || y < 0){
			return false;
		}
		//Checks whether the given matrix will overlap already placed pentominoes. Returns false when it does.
		for(int i = 0; i < pentMatrix.length;i++){
			for(int j = 0; j < pentMatrix[i].length;j++){
				if(!isCellEmpty(x+i,y+j)){
					if(pentMatrix[i][j] != 'o'){
						return false;
					}
				}
			}
		}
		return true;
	}


	/**
	 * cgeck if a given coordinate can be used to place pieces in
	 * @param coord the coordinates in the form {{x1, x2 ..., xn}{y1, y2, ... yn})
	 * @return true of possible, false if not
     */
	public boolean canPlace(int[][] coord) {
		//Checks whether the given matrix goes out of bounds of the board. Return false if it does. (Jonty thinks and
		// hopes this is true)
			for (int column = 0; column < 5; column++) {
				if (coord[0][column] >= height || coord[0][column] < 0) {
					//System.out.println("returned false because the new coords are shit");
					return false;
				}
				else if( coord[1][column] >= width || coord[1][column] < 0) {
					return false;
				}
			}

			//for (int row = 0; row < 2; row++) {
			for (int column = 0; column < 5; column++) {
				if (!isCellEmpty(coord[0][column], coord[1][column])) {
					// i think we should do some test for proving its not touching another one that moves down
					// if we start with lowest/most right/most left point then we could move it then remove it and there
					// shouldnt be a problem
					// note, i dont know how we would do that
					return false;
				}
			}
		return true;
	}

	/**
	 * Places the pentomino, starting with the first char of the pentomino being placed in the chosen cell.
	 * @param pentMatrix matrix which stores the pentomino
	 * @param x coordinate x where the pentomino needs to be placed
	 * @param y coordinate y where the pentomino needs to be placed
	 */
	public int[][] placePiece(char[][] pentMatrix, int x, int y){
		int[][] coords = new int[2][5];

		//y -= checkPad(pentMatrix);
        int count = 0;
		for(int i = 0; i < pentMatrix.length;i++){
			for(int j = 0; j < pentMatrix[i].length;j++){
				if(pentMatrix[i][j] != 'o'){
					setCell(x+i, y+j, pentMatrix[i][j]);
                    coords[0][count] = x+i;
                    coords[1][count] = y+j;
                    count++;
				}
			}
		}
        return coords;
	}

	/**
	 * Checks if the first chars of the pentomino matrix is an empty cell. Returns the amount of empty cells in the top left corner of the matrix.
	 * @param matrix matrix which stores the pentomino
	 * @return the amount of empty cells in the top left corner
	 */
	private int checkPad(char[][] matrix){
		int pad = 0;
		for(int i = 0; i < matrix.length; i++){
			char value = matrix[0][i];
			if(value == 'o'){
				pad++;
			}
			else{
				break;
			}
		}
		return pad;
	}

	/**
	 * remove a piece from a certain location
	 * @param matrix the matrix of the piece to be removed
	 * @param x the row of the piece
     * @param y the column of piece
     */
	public void removePiece(char[][] matrix, int x , int y)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				if(matrix[i][j] != 'o')
				{
					board[i+x][j+y] = 'o';
				}
			}
		}
	}

	/**
	 * Sets the char of the pentomino in the cell.
	 * @param row coordinate x or the row of the cell
	 * @param column coordinate y or the column of the cell
	 * @param value char of the pentomino
	 */
	public void setCell(int row, int column, char value){
		board[row][column] = value;
	}

	/**
	 * Get the char value from a position in the board
	 * @param row the row you want the value from
	 * @param column the column you want the value from
	 * @return the char value in the board at row x and column y
	 */
	public char getCell(int row, int column)
	{
		if( row >= this.height || column >= this.width ){
			//System.out.printf("out of bounds:%nx: %d height: %d%n y: %d width: %d%n", row, this.height, column,
			//		this.width);
			return ' ';
		}
		return board[row][column];
	}

	/**
	 * Returns how many columns the board has
	 * @return the amount of columns of the board as an integer
	 */
	public int getWidth(){
		return this.width;
	}

	/**
	 * Returns how many rows the board has
	 * @return the amount of rows the board has as an integer
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 * Checks if the cell is empty. Returns true if it is.
	 * @param x coordinate x of the cell
	 * @param y coordinate y of the cell
	 * @return true if the cell is empty
	 */
	private boolean isCellEmpty(int x,int y){
		if(board[x][y] != 'o'){
			return false;
		}
		return true;
	}

    /**
     * returns the height of all the columns of the board. The height is determined by computing where the first
     * not empty cell is at
     * @return the height of all columns combined
     */
	public int aggregateHeight()
	{
		int height=0;

			for(int j=0; j<board[0].length; j++)
			height+=columnHeight(j);

		return height;
	}

    /**
     * the amount of empty holes in a tetris board
     * @return the amount of holes
     */
	public int amountOfHoles()
	{
		int holes=0;

		for(int i=5; i<board.length; i++)
			for(int j=0; j<board[0].length; j++)
				if(board[i][j] == 'o' && board[i-1][j] != 'o') holes++;
		return holes;
	}

    /**
     * returns the height of a column
     * @param column the column you want to have the height of
     * @return the height of that column
     */
	public int columnHeight(int column)
	{
		int height=0;

		for(int i=5; i<=board.length-1; i++)
			if(board[i][column] != 'o') { height=board.length-i; break;}
		return height;
	}

    /**
     * returns the amount of bumpiness, or the absolute difference of the height of every column combined
     * @return the bumpiness
     */
	public int bumpiness()
	{
		int bumpiness=0;

		for(int i=1; i<board[0].length; i++)
			bumpiness+=Math.abs(columnHeight(i-1) - columnHeight(i) );

		return bumpiness;
	}

    /**
     * returns how many full lines/rows there are in the board
     * @return the amount of full rows
     */
	public int checkFullLines()
	{
		int rowsCleared = 0;
		for(int row = 5; row < board.length; row++)
		{
			boolean foundEmptyCell = false;
			for(int column= 0; column < board[0].length; column++)
			{
				if (board[row][column] == 'o' && !foundEmptyCell)
				{
					foundEmptyCell = true;
				}
			}
			if(!foundEmptyCell) rowsCleared++;


		}
		return rowsCleared;
	}

    /**
     * returns a cloned object
     * @return the cloned object
     */
	public Board clone()
	{
		Board boardToReturn= new Board(board[0].length, board.length);

		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[0].length; j++)
				boardToReturn.setCell(i, j, board[i][j]);

		 return boardToReturn;
	}
}