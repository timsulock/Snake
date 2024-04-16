import java.awt.*;
import java.awt.event.*; 
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    
    static final int SCREEN_WIDTH = 600; //Sets the width of the tab//
    static final int SCREEN_HEIGHT = 600; //sets the height of the tab//
    static final int UNIT_SIZE = 25; //size of the apple//
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE; //sets the amounts of game units//
    static final int DELAY = 100; //sets the speed of the game, higher the #, slower the game//
    final int x[] = new int[GAME_UNITS]; //holds all of the x-coordinates of the snake//
    final int y[] = new int[GAME_UNITS]; //holds all of the y-coordinates of the snake// //set the size to game units because snake will not exceed size of the game//
    int bodyParts = 6; //size the snake will start out at//
    int applesEaten; //amount of apples eaten, originally 0//
    int appleX; //x-coordinate of the apple//
    int appleY; //y-coordinate of the apple//
    char direction = 'R'; //direction the snake starts off going//
    boolean running = true; //game is not running//
    Timer timer; //sets a timer variable//
    Random random; //random var//
    File Clap = new File("/Users/timsulock/Desktop/Projects/snake/src/mixkit-retro-game-notification-212.wav"); //sets the file name to clap and uses the path to make the sound//
    File gameOverSound = new File("/Users/timsulock/Desktop/Projects/snake/src/mixkit-sad-game-over-trombone-471.wav"); //sets the file name to gameOverSound and uses the path to make the sound//
    

    GamePanel(){
        random = new Random(); //makes a random variable//
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); //sets the size of the panel to 600 by 600// 
        this.setBackground(Color.blue); //sets the background color of the panel//
        this.setFocusable(true); //allows the panel to listen to key commands//
        this.addKeyListener(new MyKeyAdapter()); //sets a key listener, uses MyKeyAdapter class//
        startGame(); //calls the startGame method//
    }

    public void startGame(){ //start game method//
        newApple(); //calls on the newApple method//
        running = true; //game is running//
        timer = new Timer(DELAY,this); //sets a timer with the delay to dictate how fast the game runs//
        timer.start(); //starts the timer using start function//
    }

    public void paintComponent(Graphics g) { //sets method called paintComponent and makes the parameters Graphics

        super.paintComponent(g); //clears the components background and then sets up graphics context for painting//
        draw(g); //actually baints the component//
    }

    public void draw(Graphics g) {
        if(running) {
        g.setColor(Color.red); //when code is running apple color is red//
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //fills random coordinates with apple in shape of an oval, UNIT_SIZE sets size of pples//

        for(int i = 0; i< bodyParts;i++) { 
            if(i==0) { //dealing with the head of the snake//
                g.setColor(Color.green); // sets the snake head to green//
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //fills a rectangle the color green for the snakes head//
            }
            else {
                g.setColor(new Color(45, 180, 0)); //sets the color of the body a different green//
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //fills the rectangle with the green for the body of the snake//
            }
        }
        g.setColor(Color.white); //sets the color to white//
        g.setFont(new Font("Serif",Font.BOLD, 40)); //creates a fonts size and font//
        FontMetrics metrics = getFontMetrics(g.getFont()); 
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()); //places the score in the top middle of the screen//

        } else {
            gameOver(g); //calls on the game over method//
        }
    }

    public void newApple() { //new apple method//
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE; //generates a random x coordinate for the apple//
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE; //generates a random y coordinate for the apple//
    }

    public void move() {
        for(int i=bodyParts;i>0;i--) { //for loop that iterates through all parts of the snake//
            x[i] = x[i-1]; //shifts all x-coordinates in array by 1//
            y[i] = y[i-1]; //shifts all y-coordinates in array by 1//
        }
        switch(direction) { //use a switch function to execute U, D, R, L// //switch can be used in place of if, and if-else statements//
            case 'U': 
                y[0] = y[0] - UNIT_SIZE; //moves the snake up by the UNNIT_SIZE//
                break;
            case 'D': 
                y[0] = y[0] + UNIT_SIZE; //moves the snake down by the UNNIT_SIZE//
                break;
            case 'L': 
                x[0] = x[0] - UNIT_SIZE; //moves the snake left by the UNIT_SIZE
                break;
            case 'R': 
                x[0] = x[0] + UNIT_SIZE; //moves the snake right by the UNNIT_SIZE//
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) { //if statement checks if snake has eaten an apple//
            bodyParts++; //body parts increase by 1//
            applesEaten++; //apples eaten increase by 1//
            newApple(); //calls on the new apple method to make a new apple//
            PlaySound(Clap); //calls on th playSOund method to play sound//
        }

    }

    public void PlaySound(File Sound) { //takes the file object named sound as the parameter//
        new Thread(() -> { //creates a new thread, used to play sound withough freezing//
        try{
            Clip clip = AudioSystem.getClip(); //retrieves a clip from the auiosystem//
            clip.open(AudioSystem.getAudioInputStream(Sound)); //opens audio input stream from the file and associates it with the clip object//
            clip.addLineListener(event -> { //adds a line listener to the clip object//
                if (event.getType() == LineEvent.Type.STOP) { //checks for a stop event//
                    clip.close(); //closes the clip//
                }  
            });
            clip.start(); //starts the playback of the audio//
        }catch(Exception e){ 
        }
        }).start(); //allows the code to execute asynchronously//
    }

    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts;i>0;i--) { //for loop that keeps track of the body//
            if((x[0] == x[i] && (y[0] == y[i]))) { //checks if the snake head hits the body//
                running = false; //game stops running//
                System.out.println("hit yourself");
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right corner
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running) { //if it is not running //
            timer.stop(); //timer stops//
        }
    }

    public void gameOver(Graphics g){ //gameOver method//
        //Game over text
        g.setColor(Color.red); //sets the color to red//
        g.setFont(new Font("Serif",Font.BOLD, 75)); //sets the font size and style//
        FontMetrics metrics = getFontMetrics(g.getFont()); //retrieves the font to be used//
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); //places the text in the middle of the screen//
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()); //keeps the score displayes on the game over screen//
        PlaySound(gameOverSound); //plays the game over sound//
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) { //if the program is running//
            move(); //calls on the move method//
            checkApple(); //calls on the checkApple method//
            checkCollisions(); //calls on the checkCollisions method//
        }
        repaint(); //calls this method when game is no longer running//
    }

     public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) { //method that takes informtion bout the keys that was pressed//
            switch(e.getKeyCode()) { // switch function that gets the keys that was pressed//
            case KeyEvent.VK_LEFT: 
                if(direction != 'R') {
                    direction = 'L'; //if the direction you are going is not right, you go left//
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'L') {
                    direction = 'R'; //if the direction you are going is not left, you go left//
                }
                break;
            case KeyEvent.VK_UP:
                if(direction != 'D') {
                    direction = 'U'; //if the direction you are going is not down, you go up// 
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 'U') {
                    direction = 'D'; //if the direction you are going is not up, you go down//
                }
                break;
        
            }
        }
    }      
}