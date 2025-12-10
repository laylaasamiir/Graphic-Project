package CarGame;

import javax.swing.*;
import java.awt.*;

public class TheGame extends JFrame {
    private CardLayout  cardLayout;
    private JPanel contentPanel;
    private JLayeredPane layeredPane;
    public String gameMode;
//    main
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> new TheGame());
    }
    public TheGame() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        HomePage homePage = new HomePage();
        HowToPlay howToPlay = new HowToPlay(cardLayout, contentPanel);
//        باقي الpages


//
        homePage.setButtonAction(e ->{
            String command = e.getActionCommand();
            if("Exit".equals(command)){
               dispose();
            }
            else if("signIn".equals(command)){
                homePage.showSignInDialog();
            }
            else if("howToPlay".equals(command)){
                cardLayout.show(contentPanel,"howToPlay");
            } 

            }


        });



        contentPanel.add(homePage, "HomePage"   );
        contentPanel.add(howToPlay, "howToPlay");


//        باقي الpages
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(700,500));
        layeredPane.add(contentPanel,JLayeredPane.DEFAULT_LAYER);
        contentPanel.setBounds(0,0,700,500);

        setTitle("Car Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700,500);
        setLocationRelativeTo(null);
        getContentPane().add(layeredPane);
        setVisible(true);


    }

}
