package tetris;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.Random;

/**
 * Created by Nik on 11/12/15.
 */
public class BoardHandler {

    private Board board;
    private Pentominoes pentominoes;
    private InputController input;
    private Random rng;

    private char[][] fallingPentMatrix; //the matrix of the currently falling
    private int[][] coordsFallingPent;  //coordinates of the pentomino current falling in the form of
                                        // { {x1,..x5},{y1,..,y5} }


    public BoardHandler(Board board, InputController input)
    {
        this.board = board;
        this.input = input;
        pentominoes = new Pentominoes();
        rng = new Random();
    }

    /**
     * Spawns a random pentomino in the top row(s) of the board.
     */
    public void spawnPiece()
    {
        fallingPentMatrix = getRandomPentomino();
        board.placePiece(fallingPentMatrix, 0, (board.getWidth()/2) -1);
        coordsFallingPent = getInitialCoordinates();
    }

    /**
     * returns a random matrix of a pentomino
     * @return
     */
    private char[][] getRandomPentomino()
    {
        char[] everyPentomiChar = pentominoes.getCharForTheList();
        int randomNumber = rng.nextInt(everyPentomiChar.length);
        char[][] toReturn = pentominoes.getMatrix(everyPentomiChar[randomNumber], 0);
        if(rng.nextBoolean())
        {
           // toReturn = flipMatrix(toReturn);
        }
        return toReturn;
    }

    //TODO FINISH THE FLIPPING :D
    /**
     *
     */
//    private char[][] flipMatrix(char[][] matrixToFlip)
//    {
//        char[][] flippedMatrix =
//
//        return matrixToFlip;
//    }

    private int[][] getInitialCoordinates()
    {
        int [][] toReturn = new int[2][5];
        int count = 0;
        for(int column = 0; column < board.getWidth(); column++)
        {
            for(int row = 0; row < 5; row++ )
            {
                if(board.getCell(row, column) != 'o')
                {
                    toReturn[0][count] = row;
                    toReturn[1][count] = column;
                    count++;
                }
            }
        }
        if(count != 5)
        {
            System.out.print("Something has gone horrible wrong with the initial coordinates");
        }
        return toReturn;
    }

    //direction can be 'l' for left, 'r' for roght and 'd' for down
    public void movePentominoTo(char direction)
    {
        int rowTranslation = 0;
        int columnTranslation = 0;
        if(direction == 'l')
        {
            columnTranslation = -1;
        }
        else if(direction == 'r')
        {
            columnTranslation = 1;
        }
        else if(direction == 'd')
        {
            rowTranslation = 1;
        }

        int[][] newCoords = new int[2][5];
        for(int row = 0; row < 2; row++)
        {
            for(int column = 0; column < 5; column++)
            {
                if(row == 0)
                    newCoords[row][column] = coordsFallingPent[row][column] + rowTranslation;
                else
                    newCoords[row][column] = coordsFallingPent[row][column] + columnTranslation;
            }
        }



    }
}
