/**
 *
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Lucas V. C. Nicolau
 *
 */
public class Board extends JPanel {

    Chess c = Chess.getChess();

    public Board() {
        super();
    }

    private void drawBoard(Graphics2D g) {
        g.setBackground(Color.WHITE);
        g.setColor(Color.GRAY);
        float maxWidth = this.getWidth();
        float maxHeight = this.getHeight();
        float boardSize = (maxWidth < maxHeight) ? maxWidth : maxHeight;
        int spotSize = Math.round(boardSize / 8.0f);

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                //varia a cor do quadrante
                if (g.getColor() == Color.WHITE) {
                    g.setColor(Color.GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }

                //Desenha o tabuleiro
                g.fillRect(i * spotSize, j * spotSize, (i * spotSize) + spotSize, (j * spotSize) + spotSize);
            }

            if (g.getColor() == Color.WHITE) {
                g.setColor(Color.GRAY);
            } else {
                g.setColor(Color.WHITE);
            }
        }

    }

    @Override //sobrescrita do metodo paintComponent da classe JPanel
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        // Desenha o tabuleiro (quadros pretos e brancos..
        drawBoard(g2);

        // Passa a graphics2D para o chess
//        c.setGraphics(g2);

        // e desenha as pecas....    
        c.draw(g2);
        
//        if (c.active) {
            c.drawAllowMovement(g2);
            c.drawSelection(g2);
//        }

    }

}
