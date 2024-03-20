import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_chest extends SuperObject {
    GamePanel gp;
    public OBJ_chest(GamePanel gp){

        this.gp = gp;
        name = "Chest";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
