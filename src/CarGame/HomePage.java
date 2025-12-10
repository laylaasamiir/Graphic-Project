package CarGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomePage extends JPanel {
    private JButton singlePlayerButton;
    private JButton multiPlayerButton;
    private JButton howToPlayButton;
    private JButton exitButton;
    private JButton signInButton;
    private String PlayerName = "";
    private Image BackGround;
    public HomePage() {
        BackGround = new ImageIcon("src/Images/The BackGround Image.png").getImage();
        setLayout(null);

        singlePlayerButton=createButton("Single Player");
        singlePlayerButton.setBounds(275,300,150,45);

        multiPlayerButton=createButton("Multi Player");
        multiPlayerButton.setBounds(275,350,150,45);

        howToPlayButton=createButton("How To Play");
        howToPlayButton.setBounds(275,400,150,45);

        exitButton=creatExitButton("Exit");
        exitButton.setBounds(30,10,65,30);

         signInButton=createButton("Sign In");
        signInButton.setBounds(580,10,80,30);

        add(singlePlayerButton);
        add(multiPlayerButton);
        add(howToPlayButton);
        add(exitButton);
        add(signInButton);

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
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;

    }
    private  JButton creatExitButton(String text){
        JButton button = new JButton(text);
        button.setSize(100, 40);
        button.setOpaque(true);
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
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
            PlayerName = name.trim(); // Update the player's name
            JOptionPane.showMessageDialog(this, "Welcome, " + PlayerName + "!", "Sign In Successful", JOptionPane.INFORMATION_MESSAGE);
            repaint(); // Redraw the panel to show the updated name
        } else {
            JOptionPane.showMessageDialog(this, "You must enter a name to sign in.", "Sign In Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    }









