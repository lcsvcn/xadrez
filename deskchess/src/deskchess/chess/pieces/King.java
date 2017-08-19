/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess.chess.pieces;

import java.awt.Graphics2D;

/**
 *
 * @author Lucas V. C. Nicolau
 */
public class King extends Piece {

    private final int inicialLin;
    private boolean blocked;             // Nao é usado pelo rei, por ele mover uma casa apenas e a verificação de casa bastar
    private final int inicialCol;
    private final String strColor;

    public King(Team color, String str, int x, int y) {
        super(color, x, y);

        this.strColor = str;
        this.inicialCol = x;
        this.inicialLin = y;
    }

    @Override
    public void draw(Graphics2D g) {
        int squareWidth = g.getClip().getBounds().width / 8;
        int squareHeight = g.getClip().getBounds().height / 8;

        int x0 = position.x * squareWidth;
        int y0 = position.y * squareHeight;
        int x1 = x0 + squareWidth;
        int y1 = y0 + squareHeight;

        /// dada a figura INTEIRA, seleciona apenas a area relativa ao Peao
        // Isso é um cropping na imagem....
        if (this.color == Team.black) {
            g.drawImage(img, x0, y0, x1, y1, 20, 20, 60, 60, null);
        } else {
            g.drawImage(img, x0, y0, x1, y1, 20, 72, 60, 112, null);
        }
    }

    @Override
    public boolean isMovementAllow(int x, int y) {

        if (position.x == x && position.y == y) {
            return false;
        }

        if (position.x == x) {
            if (position.y == (y + 1) || position.y == (y - 1)) {
                return true;
            }
        }

        if (position.y == y) {         // mesma coluna
            if (position.x == (x - 1) || position.x == (x + 1)) {
                return true;
            }
        }

        if (position.x == (x + 1) || position.x == (x - 1)) {
            if (position.y == (y - 1) || position.y == (y + 1)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getInicialLin() {
        return this.inicialLin;
    }

    public void setBlocked(boolean bln) {
        this.blocked = bln;
    }

    @Override
    public int setValueToMatriz(int x, int y) {

        if (position.x == x && position.y == y) {
            return 0;
        }

        if (position.x == x) {
            if (position.y == (y + 1) || position.y == (y - 1)) {
                return 1;
            }
        }

        if (position.y == y) {         // mesma coluna
            if (position.x == (x - 1) || position.x == (x + 1)) {
                return 1;
            }
        }

        if (position.x == (x + 1) || position.x == (x - 1)) {
            if (position.y == (y - 1) || position.y == (y + 1)) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int getInicialCol() {
        return inicialCol;
    }

    @Override
    public String getColor() {
        return strColor;
    }

    @Override
    public String getNotation() {
        return "K"+convertToNotation(position.x, position.y); 
    }
}
