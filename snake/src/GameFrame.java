
import javax.swing.JFrame;

public class GameFrame extends JFrame {

    
    
    GameFrame(){

        this.add(new GamePanel()); //creates a game pannel//
        this.setTitle("Tim's Snake Game with Sound"); //sets a title for the pannel//
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the operation when tab is closed//
        this.setResizable(false); //cannot be resized//
        this.pack(); //packs everything together//
        this.setVisible(true); //makes the tab visible//
        this.setLocationRelativeTo(null); //centers the tab//


        
    }
}
