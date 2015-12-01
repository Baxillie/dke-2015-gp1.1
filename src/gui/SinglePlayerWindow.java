package gui;

import tetris.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by baxie on 15-11-15.
 */
public class SinglePlayerWindow extends JPanel {

    private GameLoop gameLoop;
    private boolean gameLoopHasStarted;
    private BoardHandler bh;
    private HighScoreList highScoreList;

    private HumanInput inputController;

    public SinglePlayerWindow(MainMenu mainMenu) {
        //create the variables
        Board board = new Board(10, 20);
        final HumanInput inputController = new HumanInput();
        this.bh = new BoardHandler(board, true);
        this.highScoreList = new HighScoreList();

        //behaviour
        this.addKeyListener(inputController);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        //create the scoreboard
        final ScoreBoard scoreBoard = new ScoreBoard();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(scoreBoard, c);
        //add a timer that updates the scoreboard every 100 ms.
        new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                //System.out.println("Trying to update score");
                scoreBoard.setScore(gameLoop.getScore());
              }
            }).start();

        //create the highscore board
        HighScoreBoard highScoreBoard = new HighScoreBoard(highScoreList);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        this.add(highScoreBoard, c);

        //create the combobox to choose between tetris and pentris
        String[] optionStrings = {"Tetris", "Pentris"};
        final JComboBox optionList = new JComboBox(optionStrings);
        optionList.setSelectedIndex(0);
        optionList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(optionList.getSelectedIndex() == 0)
                {
                    bh.switchToTetris();
                }
                else if(optionList.getSelectedIndex() == 1)
                {
                    bh.switchToPentris();
                }
            }
        });
        optionList.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                optionList.requestFocus();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        this.add(optionList, c);

        //create the gamepanel and add it
        final GamePanel gamePanel = new GamePanel(board);
        this.setSize(Config.MAIN_MENU_WIDTH, Config.MAIN_MENU_HEIGHT);
        gamePanel.setSize(Config.MAIN_MENU_WIDTH, Config.MAIN_MENU_HEIGHT);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.weightx = 0.5;
        c.gridy = 0;
        c.weighty = 1;
        c.gridheight = 3;
        c.anchor = GridBagConstraints.CENTER;
        this.add(gamePanel, c);

        //set the Thread
        gameLoop = new GameLoop(bh, inputController, gamePanel/*, scoreBoard*/);
        gameLoop.start();
        gameLoopHasStarted = false;

        //add the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(30);
        buttonPanel.setLayout(new GridLayout(3,1,10,10));
        c = new GridBagConstraints();
        c.gridx = 2;
        c.weightx = 0.2;
        c.insets = new Insets(250,0,0,0);
        this.add(buttonPanel, c);

        //backbutton
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.anchor = GridBagConstraints.SOUTH;
        c.insets = new Insets(0, 0, 20, 0);
        this.add(new BackButton(mainMenu), c);

        //startbutton
        final JButton startButton = new JButton("Start");
        startButton.requestFocus(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(!gameLoopHasStarted)
               {
                   try{
                   SwingUtilities.invokeLater(new Runnable() {
                       @Override
                       public void run(){
                           gameLoopHasStarted = true;
                           gameLoop.startNewGame();
                           optionList.setEnabled(false);
                           requestFocusInWindow();
                           startButton.setEnabled(false);
                        }
                        });
                   }
                   catch(Exception expenction)
                   {
                       expenction.printStackTrace();
                   }
               }
            }
        });
        buttonPanel.add(startButton);
        //pause button
        final JButton pauseButton = new JButton("Pause  ");
        buttonPanel.add(pauseButton);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gameLoop.isPaused()) {
                    gameLoop.setPaused(true);
                    pauseButton.setText("Unpause");
                }
                else if(gameLoop.isPaused())
                {
                    gameLoop.setPaused(false);
                    pauseButton.setText("Pause  ");
                }
            }
        });
        //reset button
        JButton resetButton = new JButton("Reset");
        buttonPanel.add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bh.resetBoard();
                optionList.setEnabled(true);
                if(gameLoop.isRunning())
                {
                    gameLoop.apruptGameEnd();
                }
                gameLoopHasStarted = false;
                gamePanel.repaint();
                startButton.setEnabled(true);
                gameLoop.setPaused(false);
                pauseButton.setText("Pause");
                scoreBoard.setScore(0);
            }
        });


        //focuslistener for inputController
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public Dimension getPreferredSize()
    {
        return Config.SINGLE_PLAYER_SIZE;
    }
}
