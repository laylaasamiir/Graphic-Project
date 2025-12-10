package CarGame;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Levels  extends JPanel {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TheGame game;
    private JButton easy;
    private JButton medium;
    private JButton hard;
    private JButton back;
    private Image background;
    private GLCanvas canvas;
    private Timer timer;


    public Levels(TheGame game ,CardLayout cardLayout,JPanel contentPanel ) {
        this.game = game;
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        background = new ImageIcon("src/Images/The BackGround Image.png").getImage();
        setLayout(null);

        easy=createButton("easy");
        easy.setBounds(275,300,150,45);
        easy.addActionListener(this::startGame);

        medium=createButton("medium");
        medium.setBounds(275,350,150,45);
        medium.addActionListener(this::startGame);

        hard=createButton("hard");
        hard.setBounds(275,400,150,45);
        hard.addActionListener(this::startGame);

        back =createButton("Back");
        back.setBounds(30,10,65,30);
        back.addActionListener(e-> cardLayout.show(contentPanel,"HomePage"));

        add(back);
        add(easy);
        add(medium);
        add(hard);


    }
    private void startGame(ActionEvent e){
        String difficulty = e.getActionCommand();
        String gameMode = game.gameMode;
        if(gameMode.equals("Single Player")){
            startSinglePlayer(difficulty);
        }
        else if(gameMode.equals("Multi Player")){
            startMultiPlayer(difficulty);
        }
    }
    private void startSinglePlayer(String difficulty){
        if(canvas != null){
            contentPanel.removeAll();
        }
        canvas = new GLCanvas();
        canvas.setFocusable(true);
        contentPanel.add(canvas ,"GameScreen");
        cardLayout.show(contentPanel,"GameScreen");
        canvas.requestFocusInWindow();
        if(timer ==null){
            timer = new Timer(16,event -> canvas.display());
        }
        timer.start();
    }
    private void startMultiPlayer(String difficulty){
        if(canvas != null){
            contentPanel.removeAll();
        }
        canvas = new GLCanvas();
        canvas.setFocusable(true);
        contentPanel.add(canvas ,"GameScreen");
        cardLayout.show(contentPanel,"GameScreen");
        canvas.requestFocusInWindow();
        if(timer ==null){
            timer = new Timer(16,event -> canvas.display());

        }
        timer.start();
    }



    private  JButton createButton(String text){
        JButton button = new JButton(text);
        button.setSize(200, 60);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
