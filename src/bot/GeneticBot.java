/**
 * Created by baxie on 12/7/15.
 */
package bot;

import gui.GamePanel;
import tetris.Board;
import tetris.BoardHandler;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class GeneticBot extends Thread{
    //final variables
    final static char ACTION_MOVE_DOWN = 'd';
    final static char ACTION_MOVE_LEFT = 'l';
    final static char ACTION_MOVE_RIGHT = 'r';
    final static char ACTION_DROP_DOWN = 's';
    final static char ACTION_ROTATE_CLOCKWISE = 'x';

    private Random rng;
    private Board board;
    private BoardHandler boardHandler;

    //variables needed for genetic algorithm
    private boolean gameOver;
    private int linesCleared;
    private double[] chromesome;
    private int MAXIMUM_SPAWNS_ALLOWED = 500;
    private int spawns;

    //variables for board handling
    char[] actionCommands;
    private int count;

    /**
     * Constructor for the genetic bot
     * @param board the Board class our bot is playing on
     * @param boardHandler the BoardHandler which has the same board as the one from the Board class parameter
     * @param chromesome the weights used by this bot
     */
    public GeneticBot(Board board, BoardHandler boardHandler, double[] chromesome)
    {
        this.board = board;
        this.boardHandler = boardHandler;
        this.rng = new Random(System.currentTimeMillis());
        this.chromesome = chromesome;
        count = 0;
        spawns = 0;
        linesCleared = 0;
    }

    /**
     * returns if game is over
     * @return true for game over, false if not
     */
    public boolean gameOver()
    {
        return gameOver;
    }

    /**
     * returns the amount of lines the bot was able to clear
     * @return the amount able to be cleared by the bot
     */
    public int getLinesCleared()
    {
        return linesCleared;
    }

    /**
     * The method gives the mod a command in order to start playing
     */
    public void run()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                playGame();
            }
        });
    }

    /**
     * Loop which makes the bot play the game
     */
    private void playGame()
    {
        count++;
        //System.out.println(count);
        if (boardHandler.isPieceDoneFalling()) {
            //System.out.println("Spawning new piece");
            if (boardHandler.isGameOver() || spawns >= MAXIMUM_SPAWNS_ALLOWED) {
                gameOver = true;
                return;
            }
            else {
                int rowsCleared = boardHandler.checkFullLines();
                if(rowsCleared != 0) {
                    linesCleared+= rowsCleared;
                }
                spawns++;
                boardHandler.spawnPiece();
                makeMovementCommands();
            }
        }
        //System.out.println("Looking for user input");
        char input = getMovementCommand();
        //System.out.println("Input: " + input);
        //System.out.println("User input: " + input);
        if (input != ' ') {
            //System.out.println("user input was not empty, repainting");
            boardHandler.giveInput(input);
        }
        if (count > 4) //1000 ms have passed
        {
            count = 0;
            //System.out.println("10 loops have happened, moving the piece down");
            boardHandler.giveInput('d');
        }
        ////System.out.println("restarting the game loop!");
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    playGame();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the correct movement for a bot turn by changing the actionCommands variable
     */
    private void makeMovementCommands()
    {
        char[][] botBoard = board.getBoard();
        Board cloneBoard= board.clone();

        char[][] pieceFalling = boardHandler.getFallingPentMatrix();
        char letterPieceFalling=boardHandler.getFallingPentName();
        int currentRow = boardHandler.getCurrentRow();
        int currentColumn = boardHandler.getCurrentColumn();
        int[] recommendedPositionAndRotation = getRecommendedPositionAndRotation(botBoard, pieceFalling);
        int newRow = recommendedPositionAndRotation[0];
        int newColumn = recommendedPositionAndRotation[1];
        int rotation = recommendedPositionAndRotation[2];
        ArrayList<Character> newActionCommands = new ArrayList<>();
        newActionCommands.add('d');
        newActionCommands.add('d');
        for(int i = rotation; i > 0; i-- )
        {
            newActionCommands.add(ACTION_ROTATE_CLOCKWISE);
        }
        for(int i = 0; currentColumn != newColumn; )
        {
            if(currentColumn < newColumn)
            {
                newActionCommands.add(ACTION_MOVE_RIGHT);
                currentColumn++;
            }
            else{
                newActionCommands.add(ACTION_MOVE_LEFT);
                currentColumn--;
            }
        }
        newActionCommands.add(ACTION_DROP_DOWN);
        actionCommands = new char[newActionCommands.size()];
        for( int i = 0; i < newActionCommands.size(); i++)
        {
            actionCommands[i] = newActionCommands.get(i);
        }
    }

    /**
     * Makes sure the piece can move to the place where it will placed
     * @param newRow the row in which the piece will be placed
     * @param newColumn the column in which the piece will be placed
     * @param matrix the matrix of the current falling piece
     * @return returns whether the piece can be placed
     */
    private boolean testPath(int newRow, int newColumn, char[][] matrix)
    {
        for(int row=5; row<newRow; row++)
        {
            for(int i = 0; i < matrix.length; i++)
            {
                for(int j = 0; j < matrix[i].length; j++)
                {
                    if(matrix[i][j] != 'o' && board.getCell((row + i), (newColumn + j)) != 'o') return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the first value of the actionCommands array and deletes the value from the array
     * @return the first value of the array
     */
    private char getMovementCommand()
    {
        char action = actionCommands[0];
        char[] newActionCommands = new char[actionCommands.length - 1];
        // System.out.println("old array: " + Arrays.toString(actionCommands));
        System.arraycopy(actionCommands, 1, newActionCommands, 0, newActionCommands.length);
//        System.out.println("new array: " + Arrays.toString(newActionCommands));
        actionCommands = newActionCommands;
        return action;
    }

    /**
     * Returns the position and rotation the bot should place the piece in
     * @param board the board on which the piece should be placed
     * @param piece the piece that the bot must place
     * @return returns an array where the first 2 values are the coordinates and the third value is the rotation
     */
    private int[] getRecommendedPositionAndRotation(char[][] board, char[][] piece)
    {
        Board testBoard = new Board(0,0);
        testBoard.setBoard(board);
        ArrayList<char[][]> possiblePieces = new ArrayList<>();
        possiblePieces.add(piece);
        possiblePieces.add(BoardHandler.rotateMatrix(piece, true));
        possiblePieces.add(BoardHandler.rotateMatrix(possiblePieces.get(1), true));
        possiblePieces.add(BoardHandler.rotateMatrix(possiblePieces.get(2), true));

        ArrayList <Integer> xCoords= new ArrayList<>();
        ArrayList <Integer> yCoords= new ArrayList<>();
        int highestRow = board.length - 1;
        for(int i=board.length-1; i>=5; i--)
        {
            for (int j = board[0].length-1; j >= 0; j--)
            {
                if(board[i][j] != 'o')
                {
                    highestRow = i;
                    //  System.out.println("Highest row: " + highestRow);
                    break;
                }
            }
        }
        //fill the array with all the coords to check
        for(int i = highestRow - 2; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                xCoords.add(i);
                //System.out.println("Row: " + i + " Column: " + j);
                yCoords.add(j);
            }
        }

        //at every coord, try to place all rotations of the matrix and calculate the "snug" level
        int bestX = 0;
        int bestY = 0;
        int bestR = 0;
        double bestScore = -100000000;
        Board bestBoard = new Board(15,20);
        for(int i=0; i<xCoords.size(); i++)
        {
            int x = xCoords.get(i);
            int y = yCoords.get(i);
            //System.out.println("Row: " + x + " Column: " + y );
            for(int rot = 0; rot < possiblePieces.size(); rot++)
            {
                if(testBoard.canPlace(possiblePieces.get(rot), x, y) && testPath(x,y, possiblePieces.get(rot)))
                {
                    testBoard.placePiece(possiblePieces.get(rot), x, y);
                    //System.out.println(Arrays.deepToString(possiblePieces.get(rot)));
                    double tempScore;
                    if(boardHandler.isTetris()){
                        tempScore = chromesome[0] * testBoard.aggregateHeight() + chromesome[1] * testBoard.checkFullLines()
                                + chromesome[2] * testBoard.amountOfHoles() + chromesome[3]* testBoard.bumpiness();
                    }
                    else{
                        tempScore = chromesome[0] * testBoard.aggregateHeight() + chromesome[1] * testBoard.checkFullLines()
                                + chromesome[2] * testBoard.amountOfHoles() + chromesome[3]* testBoard.bumpiness();
                    }

//                    testBoard.printBoard();
//                    System.out.printf(" Aggregate height: %d %n Amount of holes: %d %n Bumpiness: %d %n " +
//                            "Full lines: %d %n ", testBoard.aggregateHeight(), testBoard.amountOfHoles(),
//                            testBoard.bumpiness(), testBoard.checkFullLines());
//                    System.out.println("Score: " + tempScore);
                    if(tempScore > bestScore && isGameOver(testBoard)){
                        isGameOver(testBoard);
                        bestX = x;
                        bestY = y;
                        bestR = rot;
                        bestScore=tempScore;
                        bestBoard= testBoard.clone();
                        //System.out.println("Max score: " + bestScore);

                    }
                    testBoard.removePiece(possiblePieces.get(rot), x , y);
                }
            }
        }

        //System.out.println("Printing best board:");
        //bestBoard.printBoard();
        return new int[] {bestX, bestY, bestR};
    }

    /**
     * Tests whether the game is over or not
     * @param board the board on which the bot is playing the game
     * @return whether the game is over or not
     */
    public boolean isGameOver(Board board)
    {
        for(int i=0; i<5; i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.getCell(i, j) != 'o') return true;
            }
        }
        return false;
    }

    /**
     * returns the how many lines the bot has currently cleared
     * @return the amount of lines cleared
     */
    public int getCurrentLinesCleared()
    {
        return linesCleared;
    }
}