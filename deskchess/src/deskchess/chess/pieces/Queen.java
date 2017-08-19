/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess.chess.pieces;

import java.awt.Graphics2D;
import java.io.IOException;
import deskchess.chess.pieces.Piece.Team;
import static java.lang.StrictMath.abs;

/**
 *
 * @author Lucas V. C. Nicolau
 */
public class Queen extends Piece {

    private final int inicialCol;
    private final int inicialLin;
    private boolean blocked;
    private final String strColor;

    public Queen(Team color, String str, int x, int y) throws IOException {
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

        if (this.color == Team.black) {
            g.drawImage(img, x0, y0, x1, y1, 80, 20, 120, 60, null);
        } else {
            g.drawImage(img, x0, y0, x1, y1, 80, 72, 120, 112, null);
        }
    }

    @Override
    public boolean isMovementAllow(int x, int y) {
        int aux = abs(position.y - y);

        // Logica diagonal (igual do bispo
        if (position.x == (x + aux) || position.x == (x - aux)) {
            return true;
        }

        /* Logica horizontal e vertical (igual da torre) */
        if (position.x == x) {
            return true;    // nao implementado 
        }
        if (position.y == y) {
            return true;
        }

        // Logica de um quadrado ao redor (igual do rei)
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

        // caso o contrario
        return false;
    }

    @Override
    public int getInicialLin() {
        return inicialLin;
    }

    @Override
    public int getInicialCol() {
        return inicialCol;
    }

    @Override
    public int setValueToMatriz(int x, int y) {
        int aux = abs(position.y - y);

        if (position.x == x && position.y == y) {
            return 0;
        }

        // Logica diagonal (igual do bispo)
        if (position.x == (x + aux) || position.x == (x - aux)) {
            return 1;
        }

        /* Logica horizontal e vertical (igual da torre) */
        if (position.x == x) {
            return 1;
        }
        if (position.y == y) {
            return 1;
        }

        // Logica de um quadrado ao redor (igual do rei)
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
    public String getColor() {
        return strColor;
    }

    @Override
    public String getNotation() {
        return "D"+convertToNotation(position.x, position.y);
    }
}
