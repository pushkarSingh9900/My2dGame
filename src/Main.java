import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel implements Runnable{

    // screen settings
    final int originalTilesSize = 16; // 16*16 title
    final int scale = 3;

    public final int tileSize = originalTilesSize * scale; // 48*48 scale
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound se = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;
    // ENTITY AND OBJ
    public characters player = new characters(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();

        playMusic(0);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void run(){

        double drawInterval = 1000000000/FPS; // 0.0166666 seconds
        double delta =0;
        long lastTime = System.nanoTime();
        long currentTime;
        while(gameThread!= null){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;


           if(delta>=1){
               // 1 update : update info like, character position
               update();
               // 2 draw : draw the screen with the updated info
               repaint();
               delta--;
           }
        }
    }
    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //DEBUG
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }
        drawStart = System.nanoTime();

        //tile
        tileM.draw(g2);
        //object
        for(int i =0; i< obj.length; i ++){
            if(obj[i] != null){
                obj[i].draw(g2,this);
            }
        }
        //player
        player.draw(g2);

        //UI
        ui.draw(g2);

        //DEBUG
        if(keyH.checkDrawTime==true) {

            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time:" + passed);
        }
        g2.dispose();
    }
    public void playMusic(int i ){
        music.setFile(i);
        music.play();
        music.loop();

    }
    public void stopMusic(){
        music.stop();
    }
    public void playerSE(int i){
        se.setFile(i);
        se.play();
    }
}
public class Main {
    public static void main(String[] args){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}