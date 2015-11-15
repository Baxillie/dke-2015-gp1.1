package gui;

import tetris.Board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Aleksandra and Nik on 2015-11-10.
 */
public class MainMenu extends JFrame {
    //Every panel the we want to potentially display
    private SinglePlayerWindow  singlePlayerWindow;
    private MultiPlayerWindow   multiPlayerWindow;
    private HighScoreWindow     highScoreWindow;
    private BotWindow           botWindow;
    private OptionsWindow       optionsWindow;

    private JPanel mainPanel; //holds the image header and all the buttons

    //the buttons to go to each panel
    private JButton singlePlayerButton;
    private JButton multiPlayerButton;
    private JButton optionsMenuButton;
    private JButton highScoresButton;
    private JButton botButton;
    private JButton quitButton;

    //holding this panel is currently being displayed
    private JPanel currentlyDisplayed;

    public MainMenu() {
        //set up the layout
        this.setLayout(new GridBagLayout());
        //set up the panels and their buttons and the image header
        setUpMainPanel();
        setUpOtherPanels();

        //and set the behavior of the main menu and start displayed the main menu
        //set behavior of the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pentris");
        this.setResizable(false);
        this.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        startDisplaying(mainPanel);
        this.setVisible(true);
    }

    private void setUpMainPanel()
    {
        mainPanel = new JPanel(); //to hold image and buttons
        mainPanel.setLayout(new GridBagLayout());
        setUpButtons();
        setUpActionListeners();
        setUpImage();
    }

    private void setUpOtherPanels()
    {
        singlePlayerWindow = new SinglePlayerWindow();
    }

    private void setUpButtons()
    {
        //set up the the buttons
        singlePlayerButton = new JButton("Single Player");
        multiPlayerButton = new JButton("Multiplayer");
        optionsMenuButton = new JButton("Options");
        highScoresButton  = new JButton("View Highscore");
        botButton         = new JButton("Watch bot");
        quitButton        = new JButton("Quit");

        //add the buttons to a panel and add the panel to the frame.
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new GridLayout(6, 0, 0, 20));
        buttonHolder.add(singlePlayerButton);
        buttonHolder.add(multiPlayerButton);
        buttonHolder.add(highScoresButton);
        buttonHolder.add(botButton);
        buttonHolder.add(optionsMenuButton);
        buttonHolder.add(quitButton);

        //set up constraints for the buttonpanel and add it to the panel
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        mainPanel.add(buttonHolder, c);
    }

    private void setUpImage()
    {
        try{
            BufferedImage image = ImageIO.read(new File(Config.MAIN_MENU_HEADER_IMAGE));
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            mainPanel.add(imageLabel, c);
        }
        catch(IOException e)
        {
            System.out.println("Could not find image for main menu in memory");
        }
    }

    private void setUpActionListeners()
    {
        //actionlistener for the singlePlayerWindow
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDisplaying(singlePlayerWindow);
            }
        });

        //actionlistener for the mutliplayer window
        multiPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDisplaying(multiPlayerWindow);
            }
        });

        //actionlistener for the high score window
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDisplaying(highScoreWindow);
            }
        });

        //actionlistener for the bot window
        botButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDisplaying(botWindow);
            }
        });

        //actionlistener for the option menu
        optionsMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDisplaying(optionsWindow);
            }
        });

        //actionlistener for the quit button
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    private void startDisplaying(JPanel panelToDisplay)
    {
        if(currentlyDisplayed != null)
        {
            System.out.println("Want to display " + panelToDisplay.getClass().getName());
            //stop displaying the current
            this.remove(currentlyDisplayed);
            currentlyDisplayed = panelToDisplay;
            this.add(panelToDisplay);
            this.validate();
            this.pack();
        }
        else{
            currentlyDisplayed = panelToDisplay;
            this.add(panelToDisplay);
        }
    }

}