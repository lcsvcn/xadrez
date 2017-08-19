/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess.chess.pieces;

import java.awt.Graphics2D;
import java.io.IOException;

/**
 *
 * @author Lucas V. C. Nicolau
 */
public class Knight extends Piece {

    private final int inicialCol;
    private final int inicialLin;
    public boolean blocked;             // Nao é usado pelo cavalo, pois ele pode mover livremente em L
    private final String strColor;

    public Knight(Team color, String str, int x, int y) {
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
            g.drawImage(img, x0, y0, x1, y1, 260, 20, 300, 60, null);
        } else {
            g.drawImage(img, x0, y0, x1, y1, 260, 72, 300, 112, null);
        }
    }

    @Override
    public boolean isMovementAllow(int x, int y) {

        if (position.x == x && position.y == y) {
            return false;
        }

        if (position.x == (x + 1) || position.x == (x - 1)) {
            if (position.y == (y + 2) || position.y == (y - 2)) {
                return true;
            }
        }

        if (position.x == (x + 2) || position.x == (x - 2)) {
            if (position.y == (y + 1) || position.y == (y - 1)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getInicialLin() {
        return this.inicialLin;
    }

    @Override
    public int setValueToMatriz(int x, int y) {

        if (position.x == x && position.y == y) {
            return 0;
        }

        if (position.x == (x + 1) || position.x == (x - 1)) {
            if (position.y == (y + 2) || position.y == (y - 2)) {
                return 1;
            }
        }

        if (position.x == (x + 2) || position.x == (x - 2)) {
            if (position.y == (y + 1) || position.y == (y - 1)) {
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
        return "C"+convertToNotation(position.x, position.y);
    }
}
