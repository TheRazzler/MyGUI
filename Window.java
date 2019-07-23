import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Point;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class Window {
  private static final long SCREEN_REFRESH = 17;
  public static final char LEFT = (char) 17;
  public static final char RIGHT = (char) 18;
  public static final char UP = (char) 19;
  public static final char DOWN = (char) 20;
  
  private JFrame frame;
  private Canvas canvas;
  private Color background;
  private Game game;
  private boolean running;
  
  public boolean mouseDown;
  
  private boolean[] keysPressed = new boolean[90];
  
  public Window(int width, int height, String name, Game game) {
    frame = new JFrame(name);
    frame.setSize(width, height);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    
    canvas = new Canvas();
    frame.add(canvas);
    canvas.setPreferredSize(new Dimension(width, height));
    frame.pack();
    
    MouseListener mouse = new MouseListener() {
      public void mousePressed(MouseEvent e) {
        mouseDown = true;
      }

      public void mouseReleased(MouseEvent e) {
        mouseDown = false;
      }

      public void mouseEntered(MouseEvent e) {}

      public void mouseExited(MouseEvent e) {}

      public void mouseClicked(MouseEvent e) {}
    };
    
    frame.addMouseListener(mouse);
    canvas.addMouseListener(mouse);
    KeyListener keys = new KeyListener() {
      public void keyTyped(KeyEvent e) {}
      public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if(c != KeyEvent.CHAR_UNDEFINED) {
          keysPressed[Character.toUpperCase(c) + 0] = true;
        } else {
          int code = e.getKeyCode();
          if(code == KeyEvent.VK_LEFT) {
            keysPressed[LEFT + 0] = true;
          } else if(code == KeyEvent.VK_RIGHT) {
            keysPressed[RIGHT + 0] = true;
          } else if(code == KeyEvent.VK_UP) {
            keysPressed[UP + 0] = true;
          } else if(code == KeyEvent.VK_DOWN) {
            keysPressed[DOWN + 0] = true;
          }
        }
      }
      
      public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        if(c != KeyEvent.CHAR_UNDEFINED) {
          keysPressed[Character.toUpperCase(c) + 0] = false;
        } else {
          int code = e.getKeyCode();
          if(code == KeyEvent.VK_LEFT) {
            keysPressed[LEFT + 0] = false;
          } else if(code == KeyEvent.VK_RIGHT) {
            keysPressed[RIGHT + 0] = false;
          } else if(code == KeyEvent.VK_UP) {
            keysPressed[UP + 0] = false;
          } else if(code == KeyEvent.VK_DOWN) {
            keysPressed[DOWN + 0] = false;
          }
        }
      }
    };
    
    frame.addKeyListener(keys);
    canvas.addKeyListener(keys);
    
    frame.setVisible(true);
    System.out.println("CLOSE the window to stop the program");
    background = Color.WHITE;
    
    Runnable run = new Runnable() {
      @Override
      public void run() {
        long last = System.currentTimeMillis();
        while(running) {
          long current = System.currentTimeMillis();
          if(current - last >= SCREEN_REFRESH) {
            game.tick();
            render();
            last = current;
          }
        }
      }
    };
    Thread t = new Thread(run);
    running = true;
    t.start();
  }
  
  private void render() {
    BufferStrategy bs = canvas.getBufferStrategy();
    if(bs == null) {
      canvas.createBufferStrategy(3);
    } else {
      Graphics g = bs.getDrawGraphics();
      g.setColor(background);
      g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
      for(int i = 0; i < Sprite.spriteTable.size(); i++) {
        Sprite.spriteTable.get(i).render(g);
      }
      bs.show();
      g.dispose();
    }
  }
  
  public synchronized void setBackgroundColor(Color c) {
    background = c;
  }
  
  private synchronized Color getBackgroundColor() {
    return background;
  }
  
  public synchronized boolean mouseDown() {
    return mouseDown;
  }
  
  public synchronized int[] mousePosition() {
    Point p = canvas.getMousePosition();
    if(p == null)
      p = frame.getMousePosition();
    if(p == null)
      return new int[] {0, 0};
    return new int[] {p.x, p.y};
  }
  
  public synchronized boolean keyPressed(char c) {
    return keysPressed[c + 0];
  }
  
  public synchronized void close() {
    running = false;
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }
}