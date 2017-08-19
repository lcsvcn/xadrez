/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess.chess.pieces;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Lucas V. C. Nicolau
 */
public abstract class Piece implements Serializable {
    public boolean removePiece;
    public static BufferedImage img;
    protected Point position;
    protected Team color;

    private static final String imgPath = "src/img/pecas.png";

    public abstract int getInicialLin();

    public abstract int getInicialCol();
    
    // private File imgPath;
    public enum Team {

        white,
        black
    }

    public Piece(Team color, int x, int y) {
        this.color = color;
        this.position = new Point(x, y);

        loadImage();
    }
    
    private static void loadImage() {
            try {
                img = ImageIO.read(new File(imgPath));
            } catch (IOException ex) {
                Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public abstract void draw(Graphics2D g);

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public abstract boolean isMovementAllow(int x, int y);

    public abstract String getColor();

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getLastX() {
        return -1;
    }

    public int getLastY() {
        return -1;
    }

    public void move(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }
    
    protected String convertToNotation(int x, int y) {
        switch(x) {
            case 0:
                return "a"+y;
                
            case 1:
                return "b"+y;
                
            case 2:
                return "c"+y;
                
            case 3:
                return "d"+y;
            
            case 4:
                return "e"+y;
            
            case 5:
                return "f"+y;
            
            case 6:
                return "g"+y;
           
            case 7:
                return "h"+y;
                
            default:
                return "ERROR";
        }
    }
    
    public abstract int setValueToMatriz(int x, int y);
    
    // Retorna para o log
    public abstract String getNotation();
    
    @Override
    public String toString() {
        return super.getClass().getName().substring((getClass().getName().lastIndexOf('.')) + 1).toUpperCase(); //To change body of generated methods, choose Tools | Templates.
    }
}
