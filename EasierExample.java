import java.util.Random;
/**
 * This is an example of how to use my GUI interface for a game
 * @author Spencer Yoder
 */
public class EasierExample implements Game { //The class definition MUST include "implements Game"
  //All the game's resources go up here, right under the class name
  Sprite badGuy = new Sprite("example_sprites/enemy.png");
  int timeLeft = 60 * 10;
  boolean mouseDown = false;
  //ALWAYS include this line up here
  Window window = null;
  int score = 0;
  
  public static void main(String[] args) {
    //Have this line, where EasierExample is the class name
    EasierExample e = new EasierExample();
    //This line starts the game
    e.window = new Window(800, 800, "Easier example", e);
  }
  
  /**
   * When the game starts, this method runs 60 times per second
   * Use the game resources in this method
   */
  public void tick() {
    Random r = new Random();
    if(!badGuy.isVisible()) {
      int x = r.nextInt(800 - badGuy.getWidth());
      int y = r.nextInt(800 - badGuy.getHeight());
      badGuy.setX(x);
      badGuy.setY(y);
      badGuy.show();
    }
    int[] position = window.mousePosition();
    int mouseX = position[0];
    int mouseY = position[1];
    boolean clicked = false;
    if(mouseDown == false && window.mouseDown() == true) {
      clicked = true;
      mouseDown = true;
    }
    
    if(mouseX > badGuy.getX() && mouseX < badGuy.getX() + badGuy.getWidth() && mouseY > badGuy.getY() && mouseY < badGuy.getY() + badGuy.getHeight()) {
      if(clicked) {
        score++;
        badGuy.hide();
      }
    }
    
    if(window.mouseDown() == false) {
      mouseDown = false;
    }
    timeLeft--;
    if(timeLeft <= 0) {
      System.out.println("Game over!");
      System.out.println("Score: " + score);
      window.close();
    }
  }
}