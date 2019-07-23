import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;

import java.util.ArrayList;

public class Sprite {
  public static ArrayList<Sprite> spriteTable = new ArrayList<Sprite>(100);
  
  private BufferedImage[] images;
  private BufferedImage image;
  private int x;
  private int y;
  private int width;
  private int height;
  private boolean visible;
  private int idx;
  
  public Sprite(String ... paths) {
    images = new BufferedImage[paths.length];
    for(int i = 0; i < paths.length; i++) {
      try {
        images[i] = ImageIO.read(new File(paths[i]));
      } catch(IOException e) {
        throw new IllegalArgumentException("Trouble accessing file '" + paths[i] + "'");
      }
    }
    switchToImage(0);
  }
  
  public boolean touching(Sprite other) {
    if(x + width < other.x || x > other.x + width)
      return false;
    if(y + height < other.y || y > other.y + height)
      return false;
    return true;
  }
  
  public synchronized void render(Graphics g) {
    g.drawImage(image, x, y, null);
  }
  
  public synchronized void switchToImage(int idx) {
    image = images[idx];
    width = image.getWidth();
    height = image.getHeight();
  }
  
  public synchronized void show() {
    if(!visible) {
      idx = spriteTable.size();
      spriteTable.add(this);
      visible = true;
    }
  }
  
  public synchronized void hide() {
    if(visible) {
      spriteTable.remove(idx);
      for(int i = idx; i < spriteTable.size(); i++) {
        spriteTable.get(i).idx--;
      }
      visible = false;
    }
  }
  
  public boolean isVisible() {
    return visible;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
}