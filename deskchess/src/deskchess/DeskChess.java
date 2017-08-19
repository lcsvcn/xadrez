/**
 *
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package deskchess;

import view.Chess;
import view.DeskChessFrame;

/**
 *
 * @author Lucas V. C. Nicolau
 */
public class DeskChess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Chess chess = new Chess();
            new DeskChessFrame(chess).setVisible(true);
        });
    }

}
