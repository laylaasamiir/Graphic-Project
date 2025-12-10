package CarGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private JButton singlePlayerButton  ;
    private JButton multiPlayerButton ;
    private JButton howToPlayButton ;
    private JButton exitButton ;
    private JButton signInButton;
    private String PlayerName="";
    private Image BackGround;
    public HomePage() {
        BackGround = new ImageIcon("src/Images/BackGround.png").getImage();
        setLayout(null);

        singlePlayerButton=createButton("singlePlayerButton");
        singlePlayerButton.setLocation(300,150);

        multiPlayerButton=createButton("multiPlayer");
        multiPlayerButton.setLocation(300,250);

        howToPlayButton=createButton("howToPlay");
        howToPlayButton.setLocation(300,300);

        exitButton=creatExitButton("Exit");
        exitButton.setLocation(10,250);

        signInButton=createButton("signIn");
        signInButton.setLocation(10,10);

        add(singlePlayerButton);
        add(multiPlayerButton);
        add(howToPlayButton);
        add(signInButton);
        add(exitButton);


    }
    public void setButtonAction(ActionListener actionListener) {
        singlePlayerButton.addActionListener(actionListener);
        multiPlayerButton.addActionListener(actionListener);
        howToPlayButton.addActionListener(actionListener);
        signInButton.addActionListener(actionListener);
        exitButton.addActionListener(actionListener);

    }
    private  JButton createButton(String text){
        JButton button = new JButton(text);
        button.setSize(200, 60);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;

    }
    private  JButton creatExitButton(String text){
        JButton button = new JButton(text);
        button.setSize(100, 40);
        button.setOpaque(true);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }
    protected  void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (BackGround != null) {
            g.drawImage(BackGround, 0, 0, this.getWidth(), this.getHeight(), this);

        }
    if(!PlayerName.isEmpty()){
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth("Welcome, " + PlayerName + "!")) / 2;
        int y = metrics.getAscent() + 10;
        g.drawString("Welcome, " + PlayerName + "!", x, y);
      }
    }

    public void showSignInDialog(){
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Sign In", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            PlayerName = name.trim();
            JOptionPane.showMessageDialog(this, "Welcome, " + PlayerName + "!", "Sign In Successful", JOptionPane.INFORMATION_MESSAGE);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "You must enter a name to sign in.", "Sign In Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    }









