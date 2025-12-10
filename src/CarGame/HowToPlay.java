package CarGame;

import javax.swing.*;
import java.awt.*;

public class HowToPlay extends JPanel {
    private Image HowToPlay;
    private JButton backButton;

    public HowToPlay(CardLayout cardLayout, JPanel contentPanel) {
        HowToPlay = new ImageIcon("src/Images/How To Play.png").getImage();
        setLayout(null);



    backButton =createButton("Back");
    backButton.setLocation(300, 500);
    backButton.addActionListener(e-> cardLayout.show(contentPanel,"HomePage"));
    add(backButton);
    }
    @Override
    protected  void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(HowToPlay!=null){
            g.drawImage(HowToPlay,0,0,getWidth(),getHeight(),this);
        }
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


}
