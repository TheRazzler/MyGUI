import java.util.Random;
public class Example implements Game {
  int score = 0;
  double enemySpeed = 1.0/120;
  double respawnChance = .01;
  double enemyProgress = 0;
  boolean mouseDown = false;
  int beamSprite = 0;
  Sprite background = new Sprite("example_sprites/background.png");
  Sprite player = new Sprite("example_sprites/player.png");
  Sprite[] beams = new Sprite[10];
  Sprite[] enemies = new Sprite[50];
  Window window;
  public static void main(String[] args) {
    Example example = new Example();
    for(int i = 0; i < example.beams.length; i++) {
      example.beams[i] = new Sprite("example_sprites/beam1.png", "example_sprites/beam2.png");
    }
    
    for(int i = 0; i < example.enemies.length; i++) {
      example.enemies[i] = new Sprite("example_sprites/enemy.png");
    }
    
    example.spawn(example.enemies);
    example.player.setX(400);
    example.player.setY(800 - example.player.getHeight());
    example.background.show();
    example.player.show();
    
    example.window = new Window(800, 800, "Spencer's example", example);
  }
  
  public void tick() {
    if(Math.random() < respawnChance) {
      spawn(enemies);
    }
    
    enemyProgress += enemySpeed;
    
    while(enemyProgress > 1) {
      enemyProgress -= 1;
      for(int i = 0; i < enemies.length; i++) {
        if(enemies[i].isVisible()) {
          enemies[i].setY(enemies[i].getY() + 8);
          if(enemies[i].getY() + enemies[i].getWidth() >= 800) {
            enemies[i].setY(0);
          }
        }
      }
    }
    
    if(window.keyPressed('A') && player.getX() >= 0) {
      player.setX(player.getX() - 8);
    } else if(window.keyPressed('D') && player.getX() + player.getWidth() <= 800) {
      player.setX(player.getX() + 8);
    }
    
    if(window.mouseDown() && !mouseDown) {
      shoot(beams, player);
      mouseDown = true;
    }
    if(!window.mouseDown()) {
      mouseDown = false;
    }
    for(int i = 0; i < beams.length; i++) {
      if(beams[i].isVisible()) {
        beams[i].setY(beams[i].getY() - 8);
        beams[i].switchToImage(beamSprite);
      }
    }
    beamSprite = (beamSprite + 1) % 2;
    
    for(int i = 0; i < enemies.length; i++) {
      if(enemies[i].isVisible()) {
        for(int j = 0; j < beams.length; j++) {
          if(beams[j].isVisible()) {
            if(enemies[i].touching(beams[j])) {
              enemies[i].hide();
              beams[j].hide();
              score += 100;
            }
            if(beams[j].getY() < 0) {
              beams[j].hide();
            }
          }
        }
        if(enemies[i].touching(player)) {
          System.out.println("Game over");
          System.out.println("Score: " + score);
          window.close();
          System.exit(1);
        }
      }
    }
    enemySpeed += .001;
    respawnChance += .00001;
    
  }
  
  public void spawn(Sprite[] enemies) {
    Random r = new Random();
    for(int i = 0; i < enemies.length; i++) {
      if(!enemies[i].isVisible()) {
        enemies[i].setX(r.nextInt(94) * 8);
        enemies[i].setY(0);
        enemies[i].show();
        break;
      }
    }
  }
  
  public void shoot(Sprite[] beams, Sprite player) {
    for(int i = 0; i < beams.length; i++) {
      if(!beams[i].isVisible()) {
        beams[i].setY(800 - player.getHeight());
        beams[i].setX(player.getX() + player.getWidth() / 2);
        beams[i].show();
        break;
      }
    }
  }
}