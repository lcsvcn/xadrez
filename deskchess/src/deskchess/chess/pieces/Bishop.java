/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess.chess.pieces;

import java.awt.Graphics2D;
import java.io.IOException;
import static java.lang.StrictMath.abs;

/**
 *
 * @author Lucas V. C. Nicolau
 *
 */
public class Bishop extends Piece  {

    private final int inicialCol;
    private final int inicialLin;
    private boolean blocked;
    private final String strColor;

    public Bishop(Team color, String str, int x, int y) {
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
            g.drawImage(img, x0, y0, x1, y1, 200, 20, 240, 60, null);
        } else {
            g.drawImage(img, x0, y0, x1, y1, 200, 72, 240, 112, null);
        }
    }

    @Override
    public boolean isMovementAllow(int x, int y) {
        int aux = abs(position.y - y);

        if (position.x == x && position.y == y) {
            return false;
        }

        return position.x == (x + aux) || position.x == (x - aux);
    }

    @Override
    public int getInicialLin() {
        return this.inicialLin;
    }

    @Override
    public int setValueToMatriz(int x, int y) {
        int aux = abs(position.y - y);

        if (position.x == x && position.y == y) {
            return 0;
        }

        if (position.x == (x + aux) || position.x == (x - aux)) {
            return 1;
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
        return "D" + convertToNotation(position.x, position.y);
    }
}
