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
public class Pawn extends Piece {

    private final int inicialLin;
    private boolean blocked;   // Nao é usado no Peao, pois eles são da fileira da frente
    private boolean specialPawn;
    private boolean enPassant;
    private final int inicialCol;
    private final String strColor;
    private int lastPositionX;
    private int lastPositionY;

    public Pawn(Team color, String str, int x, int y) {
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
            g.drawImage(img, x0, y0, x1, y1, 320, 20, 360, 60, null);
        } else {
            g.drawImage(img, x0, y0, x1, y1, 320, 72, 360, 112, null);
        }
    }

    @Override
    public void move(int x, int y) {
//        int aux = 1;
//
//        int cte;
//        if (inicialLin == 1) {
//            cte = -1;
//        } else {
//            cte = 1;
//        }
        
        this.lastPositionX = this.position.x;
        this.lastPositionY = this.position.y;

        this.position.x = x;
        this.position.y = y;
//
//        if (!enPassant) {
//            if (lastPositionX == (position.x + 1) || lastPositionX == (position.x - 1)) {
//                if (lastPositionY == position.y-cte) {
//                    aux = 2;
//                }
//            }
//        }
//        this.enPassant = aux == 2;

        this.specialPawn = true;
    }

    @Override
    public boolean isMovementAllow(int x, int y) {
        int cte;
        if (inicialLin == 1) {
            cte = -1;
        } else {
            cte = 1;
        }

        if (position.x == x && position.y == y) {
            return false;
        }

        if (position.x == x) {      // mesma linha
            if (!specialPawn) {
                if (position.y == (y + (2 * cte))) {
                    return true;
                }

//                if (this.enPassant) {
//                    if (position.y == (y - cte)) {
//                        return true;
//                    }
//                }
            }
            if (position.y == (y + cte)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getLastX() {
        return this.lastPositionX;
    }

    @Override
    public int getLastY() {
        return this.lastPositionY;
    }

    @Override
    public int getInicialLin() {
        return this.inicialLin;
    }

    @Override
    public int setValueToMatriz(int x, int y) {
        int cte;
        
        if (inicialLin < 2) {
            cte = -1;
        } else {
            cte = 1;
        }

        if (position.x == x && position.y == y) {
            return 0;
        }

        if (position.x == x) {      // mesma linha
            if (!specialPawn) {
                if (position.y == y + 2 * cte) {
                    return 1;
                }
            }
            if (position.y == (y + 1 * cte)) {
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
        return convertToNotation(position.x, position.y);
    }

}
