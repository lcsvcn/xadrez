/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package view;

import deskchess.chess.pieces.*;
import deskchess.chess.pieces.Piece.Team;
import saves.SaveAndLoad;
import saves.saveVariables;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import log.Log;
import timer.AutoSave;

public final class Chess {
    /* -- Private -- */
    
    private static Chess chessSingleton = null;   // Singleton desta classe
    private final ArrayList<Piece> whitePieces;  // Vetor para as pecas brancas
    private final ArrayList<Piece> blackPieces;  // Vetor para as pecas pretas
    private final Log lChess;
    private final ArrayList<String> Log;         // Vetor de log
    private final int mLast[] = new int[2];     // Armazena o click anterior do mouse
    private final int mCurrent[] = new int[2];  // Armazena o click atual do mouse
    private int[][] matAllowPosition;
    private int[][] matBlackCheckMate;
    private int[][] matWhiteCheckMate;
    private String promotion;
    private int auxiliarPromotionLin;
    private int auxiliarPromotionCol;
    private Graphics2D g2_aux;
    private boolean blackKingInCheck;
    private boolean whiteKingInCheck;
    private boolean initialSave;
    private final AutoSave auto;
    /* -- Public -- */
    //Booleans :
//    public boolean active;                 // Jogo ativo
    public boolean allowMovement;          // Movimento permitido
    public boolean selectPiece;            // Peca selecionada
    public boolean firstMove;              // Primeiro movimento
    public boolean showAllAllowMovement;   // Mostra movimentos permitidos
    public boolean changeTurn;             // Troca o turno (mensagem)
    public int turn;                       // Armazena o turno (1 - player 1, 2 - player 2)

    protected mouseAction mAction;         // Utiliza o enum para armazenar os clicks do mouse 

    protected boolean enPassant;
    private Piece pReference;
    private boolean showAlertCheck;
    private boolean blackKingInCheckMate;
    private boolean whiteKingInCheckMate;

    private SaveAndLoad sl;
    private SaveAndLoad undo;
    private SaveAndLoad redo;
    private final saveVariables sVar;
    private boolean canRedo;
    private String cTotal;
    private String cTurn;
    private boolean canUndo;
    private float alpha;
    private float waitAutoSave;
    private String removeLineLog;
    private List list1;
    private boolean bRandom;
    private int player;
    
    // Enumera current e last - obs.: ver int[] mLast e int[] mCurrent[]
    public enum mouseAction {
        last,
        current
    }



    public Chess() {
        this.sVar = new saveVariables("src/files/var.txt");
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        Log = new ArrayList<>();
        lChess = new Log();

        init();

        this.auto = new AutoSave(this);

        chessSingleton = this;
    }

    // Inicia tudo do xadrez ( desta classe principal )
    public void init() {
            this.initialSave = true;
            this.waitAutoSave = 2.0f;  // Tempo padrão - 2 min
            this.turn = 1;
            this.matAllowPosition = new int[8][8];
            this.matBlackCheckMate = new int[8][8];
            this.matWhiteCheckMate = new int[8][8];

            whitePieces.clear();            // Limpa o vetor de pecas brancas
            blackPieces.clear();            // Limpa o vetor de pecas pretas
            
            bRandom= false;
            initPieces(bRandom);

            if (sl == null) {
                this.sl = new SaveAndLoad("src/files/data0.bin", "src/files/data1.bin", whitePieces, blackPieces);
            }
            if (undo == null) {
                this.undo = new SaveAndLoad("src/files/undo0.bin", "src/files/undo1.bin", whitePieces, blackPieces);
                this.canUndo = false;
            }
            if (redo == null) {
                this.redo = new SaveAndLoad("src/files/redo0.bin", "src/files/redo1.bin", whitePieces, blackPieces);
                this.canRedo = false;
            }
    }
    
    private void initPieces(boolean random) {
        if(random) {
            initPiecesMode(randomColor());
        } else {
            initPiecesMode(2);
        }
    }
    
    private void initPiecesMode(int mode) {
        try {
            if(mode == 1) {
                for (int i = 0; i < 8; i++) {
                    whitePieces.add(new Pawn(Team.white, "WHITE", i, 1));
                }

                whitePieces.add(new Knight(Team.white, "WHITE", 2, 0));
                whitePieces.add(new Knight(Team.white, "WHITE", 5, 0));
                whitePieces.add(new Bishop(Team.white, "WHITE", 1, 0));
                whitePieces.add(new Bishop(Team.white, "WHITE", 6, 0));
                whitePieces.add(new Rook(Team.white, "WHITE", 0, 0));
                whitePieces.add(new Rook(Team.white, "WHITE", 7, 0));
                whitePieces.add(new Queen(Team.white, "WHITE", 3, 0));
                whitePieces.add(new King(Team.white, "WHITE", 4, 0));

                for (int i = 0; i < 8; i++) {
                    blackPieces.add(new Pawn(Team.black, "BLACK", i, 6));
                }
                blackPieces.add(new Knight(Team.black, "BLACK", 2, 7));
                blackPieces.add(new Knight(Team.black, "BLACK", 5, 7));
                blackPieces.add(new Bishop(Team.black, "BLACK", 1, 7));
                blackPieces.add(new Bishop(Team.black, "BLACK", 6, 7));
                blackPieces.add(new Rook(Team.black, "BLACK", 0, 7));
                blackPieces.add(new Rook(Team.black, "BLACK", 7, 7));
                blackPieces.add(new Queen(Team.black, "BLACK", 3, 7));
                blackPieces.add(new King(Team.black, "BLACK", 4, 7));
            } else {
                for (int i = 0; i < 8; i++) {
                    blackPieces.add(new Pawn(Team.black, "BLACK", i, 1));
                }
                blackPieces.add(new Knight(Team.black, "BLACK", 2, 0));
                blackPieces.add(new Knight(Team.black, "BLACK", 5, 0));
                blackPieces.add(new Bishop(Team.black, "BLACK", 1, 0));
                blackPieces.add(new Bishop(Team.black, "BLACK", 6, 0));
                blackPieces.add(new Rook(Team.black, "BLACK", 0, 0));
                blackPieces.add(new Rook(Team.black, "BLACK", 7, 0));
                blackPieces.add(new King(Team.black, "BLACK", 3, 0));
                blackPieces.add(new Queen(Team.black, "BLACK", 4, 0));

                for (int i = 0; i < 8; i++) {
                    whitePieces.add(new Pawn(Team.white, "WHITE", i, 6));
                }
                whitePieces.add(new Knight(Team.white, "WHITE", 2, 7));
                whitePieces.add(new Knight(Team.white, "WHITE", 5, 7));
                whitePieces.add(new Bishop(Team.white, "WHITE", 1, 7));
                whitePieces.add(new Bishop(Team.white, "WHITE", 6, 7));
                whitePieces.add(new Rook(Team.white, "WHITE", 0, 7));
                whitePieces.add(new Rook(Team.white, "WHITE", 7, 7));
                whitePieces.add(new King(Team.white, "WHITE", 3, 7));
                whitePieces.add(new Queen(Team.white, "WHITE", 4, 7));
            }
        } catch (Exception IOException) {
            System.err.println("Error");
        }
    }

   
    /* Randomiza o modo de inicio de jogo (Cores) - designando qual cor o p1 e o p2 serao */
    private int randomColor() {
        int rand;

        rand = (int) (Math.random() * 2);

        return rand;
    }
    
    public void setMatrizAllowPositionToZero() {
        pReference = null;      // remove referencia a peça
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matAllowPosition[i][j] = 0;
            }
        }
    }

    private void setMatrizToZero(int[][] mat) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mat[i][j] = 0;
            }
        }
    }

    /* Verifica se existe alguma peça dentro do intervalo de [from; to] -- Usado no movimento especial de roque */
    private boolean isAnyPieceAtInterval(int inicialLin, int from, int to) {
        boolean empty = true;
        for (Piece p : whitePieces) {
            if (p.getX() > from && p.getX() < to && p.getY() == inicialLin) {
                if (matAllowPosition[p.getX()][p.getY()] != 0) {
                    empty = false;
                }
            }
            if (!empty) {
                break;
            }
        }

        for (Piece p : blackPieces) {

            if (p.getX() > from && p.getX() < to && p.getY() == inicialLin) {
                if (matAllowPosition[p.getX()][p.getY()] != 0) {
                    empty = false;
                }
            }
            if (!empty) {
                break;
            }
        }

        return empty;
    }

    /* Verifica se existe peça na posicao x e y */
    private boolean isAnyPieceAtPosition(int x, int y) {

        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }

        for (Piece p : blackPieces) {
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private int getConstant() {
        for (Piece p : whitePieces) {
            if (p.getInicialLin() < 2) {
                return 1;
            } else {
                return -1;
            }
        }
        System.err.println("Error\n");
        return 0;
    }
    /* Verifica se existe peça na posicao x e y */
    /* Verifica se é peça correta (peao, rei, ...) */

    private boolean isPieceByKindAtPosition(Piece pKing, int x, int y, int cont) {
        int cte = getConstant();

        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                if (cont < 4 && ("QUEEN".equals(p.toString()) || "ROOK".equals(p.toString()))) {   // Horizontal e Vertical
                    return true;
                } else if (cont > 3 && ("QUEEN".equals(p.toString()) || "BISHOP".equals(p.toString()))) {   // Diagonal
                    return true;
                }
            }
        }

        for (Piece p : blackPieces) {
            if (p.getX() == x && p.getY() == y) {
                if (cont < 4 && ("QUEEN".equals(p.toString()) || "ROOK".equals(p.toString()))) {   // Horizontal e Vertical
                    return true;
                } else if (cont > 3 && ("QUEEN".equals(p.toString()) || "BISHOP".equals(p.toString()))) {   // Diagonal
                    return true;
                }
            }
        }

        return false;
    }

    private boolean doCastling(ArrayList<Piece> pList, Piece pKing, Piece pRook, int iKing, int iRook) {
        
        // Avalia se a peça rei esta na posicao inicial
        if (pKing.getX() != pKing.getInicialCol() || pKing.getY() != pKing.getInicialLin()) {
            return false;
        }

        // Avalia se a peça torre esta na posicao inicial
        if (pRook.getX() != pRook.getInicialCol() || pRook.getY() != pRook.getInicialLin()) {
            return false;
        }

        // -- ANALISE DO TIPO DE ROQUE ---
        if (pKing.getX() < pRook.getX()) {   // Castling menor
            if (!isAnyPieceAtInterval(pKing.getInicialLin(), pKing.getX(), pRook.getX())) {
                pRook.move(pKing.getX() + 1, pRook.getY());
                pKing.move(pKing.getX() + 2, pKing.getY());
                lChess.add(pKing.getColor(), turn, "0-0");
                Log.add("   |   " + pKing.getColor() + "   |   " + "0-0" + "   |"); // old way
                return true;
            }
        } else {                            // Castling maior  
            if (!isAnyPieceAtInterval(pRook.getInicialLin(), pRook.getX(), pKing.getX())) {
                pRook.move(pKing.getX() - 1, pRook.getY());
                pKing.move(pKing.getX() - 2, pKing.getY());
                lChess.add(pKing.getColor(), turn, "0-0-0");
                Log.add("   |   " + pKing.getColor() + "   |   " + "0-0-0" + "  |"); // old way
                return true;
            }
        }
        return false;
    }

    public boolean selectSpecialMovementCastling() {
        Piece pKing = null;     // piece King  (Rei)
        Piece pRook = null;     // piece Rook  (Torre)

        int iKing = -1;     // index King   (Rei)
        int iRook = -1;     // index Rook   (Torre)

        for (Piece p : whitePieces) {
            if (p.getX() == getMouseX(mouseAction.current) && p.getY() == getMouseY(mouseAction.current)) {
                if (null != p.toString()) {
                    switch (p.toString()) {
                        case "KING":
                            pKing = p;
                            iKing = whitePieces.indexOf(p);
                            break;
                        case "ROOK":
                            pRook = p;
                            iRook = whitePieces.indexOf(p);
                            break;
                    }
                }
            }

            if (p.getX() == getMouseX(mouseAction.last) && p.getY() == getMouseY(mouseAction.last)) {
                if (null != p.toString()) {
                    switch (p.toString()) {
                        case "KING":
                            pKing = p;
                            iKing = whitePieces.indexOf(p);
                            break;
                        case "ROOK":
                            pRook = p;
                            iRook = whitePieces.indexOf(p);
                            break;
                    }
                }
            }
        }

        if (pKing != null && pRook != null) {          // selecionado Rei e Torre (Castling => true)
            return doCastling(whitePieces, pKing, pRook, iKing, iRook);
        } else if (pKing != null || pRook != null) {      // seleciona somente uma das peças (Castling => false)
            return false;
        }

        for (Piece p : blackPieces) {
            if (p.getX() == getMouseX(mouseAction.current) && p.getY() == getMouseY(mouseAction.current)) {
                pKing = p;
                iKing = blackPieces.indexOf(p);
            }

            if (p.getX() == getMouseX(mouseAction.last) && p.getY() == getMouseY(mouseAction.last)) {
                pRook = p;
                iRook = blackPieces.indexOf(p);
            }
        }

        if (pKing != null && pRook != null) {
            return doCastling(blackPieces, pKing, pRook, iKing, iRook);
        }
        return false;
    }

    // Seleciona a peca que vai ser trocada e armazena sua string (quando confirmado o peao sera promovido)
    public void selectSpecialTrade(String piece) {
        promotion = piece;
    }

    // Realiza a promoção do peao conforme a escolha feita pelo player
    public void doPromotion() throws IOException {

        for (Piece p : whitePieces) {
            if (p.getInicialLin() == auxiliarPromotionLin && p.getInicialCol() == auxiliarPromotionCol) {

                switch (promotion) {
                    case "KNIGHT":
                        whitePieces.set(whitePieces.indexOf(p), new Knight(Team.white, "WHITE", p.getX(), p.getY()));

                        break;
                    case "QUEEN":
                        whitePieces.set(whitePieces.indexOf(p), new Queen(Team.white, "WHITE", p.getX(), p.getY()));
                        break;
                    case "ROOK":
                        whitePieces.set(whitePieces.indexOf(p), new Rook(Team.white, "WHITE", p.getX(), p.getY()));
                        break;
                    case "BISHOP":
                        whitePieces.set(whitePieces.indexOf(p), new Bishop(Team.white, "WHITE", p.getX(), p.getY()));
                        break;
                }
            }
        }
        for (Piece p : blackPieces) {
            if (p.getInicialLin() == auxiliarPromotionLin && p.getInicialCol() == auxiliarPromotionCol) {

                switch (promotion) {
                    case "KNIGHT":
                        blackPieces.set(blackPieces.indexOf(p), new Knight(Team.black, "BLACK", p.getX(), p.getY()));
                        break;
                    case "QUEEN":
                        blackPieces.set(blackPieces.indexOf(p), new Queen(Team.black, "BLACK", p.getX(), p.getY()));
                        break;
                    case "ROOK":
                        blackPieces.set(blackPieces.indexOf(p), new Rook(Team.black, "BLACK", p.getX(), p.getY()));
                        break;
                    case "BISHOP":
                        blackPieces.set(blackPieces.indexOf(p), new Bishop(Team.black, "BLACK", p.getX(), p.getY()));
                        break;
                }
            }
        }
    }

    // Tempo de espera ( Default é 1 min)
    public void setWaitAutoSave(float waitAutoSave) {
        this.waitAutoSave = waitAutoSave;
    }

    // Tempo de espera ( Default é 1 min)
    public float getWaitAutoSave() {
        return this.waitAutoSave;
    }

    public void setPieceReference(Piece p) {
        this.pReference = p;
    }

    private Piece getPieceFromArrayList(ArrayList<Piece> pList) {
        for (Piece p : pList) {
            if ("KING".equals(p.toString())) {
                return p;
            }
        }
        return null;
    }

    // Seta matriz checkmate do rei branco
    // int x e int y ( é alterar a posicao normal da peça - usado p/ bloquear movimentos)
    private void setMatWhiteCheckMate(int altX, int altY) {
        setMatrizToZero(matWhiteCheckMate);

        Piece pKing = getPieceFromArrayList(whitePieces);   // Pega peça equivalente ao rei branco

        int cont = 0;
        int aux;
        int num;

        int cte;

        if (pKing.getInicialLin() < 2) {
            cte = 1;
        } else {
            cte = -1;
        }

        int x = pKing.getX() + altX;   // Posicao inicial X
        int y = pKing.getY() + altX;   // Posicao inicial Y

        matWhiteCheckMate[x][y] = 1;    // Posicao do Rei

        if (altX != 0 && altY != 0) {
            matAllowPosition[x][y] = 1;
        }

        while (cont < 8) {
            num = 1;
            while (true) {

                switch (cont) {
                    case 0:
                        x++;
                        break;

                    case 1:
                        y++;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x--;
                        break;
                    case 4:
                        x++;
                        y++;
                        break;
                    case 5:
                        x--;
                        y--;
                        break;
                    case 6:
                        x++;
                        y--;
                        break;
                    case 7:
                        x--;
                        y++;
                        break;
                }
                if (x < 0 || x > 7) {        // Fora dos limites (x)
                    break;
                }
                if (y < 0 || y > 7) {        // Fora dos limites (y)
                    break;
                }

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    System.out.printf("Fora do intervalo! x = %d e y = %d e cont = %d\n", x, y, cont);
                    break;
                } else {
                    // Crio uma crescente de peças a partir do rei (numerando as pecas)
                    // Depois torno o numero (-) se for de peça !=  <-> ou (+) se for peça == ;
                    if (isPieceByKindAtPosition(pKing, x, y, cont)) {
                        num++;
                        aux = getPieceColorFactor(pKing, x, y) * num;
                        matWhiteCheckMate[x][y] = aux;
                    } else {
                        matWhiteCheckMate[x][y] = 0;
                    }
                }
            }
            x = pKing.getX() + altX;   // Posicao inicial X
            y = pKing.getY() + altY;   // Posicao inicial Y
            cont++;
        }

    }

    // Seta matriz checkmate do rei preto
    private void setMatBlackCheckMate(int altX, int altY) {
        setMatrizToZero(matBlackCheckMate);     // Reseta matriz

        Piece pKing = getPieceFromArrayList(blackPieces);  // Pega peça equivalente ao rei preto

        int cont = 0;
        int aux = 0;
        int num;

        int cte;

        if (pKing.getInicialLin() < 2) {
            cte = 1;
        } else {
            cte = -1;
        }

        int x = pKing.getX() + altX;   // Posicao inicial X
        int y = pKing.getY() + altY;   // Posicao inicial Y

        matBlackCheckMate[x][y] = 1;    // Posicao do Rei

        while (cont < 8) {
            num = 1;
            while (true) {

                switch (cont) {
                    case 0:
                        x++;
                        break;

                    case 1:
                        y++;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x--;
                        break;
                    case 4:
                        x++;
                        y++;
                        break;
                    case 5:
                        x--;
                        y--;
                        break;
                    case 6:
                        x++;
                        y--;
                        break;
                    case 7:
                        x--;
                        y++;
                        break;
                }
                if (x < 0 || x > 7) {        // Fora dos limites (x)
                    break;
                }
                if (y < 0 || y > 7) {        // Fora dos limites (y)
                    break;
                }

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    System.out.printf("Fora do intervalo! x = %d e y = %d e cont = %d\n", x, y, cont);
                } else {

                    if (isAnyPieceAtPosition(x, y)) {
//                        aux = getPieceColorFactor(pKing, x, y) * num;
                        if (matchColorByPosition(pKing, x, y)) {
                            matBlackCheckMate[x][y] = aux;
                        }
                    } else {
                        matBlackCheckMate[x][y] = 0;
                    }
                }
            }
            x = pKing.getX() + altX;   // Posicao inicial X
            y = pKing.getY() + altY;   // Posicao inicial Y
            cont++;
        }
    }

    // Retorna fator -1 (cor diferente) para  multiplicar num
    // Retorna fator 1 (mesma cor) para  multiplicar num
    // Retorna fator 0 (sem peca) , pois num * 0 = 0 -- e zero é correspondente sem peca
    private int getPieceColorFactor(Piece p, int x, int y) {
        for (Piece pNext : whitePieces) {
            if (pNext.getX() == x && pNext.getY() == y) {
                return (p.getColor().equals(pNext.getColor())) ? 1 : -1;
            }
        }

        for (Piece pNext : blackPieces) {
            if (pNext.getX() == x && pNext.getY() == y) {
                return (p.getColor().equals(pNext.getColor())) ? 1 : -1;
            }
        }

        return 0;       // Nao tem peca
    }

    // Retorna :
    // 2 - Se for de mesma cor
    // 3 - Se for de cor diferente
    // 0 - Se for vazio
    private int getMatCheckMateValue(Piece p, int x, int y) {
        for (Piece pNext : whitePieces) {
            if (pNext.getX() == x && pNext.getY() == y) {
                return (p.getColor().equals(pNext.getColor())) ? 2 : 3;
            }
        }

        for (Piece pNext : blackPieces) {
            if (pNext.getX() == x && pNext.getY() == y) {
                return (p.getColor().equals(pNext.getColor())) ? 2 : 3;
            }
        }
        return 0;
    }

    /* Retorna referencia a peça */
    public Piece getPieceReference() {
        return this.pReference;
    }

    // Seta todos os movimentos permitidos para imprimir na selecão e depois mover
    void setAllowMovement(Piece p0) {

        blockedPieces(p0);
    }

    private void blockKing(Piece pKing, int[][] mat) {
        int x;
        int y;
        int cont = 0;

        while (cont < 8) {
            x = pKing.getX();
            y = pKing.getY();
            switch (cont) {
                case 0:
                    x++;
                    break;
                case 1:
                    y++;
                    break;
                case 2:
                    x--;
                    break;
                case 3:
                    y--;
                    break;
                case 4:
                    x++;
                    y++;
                    break;
                case 5:
                    x--;
                    y--;
                    break;
                case 6:
                    x++;
                    y--;
                    break;
                case 7:
                    x--;
                    y++;
                    break;
            }
            if (x < 0 || x > 7) {
                break;
            }
            if (y < 0 || y > 7) {
                break;
            }
            cont++;
        }
    }

    // Bloqueia que qualquer peça realize movimentos que pulem uma peça do proprio time
    private void blockedPieces(Piece p) {
        int aux;
        int startX;
        int startY;
        int cte;

        // Checa os movimentos que colocam o rei em check e proibe
        if ("KING".equals(p.toString())) {
//            if(blockKing(p, matAllowPosition)) {

//            }
        }
//        System.out.printf("%s\n", p.getColor());
        if ("QUEEN".equals(p.toString()) || "ROOK".equals(p.toString()) || "PAWN".equals(p.toString())) {

            aux = 1;

            int cont = 0;
            int i = p.getX();
            int j = p.getY();

            while (cont < 4) {

                switch (cont) {
                    case 0:
                        i++;
                        break;

                    case 1:
                        j++;
                        break;
                    case 2:
                        j--;
                        break;
                    case 3:
                        i--;
                        break;
                }
                if (i < 0 || i > 7 || j < 0 || j > 7) {
                    cont++;

                    i = p.getX();
                    j = p.getY();

                    if (cont == 4) {
                        break;
                    } else {
                        aux = 1;
                        continue;
                    }
                }
                /* REPENSAR PARA PODER COMER A PRIMEIRA PEÇA DO OUTRO TIME*/
                /* FUNCIONANDO PERFEITO TIRANDO AS OPCOES DE COMER -- NAO PERDER O JA FEITO, MELHORAR */
                if (aux != 0) {                     // Se for vazio bloqueia continuar

                    if (matAllowPosition[i][j] == 0) {
                        aux = 0;                                // Vazio -bloqueia depois disso
                    } else if (matAllowPosition[i][j] == 2) {
                        matAllowPosition[i][j] = 1;
                        aux = 0;
                    } else if (matAllowPosition[i][j] == 1) {
                        aux = 1;
                    }

                } else {
                    matAllowPosition[i][j] = aux;
                }
            }
        }

        if ("QUEEN".equals(p.toString()) || "BISHOP".equals(p.toString()) || "PAWN".equals(p.toString())) {

            aux = 1;
            int cont = 0;
            int i = p.getX();
            int j = p.getY();

            while (cont < 4) {

                switch (cont) {
                    case 0:
                        i++;
                        j++;
                        break;

                    case 1:
                        i--;
                        j++;
                        break;
                    case 2:
                        i++;
                        j--;
                        break;
                    case 3:
                        i--;
                        j--;
                        break;
                }

                if (i < 0 || i > 7 || j < 0 || j > 7) {
                    cont++;
                    i = p.getX();
                    j = p.getY();

                    if (cont == 4) {
                        break;
                    } else {
                        aux = 1;
                        continue;
                    }
                }
                if (aux != 0) {                     // Se for vazio bloqueia continuar

                    if (matAllowPosition[i][j] == 0) {
                        aux = 0;                                // Vazio -bloqueia depois disso
                    } else if (matAllowPosition[i][j] == 2) {
                        if (aux != 2) {
                            matAllowPosition[i][j] = 1;        // Peca do oponente - bloqueia depois dessa peca  
                        }
                        aux = 2;
                    }

                } else {
                    matAllowPosition[i][j] = aux;
                }
            }
        }
    }

    public void verifySameTeam(Piece p) {
        if (whitePieces.contains(p)) {
            for (Piece pNext : whitePieces) {
                if (matAllowPosition[pNext.getX()][pNext.getY()] == 1) {
                    matAllowPosition[pNext.getX()][pNext.getY()] = 0;
                }
            }
        } else {
            for (Piece pNext : blackPieces) {
                if (matAllowPosition[pNext.getX()][pNext.getY()] == 1) {
                    matAllowPosition[pNext.getX()][pNext.getY()] = 0;
                }
            }
        }
    }

    public void verifyOtherTeam(Piece p) {
        if (blackPieces.contains(p)) {
            for (Piece pNext : whitePieces) {
                if (matAllowPosition[pNext.getX()][pNext.getY()] == 1) {
                    if (!"KNIGHT".equals(p.toString())) {
                        matAllowPosition[pNext.getX()][pNext.getY()] = 2;
                    }
                }
            }
        } else {
            for (Piece pNext : blackPieces) {
                if (matAllowPosition[pNext.getX()][pNext.getY()] == 1) {
                    if (!"KNIGHT".equals(p.toString())) {
                        matAllowPosition[pNext.getX()][pNext.getY()] = 2;
                    }
                }
            }
        }
    }

    // Seta na matriz as posicoes permitidas para movimentar
    public void setAllAllowPosition(Piece p) {

        // Movimento especial de roque
//        selectSpecialMovementCastling();
        // Pega os movimentos permitiods de cada peça
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matAllowPosition[i][j] = p.setValueToMatriz(i, j);          // Nesse momento estamos com uma matriz com 0 - vazio e 1 - Peca
            }
        }

        verifyOtherTeam(p);

        verifySameTeam(p);

        // Verifica se a peca esta bloqueada
        setAllowMovement(p);

        // Verifica o movimento diferenciado de comer peça do peao        
        checkNextPosition(p);
    }

    /* Verifica se o rei foi morto */
    public boolean isGameOver() {

        for (Piece p : whitePieces) {
            if ("KING".equals(p.toString())) {
                return (matAllowPosition[p.getX()][p.getY()] == 1);
            }
        }

        for (Piece p : blackPieces) {
            if ("KING".equals(p.toString())) {
                return (matAllowPosition[p.getX()][p.getY()] == 1);
            }
        }
        return false;
    }

    public String getTurn() {
        for (Piece p : whitePieces) {
            if (p.getInicialLin() > 5) {
                return "WHITE.1";
            } else {
                return "WHITE.2";
            }
        }
        return "ERRO";
    }

    public Piece getPieceWhite() {
//        if (active) {
        for (Piece p : whitePieces) {
            if ((p.getX() == getMouseX(mouseAction.last)) && (p.getY() == getMouseY(mouseAction.last))) {
                this.selectPiece = true;
                return p;
            }
        }
//        }
        return null;
    }

    public Piece getPieceBlack() {
//        if (active) {
        for (Piece p : blackPieces) {
            if ((p.getX() == getMouseX(mouseAction.last)) && (p.getY() == getMouseY(mouseAction.last))) {
                this.selectPiece = true;
                return p;
            }
        }
//        }
        return null;
    }

    public Piece getPieceWhiteToDraw() {
//        if (active) {
        for (Piece p : whitePieces) {
            if ((p.getX() == getMouseX(mouseAction.current)) && (p.getY() == getMouseY(mouseAction.current))) {
                return p;
            }
        }
//        }
        return null;
    }

    public Piece getPieceBlackToDraw() {
//        if (active) {
        for (Piece p : blackPieces) {
            if ((p.getX() == getMouseX(mouseAction.current)) && (p.getY() == getMouseY(mouseAction.current))) {
                return p;
            }
        }
//        }
        return null;
    }

    /* Verifica se o movimento está permitido na matriz */
    public boolean isAllowedToMove(Piece p) {
        return (matAllowPosition[getMouseX(mouseAction.current)][getMouseY(mouseAction.current)] == 1);
    }

    /* Retorna a partir da posicao x e y passadas se pertence ao whitePieces ou ao blackPieces */
    private ArrayList<Piece> getTypeArrayList(int x, int y) {
        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                return whitePieces;
            }
        }

        for (Piece p : blackPieces) {
            if (p.getX() == x && p.getY() == y) {
                return blackPieces;
            }
        }

        return null;
    }

    /* Retorna 0 caso as cores sejam iguais, senão retorna 1 */
    private int matchColorByPieces(Piece p, Piece pNext) {
        return (p.getColor().equals(pNext.getColor()) ? 0 : 1);
    }

    private boolean matchColorByPosition(Piece pCompare, int x, int y) {
        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                return p.getColor().equals(pCompare.getColor());
            }
        }
        for (Piece p : whitePieces) {
            if (p.getX() == x && p.getY() == y) {
                return p.getColor().equals(pCompare.getColor());
            }
        }
        return false;
    }
    /* Checa se na proxima posição tem peça do mesmo time ou de outro ( também verifica a diagonal do peão) */

    private void checkNextPosition(Piece p) {

//        if (active) {
        for (Piece pNext : whitePieces) {
            if ((pNext.getX() == getMouseX(mouseAction.current)) && (pNext.getY() == getMouseY(mouseAction.current))) {

                // Movimento diferencado de comer do peao
                if ("PAWN".equals(p.toString())) {
                    pawnMovementException(p, pNext);
                }

                // Checa se a proxima peca e de mesma cor ou nao e seta 0 ou 1
                if (p.isMovementAllow(getMouseX(mouseAction.current), getMouseY(mouseAction.current))) {
                    matAllowPosition[pNext.getX()][pNext.getY()] = matchColorByPieces(p, pNext);
                }
                break;
            }
        }

        for (Piece pNext : blackPieces) {
            if ((pNext.getX() == getMouseX(mouseAction.current)) && (pNext.getY() == getMouseY(mouseAction.current))) {

                // Movimento diferencado de comer do peao
                if ("PAWN".equals(p.toString())) {
                    pawnMovementException(p, pNext);
                }

                // Checa se a proxima peca e de mesma cor ou nao e seta 0 ou 1
                if (p.isMovementAllow(getMouseX(mouseAction.current), getMouseY(mouseAction.current))) {
                    matAllowPosition[pNext.getX()][pNext.getY()] = matchColorByPieces(p, pNext);
                }
                break;
            }
        }
//        }
    }

    private Piece removePieceNext(Piece p) {
        if (!whitePieces.contains(p)) {
            for (Piece pNext : whitePieces) {
                if (pNext.getX() == p.getX() && pNext.getY() == p.getY()) {
                    whitePieces.remove(pNext);
                    return pNext;
                }
            }
        } else {
            for (Piece pNext : blackPieces) {
                if (pNext.getX() == p.getX() && pNext.getY() == p.getY()) {
                    blackPieces.remove(pNext);
                    return pNext;
                }
            }
        }
        return null;
    }

    public boolean getKingInCheck() {
        if (this.showAlertCheck) {
            this.showAlertCheck = false;
            return (this.whiteKingInCheck || this.blackKingInCheck);
        } else {
            return false;
        }
    }

    public boolean getWhiteKingInCheck(String str) {
        return this.whiteKingInCheck;
    }

    public boolean getBlackKingInCheck(String str) {
        return this.blackKingInCheck;
    }

    public void doAllowMovement(Piece p, JDialog pawnChangeType) {
        allowMovement = isAllowedToMove(p);
        
        if (allowMovement) {

            movePiece(p);

            if (p.getColor().equals("WHITE")) {
                setMatBlackCheckMate(0, 0);     // Nao modifica pos inicial
//                imprimeMatriz(matBlackCheckMate);  // teste
                blackKingInCheck = isKingInCheck(p, matBlackCheckMate);
                blackKingInCheckMate = isKingInCheckMate(p, matBlackCheckMate);
                if (blackKingInCheck) {
                    showAlertCheck = true;
                    System.out.println("Check no Rei Preto!\n");
                }

            } else {
                setMatWhiteCheckMate(0, 0);         // nao modifica pos inicial
//                imprimeMatriz(matWhiteCheckMate);  // teste
                whiteKingInCheck = isKingInCheck(p, matWhiteCheckMate);
                whiteKingInCheckMate = isKingInCheckMate(p, matWhiteCheckMate);
                if (whiteKingInCheck) {
                    showAlertCheck = true;
                    System.out.println("Check no Rei Branco!\n");
                }

            }

//            System.out.println("Move\n");
            changeTurn = true;

            if ("PAWN".equals(p.toString())) {
                if (p.getY() == 7 || p.getY() == 0) {
                    auxiliarPromotionLin = p.getInicialLin();
                    auxiliarPromotionCol = p.getInicialCol();
                    System.out.println("Troca\n");
                    pawnChangeType.setVisible(true);
                    pawnChangeType.setEnabled(true);

                    changeTurn = false;
                }
            }
            setMatrizAllowPositionToZero();    // Limpa a selecão de movimentos permitidos
        } else {
              try {
                  throw new IlegalChessMovement();
               } catch (IlegalChessMovement ex) {
                    lChess.add(p.getColor(), turn, ex.toString());
                    Log.add("   |  " + p.getColor() + "  |    " + ex.toString() +  "     |");
            }
//            try {
////                throw new IlegalChessMovement();
//            } catch (IlegalChessMovement ex) {
//                Logger.getLogger(DeskChessFrame.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

//    public void setGraphics(Graphics2D g2) {
//        this.g2_aux = g2;
//    }
//
//    public Graphics2D getGraphics() {
//        return this.g2_aux;
//    }

    /* Seta na matriz o movimento diferenciado de comer do peão */
    private void pieceTypePawn(int x, int y) {
        int cte;

        if (pReference.getInicialLin() < 2) {
            cte = 1;
        } else {
            cte = -1;
        }

        if (blackPieces.contains(pReference)) {
            for (Piece p : whitePieces) {
                if (p.getY() == (pReference.getY() + cte)) {
                    if (p.getX() == (pReference.getX() - 1) || p.getX() == (pReference.getX() + 1)) {
                        matAllowPosition[p.getX()][p.getY()] = 1;       //Peça na diagonal
                    } else if (p.getX() == pReference.getX()) {
                        matAllowPosition[p.getX()][p.getY()] = 0;     // Peça na frente
                    } else if (p.getX() == (pReference.getX()) || p.getY() == (pReference.getY() - 2 * cte)) {
                        matAllowPosition[pReference.getX()][pReference.getY()] = 0;
                    }

                }
            }
        } else {
            for (Piece p : blackPieces) {
                if (p.getY() == (pReference.getY() + cte)) {
                    if (p.getX() == (pReference.getX() - 1) || p.getX() == (pReference.getX() + 1)) {
                        matAllowPosition[p.getX()][p.getY()] = 1;       //Peça na diagonal
                    } else if (p.getX() == pReference.getX()) {
                        matAllowPosition[p.getX()][p.getY()] = 0;     // Peça na frente
                    } else if (p.getX() == (pReference.getX()) || p.getY() == (pReference.getY() - 2 * cte)) {
                        matAllowPosition[pReference.getX()][pReference.getY()] = 0;
                    }
                }

            }
        }
    }

    // Movimento de comer do peao (diagonal)
    private void pawnMovementException(Piece p, Piece pNext) {
        int cte;

        if (p.getInicialLin() < 2) {
            cte = 1;
        } else {
            cte = -1;
        }

        if (p.getX() == pNext.getX() && (p.getY() == (pNext.getY() - cte) || p.getY() == (pNext.getY() - 2 * cte))) {
            allowMovement = false;
        } else if ((p.getX() == (pNext.getX() + 1) || p.getX() == (pNext.getX() - 1)) && p.getY() == (pNext.getY() - cte)) {
            allowMovement = true;
        }
    }

    
    
    private void movePiece(Piece p) {
        if (selectPiece) {
            if (isAllowedToMove(p)) {
                saveUndo();
                p.move(getMouseX(mouseAction.current), getMouseY(mouseAction.current));

                Piece pRemoved = removePieceNext(p);     // Se houver uma peça colidindo na nova posicao remove
                saveRedo();
//                Log.add(".   |   " + p.toString() + "  |   x:" + getMouseX(mouseAction.last) + "  y:" + getMouseY(mouseAction.last) + " |  x:" + getMouseX(mouseAction.current) + "  y:" + getMouseY(mouseAction.current) + " |");
                if (pRemoved == null) {
                    if ("PAWN".equals(p.toString())) {
                        lChess.add(p.getColor(), turn, p.getNotation());
                        Log.add("   |  " + p.getColor() + "  |      " + p.getNotation() + "       |");
                    } else {
                        lChess.add(p.getColor(), turn, p.getNotation());
                        Log.add("   |  " + p.getColor() + "  |      " + p.getNotation() + "      |");
                    }
                } else {
                    if ("PAWN".equals(p.toString()) && "PAWN".equals(pRemoved.toString())) {
                        lChess.add(p.getColor(), turn, p.getNotation() + "x" + pRemoved.getNotation());
                        Log.add("   |  " + p.getColor() + "  |     " + p.getNotation() + "x" + pRemoved.getNotation() + "     |");
                    } else if ("PAWN".equals(p.toString()) || "PAWN".equals(pRemoved.toString())) {
                        lChess.add(p.getColor(), turn, p.getNotation() + "x" + pRemoved.getNotation());
                        Log.add("   |  " + p.getColor() + "  |    " + p.getNotation() + "x" + pRemoved.getNotation() + "     |");
                    } else {
                        lChess.add(p.getColor(), turn, p.getNotation() + "x" + pRemoved.getNotation());
                        Log.add("   |  " + p.getColor() + "  |    " + p.getNotation() + "x" + pRemoved.getNotation() + "    |");
                    }
                }
                this.selectPiece = false;
                canUndo = true;
                canRedo = false;
                System.out.println(lChess.getLastIndex());
            }
        }
    }


    void imprimeMatriz(int[][] mat) {
        System.out.println("---------------------------------------------------------\n");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%d ", mat[j][i]);
            }
            System.out.println("\n");
        }

        System.out.println("---------------------------------------------------------\n");
    }

    public final ArrayList<Piece> getArrayListWhite() {
        return this.whitePieces;
    }

    public final ArrayList<Piece> getArrayListBlack() {
        return this.blackPieces;
    }

    
    
    
    /* ========== METODOS LOG ===============*/
    /* Seta o log do xadrez, com notação xadriz*/
    public void setLogTable(List list1) {
        this.list1 = list1;
        updateLogTable(list1);
    }
    /* Seta o log do xadrez, com notação xadriz*/

    public void updateLogTable(List list1) {
        list1.removeAll();
//        list1.add("|  Index  |   Color   |    Notatation  |", 0);

        for (int i = 0; i < (Log.size()); i++) {
            if (i < 10) {
                list1.add("|    " + i + Log.get(i), i);
            } else if (i < 100) {
                list1.add("|   " + i + Log.get(i), i);
            } else {
                list1.add("|  " + i + Log.get(i), i);
            }
        }
    }
   
    
    
    
    /* ========== METODOS SALVAR/LOAD e UNDO/REDO  ===============*/
    private void saveUndo() {
        System.out.printf("undo : ");
        undo.updateList(whitePieces, blackPieces);
        undo.saveList();
        undo.setTurn(turn);
        sVar.setCont(sl.getContWhite(), sl.getContBlack());
        sVar.createFile(Log, turn);
    }

    private void saveRedo() {
        System.out.printf("redo : ");
        redo.updateList(whitePieces, blackPieces);
        redo.saveList();
        redo.setTurn(turn);
        sVar.setCont(sl.getContWhite(), sl.getContBlack());
        sVar.createFile(Log, turn);
    }

    // Click no botao undo ou na opçao de menu undo
    public void clickLoadUndo() {
        if (canUndo) {
            loadUndo();
            int aux = Log.size();
            if (aux > 0) {
                removeLineLog = Log.get(aux - 1);
                Log.remove(aux - 1);
                updateLogTable(list1);
            }
            canUndo = false;
            canRedo = true;
        }
    }

    // Click no botao redo ou na opçao de menu redo
    public void clickLoadRedo() {
        if (canRedo) {
            loadRedo();
            Log.add(removeLineLog);
            updateLogTable(list1);
            canRedo = false;
            canUndo = true;
        }
    }

    // Funcao loadUndo desfaz a jogada atual
    private void loadUndo() {
        System.out.printf("undo : ");
        setMatrizAllowPositionToZero();
        turn = undo.getTurn();
        undo.updateList(whitePieces, blackPieces);
        undo.loadList(sl.getContWhite(), sl.getContBlack());
    }

    // Funcao loadRedo -- Recupera o jogada desfeita
    private void loadRedo() {
        System.out.printf("redo : ");
        whitePieces.clear();
        blackPieces.clear();
        setMatrizAllowPositionToZero();
        redo.updateList(whitePieces, blackPieces);
        turn = redo.getTurn();
        redo.loadList(sl.getContWhite(), sl.getContBlack());
    }

    // Funcao que salva o jogo ( E permite que carregue a partir da Serialização
    public void saveGame() {
        System.out.printf("sl : ");
        sl.updateList(whitePieces, blackPieces);
        sl.saveList();
        sVar.setCont(sl.getContWhite(), sl.getContBlack());
        sVar.createFile(Log, turn);
    }

    // Funcao que salva o jogo (Carrega Jogo)
    public void loadGame() {
        System.out.printf("sl : ");
        setMatrizAllowPositionToZero();
        Log.removeAll(Log);

        sVar.readFile(Log, turn);

        if (sVar.getContWhite() == 0 || sVar.getContBlack() == 0) {
            System.err.println("Erro não encontrou nenhuma peça --fixed by override the load file");
            saveGame();   // Salva o jogo atual para corrigir não ter lido o load com sucesso
        } else {
            sl.loadList(sVar.getContWhite(), sVar.getContBlack());
            updateLogTable(list1);
            setMatrizAllowPositionToZero();
        }
    }

    
    
    
    /* ========== METODOS PARA PEGAM CLICK DO MOUSE ANTERIOR E ATUAL ===============*/
    /* Seta o valor atual do mouse */
    public void setMousePosition(Point mPoint) {
        mCurrent[0] = mPoint.x;
        mCurrent[1] = mPoint.y;

//        System.out.printf("atual %d %d\n", mCurrent[0], mCurrent[1]);
    }

    /* Seta o valor anterior do mouse */
    public void setLastMousePosition(Point mPoint) {
        mLast[0] = mPoint.x;
        mLast[1] = mPoint.y;

//        System.out.printf("last %d %d\n", mLast[0], mLast[1]);
    }

    /* Retorna o X do mouse ( anterior ou atual - dependendo do enum que receber) */
    public int getMouseX(mouseAction act) {
        return (act == mouseAction.current) ? mCurrent[0] : mLast[0];
    }

    /* Retorna o Y do mouse ( anterior ou atual - dependendo do enum que receber) */
    public int getMouseY(mouseAction act) {
        return (act == mouseAction.current) ? mCurrent[1] : mLast[1];
    }

    
    
    
    /* ========== METODOS PARA DESENHAR ===============*/
    /* Metodo de desenhar todas as peças dos ArrayLists whitePieces e blackPieces*/
    public void draw(Graphics2D g2) {

        //Percorre o vetor de peças brancas   
        for (Piece p : whitePieces) {
            p.draw(g2);
        }

        //Percorre o vetor de peças pretas
        for (Piece p : blackPieces) {
            p.draw(g2);
        }
    }
    /* Metodo de desenhar um quadrado */

    private void drawSquare(Graphics2D g2, Color clr, int x, int y, int border) {
        int width = 540;            // Tamanho de um dos lados do tabuleiro  
        int square = width / 8;     // Tamanho do lado de um dos quadrados do quadrante         

        g2.setColor(clr);

        g2.setStroke(new BasicStroke((float) border));           // Seta a borda (espessura)
        g2.drawRect(x * square + border / 2, y * square + border / 2, square - border, square - border);
    }

    /* Metodo de desenhar um Circulo */
    private void drawCircle(Graphics2D g2, Color clr, int x, int y, int border) {
        int width = 540;            // Tamanho de um dos lados do tabuleiro  
        int square = width / 8;     // Tamanho do lado de um dos quadrados do quadrante         
        int R = 50;
        g2.setColor(clr);

        g2.fillOval((x * square + R / 2), (y * square + R / 2), square - R, square - R);

    }

    /* Desenha o quadrado de seleção de casa */
    public void drawSelection(Graphics2D g2) {
        drawSquare(g2, Color.RED, getMouseX(mouseAction.current), getMouseY(mouseAction.current), 2);
    }

    /* Desenha o quadrado dos movimentos possiveis */
    public void drawAllowMovement(Graphics2D g2) {
        if (pReference != null) {
            if ("PAWN".equals(pReference.toString())) {
                pieceTypePawn(pReference.getX(), pReference.getY());
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matAllowPosition[i][j] == 1) {
//                    drawSquare(g2, Color.ORANGE, i, j, 5);
                    drawCircle(g2, Color.ORANGE, i, j, 5);
                }
            }
        }
    }

    
    
    
    /* ========== METODOS PARA CHECK E CHECK MATE ===============*/
    /* Checa se o rei esta em check */
    private boolean isKingInCheck(Piece p, int[][] mat) {
        boolean allow = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mat[i][j] == 1) {    // posicao do rei do oponente
                    if (matAllowPosition[i][j] == 1) {  // movimento permitido
                        allow = true;
                    }
                }
            }
        }
        return (allow && mat[p.getX()][p.getY()] == -2);
    }

    /* Checa se o rei esta em check mate */
    private boolean isKingInCheckMate(Piece p, int[][] mat) {
        return false;
    }
    
    public void clearLog() {
        System.out.println("===BEFORE CLEAR===");
        lChess.printAll();
        lChess.clear();
        System.out.println("===AFTER CLEAR===");
        lChess.printAll();
    }
    
    public void remp
    /* Retorna o Singleton desta classe */
    public static Chess getChess() {

        // Verifica se o chessSingleton for null para a execução, lançando excessão, senão retorna chessSingleton
        if (chessSingleton != null) {
            return chessSingleton;
        } else {
            System.err.println("Erro >> Classe chessSingleton não foi inicializada corretamente (null)\n");
            throw new NullPointerException();
        }
    }
}
