/**
 *
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package view;

import deskchess.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import timer.CounterTotal;
import timer.CounterTurn;

public class DeskChessFrame extends javax.swing.JFrame implements MouseListener, MouseMotionListener {
    private final Chess c = Chess.getChess();             // Pega a classe Chess (Singleton)

    private final CounterTotal cTotal;
    private final CounterTurn cTurn;
    private Point mousePosition = null;     // Armazena o click do mouse    
    private int playerTurn;                 // Alerta de playerTurn (Label do design)
    private final int maxRoundMinutes = 20;         // tempo maximo de um player jogar em minutos
    private boolean resetTimer;
    private boolean gameStarted;
    private boolean firstMove;
    private final boolean devMode = false;
    private Board board;
    private Chess chess;
    private boolean getNewPiece;
    private final float waitAutoSave;

    public DeskChessFrame(Chess chess) {
        initComponents();
        initIconImages();
        this.waitAutoSave = 1.0f;       // Default
        this.cTurn = new CounterTurn(showTimerTurn);
        this.cTotal = new CounterTotal(showTimerTotal);
        init(chess);
    }

    private void init(Chess chess) {
        this.playerTurn = 0;
        this.resetTimer = true;
        this.chess = chess;
        this.mousePosition = new Point(-1, -1);

        c.setLogTable(list1);

        // Alerta de alert check
        alertCheck.setSize(330, 190);
        alertCheck.setLocation(540 / 2, 540 / 2);
        alertCheck.setVisible(false);
        alertCheck.setEnabled(false);

        // Alerta de alert check mate
        alertCheckMate.setSize(330, 190);
        alertCheckMate.setLocation(540 / 2, 540 / 2);
        alertCheckMate.setVisible(false);
        alertCheckMate.setEnabled(false);

        // Alert de promoção
//        pawnPromotion.setEnabled(false);
//        pawnPromotion.setVisible(false);
//        pawnPromotion.setEnabled(false);

        continueQuit.setLocation(540 / 2, 540 / 2);
//        continueQuit.setSize(290, 151);
        continueQuit.setSize(new Dimension(290, 151));

        pawnPromotion.setSize(500, 500);
        // Posicao da mensagem de turno (player 1 turn ou player 2 turn)

        pawnPromotion.setLocation(540 / 2, 540 / 2);

        Dimension area = new Dimension(540, 540);
        this.board = new Board();      // Classe do tabuleiro
        board.setVisible(true);
        board.setPreferredSize(area);//set dimensao do painel de desenho
        board.setBackground(Color.WHITE);//set cor de fundo        
        board.addMouseListener(this);//Adiciona evento de mouse ao Painel
        board.addMouseMotionListener(this);//Adiciona evento de mouse ao Painel
        this.boardPanel.setLayout(new GridLayout(1, 1));
        this.boardPanel.add(board);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int width = boardPanel.getWidth() / 8;   // largura de cada quadrado
        Piece p0 = null;        // Peça p0 antecede o movimento (mostra as possibilidades dessa peça)
        Piece p = null;         // Peça p será movimentada - no lugares permitidos por p0

        if (firstMove) {    // Se for primeira movimentação
            mousePosition.setLocation((e.getX() / width), (e.getY() / width)); // Pega posicao atual como ultima posicao// Pega posicao atual como ultima posicao
            c.setLastMousePosition(mousePosition);
        } else {        // Senao
            c.setLastMousePosition(mousePosition); // Pega ultima posicao
            mousePosition.setLocation((e.getX() / width), (e.getY() / width));
        }

        c.setMousePosition(mousePosition);  // Pega posicao atual

        // Pega peças (p e p0)
        switch (c.getTurn()) {
            case "WHITE.1":
                if (c.turn == 1) {
                    p0 = c.getPieceWhiteToDraw();
                    p = c.getPieceWhite();
                } else {
                    p0 = c.getPieceBlackToDraw();
                    p = c.getPieceBlack();
                }
                break;
            case "WHITE.2":
                if (c.turn == 1) {
                    p0 = c.getPieceBlackToDraw();
                    p = c.getPieceBlack();
                } else {
                    p0 = c.getPieceWhiteToDraw();
                    p = c.getPieceWhite();
                }
                break;
        }

        // Desenha os possiveis locais para realizar a movimentação da peça
        if (p0 != null) {
            c.setAllAllowPosition(p0);
            c.setPieceReference(p0);
        } 

        // Movimenta a peça se for permitido e passa o turno
        if (p != null) {
//            c.selectSpecialMovementRoque();

            c.doAllowMovement(p, pawnPromotion);
        } 
    }
    
    private void initIconImages() {
        try {
            Icon i = new javax.swing.ImageIcon("src/img/loadIcon.png");
            Load.setIcon(i); // NOI18N  
        } catch(Exception IOException) {
            Load.setText("Load");
        }
        
        try {
            Icon i = new javax.swing.ImageIcon("src/img/saveIcon.png");
            Save.setIcon(i); // NOI18N  
        } catch(Exception IOException) {
            Save.setText("Save");
        }
        
         try {
            Icon i = new javax.swing.ImageIcon("src/img/loadIcon.png");
            Load.setIcon(i); // NOI18N  
        } catch(Exception IOException) {
            Load.setText("Load");
        }
        
        try {
            Icon i = new javax.swing.ImageIcon("src/img/undoIcon.png");
            Undo.setIcon(i); // NOI18N
        } catch (Exception IException) {
            Undo.setText("Undo");
        }
        try {
            Icon i = new javax.swing.ImageIcon("src/img/redoIcon.png");
            Redo.setIcon(i); // NOI18N
        } catch (Exception IException) {
            Redo.setText("Redo");
        }
        
         try {
            Icon i = new javax.swing.ImageIcon("src/img/exitIcon.png");
            Quit.setIcon(i); // NOI18N
        } catch (Exception IException) {
            Quit.setText("Quit");
        }
        try {
            okAbandon.setIcon(new javax.swing.ImageIcon("src/img/IconConfirm.png")); // NOI18N
        } catch(Exception IOException) {
            okAbandon.setText("Ok");
        }
        
        try {
            cancelReturn.setIcon(new javax.swing.ImageIcon("src/img/IconCancel.png")); // NOI18N
        } catch(Exception IOException) {
            cancelReturn.setText("Return");
        }
        
    }
        
    private void changeTurn() {
        if (c.allowMovement) {
//            save.saveEverthing();
//            save.saveArrayListToFile();
            cTurn.setResetClock();
            c.enPassant = false;

            if (c.turn == 1) {
                c.turn++;
            } else if (c.turn == 2) {
                c.turn--;
            }

            player.setText("Player " + c.turn + " Turn !");
            playerClone.setText("Player " + c.turn + " - Your King is in CHECK !");
            c.allowMovement = false;
        }
    }

    private void showAlertCheck() {
        if (c.getKingInCheck()) {
            alertCheck.setVisible(true);
            alertCheck.setEnabled(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        changeTurn();

        showAlertCheck();

        //Log da movimentaçao
        c.setLogTable(list1);

        // Alerta de check mate ( game over )
        if (c.isGameOver()) {
            System.out.println("\n-- GAME OVER -- \n");
            alertCheckMate.setLocation(540 / 2, 540 / 2);
            alertCheckMate.setVisible(true);
        }

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaint();
    }

    private void sair() {
        System.out.println("EXIT !\n");
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        continueQuit = new javax.swing.JDialog();
        okAbandon = new javax.swing.JButton();
        cancelReturn = new javax.swing.JButton();
        label_confirmToQuit = new javax.swing.JLabel();
        pawnPromotion = new javax.swing.JDialog();
        BishopType = new javax.swing.JButton();
        rookType = new javax.swing.JButton();
        queenType = new javax.swing.JButton();
        knightType = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        okPromote = new javax.swing.JButton();
        DetailsMatch = new javax.swing.JDialog();
        alertCheckMate = new javax.swing.JDialog();
        label1 = new java.awt.Label();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        alertCheck = new javax.swing.JDialog();
        playerClone = new javax.swing.JLabel();
        Alert = new javax.swing.JLabel();
        okCheck = new javax.swing.JButton();
        Preferences = new javax.swing.JDialog();
        boardPanel = new javax.swing.JPanel();
        NomeDoJogo = new javax.swing.JLabel();
        player = new javax.swing.JLabel();
        Quit = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        showTimerTurn = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        nomeAluno = new javax.swing.JLabel();
        list1 = new java.awt.List();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        showTimerTotal = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        Load = new javax.swing.JButton();
        Save = new javax.swing.JButton();
        Redo = new javax.swing.JButton();
        Undo = new javax.swing.JButton();
        nomeProf1 = new javax.swing.JLabel();
        Total = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        Timer = new javax.swing.JLabel();
        Index = new javax.swing.JLabel();
        Turn = new javax.swing.JLabel();
        Index1 = new javax.swing.JLabel();
        Index2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        preferMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        continueQuit.setAlwaysOnTop(true);
        continueQuit.setMinimumSize(new java.awt.Dimension(100, 100));
        continueQuit.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        okAbandon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        okAbandon.setFocusable(false);
        okAbandon.setRequestFocusEnabled(false);
        okAbandon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                okAbandonMousePressed(evt);
            }
        });
        okAbandon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okAbandonActionPerformed(evt);
            }
        });
        continueQuit.getContentPane().add(okAbandon, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 110, 90));

        cancelReturn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cancelReturn.setFocusable(false);
        cancelReturn.setRequestFocusEnabled(false);
        cancelReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cancelReturnMousePressed(evt);
            }
        });
        cancelReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelReturnActionPerformed(evt);
            }
        });
        continueQuit.getContentPane().add(cancelReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 110, 90));

        label_confirmToQuit.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        label_confirmToQuit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_confirmToQuit.setText("Do you really want to quit ?");
        label_confirmToQuit.setAlignmentY(0.0F);
        label_confirmToQuit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        continueQuit.getContentPane().add(label_confirmToQuit, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 40));

        BishopType.setText("Bishop");
        BishopType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BishopTypeMousePressed(evt);
            }
        });

        rookType.setText("Rook");
        rookType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rookTypeMousePressed(evt);
            }
        });

        queenType.setText("Queen");
        queenType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                queenTypeMousePressed(evt);
            }
        });

        knightType.setText("Knight");
        knightType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                knightTypeMousePressed(evt);
            }
        });

        jLabel2.setText("Choose the piece that you pawn will become :");

        okPromote.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        okPromote.setSelected(true);
        okPromote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                okPromoteMousePressed(evt);
            }
        });
        okPromote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okPromoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pawnPromotionLayout = new javax.swing.GroupLayout(pawnPromotion.getContentPane());
        pawnPromotion.getContentPane().setLayout(pawnPromotionLayout);
        pawnPromotionLayout.setHorizontalGroup(
            pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pawnPromotionLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(queenType, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(knightType, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rookType, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BishopType, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 31, Short.MAX_VALUE))
            .addGroup(pawnPromotionLayout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(okPromote, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pawnPromotionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(76, 76, 76))
        );
        pawnPromotionLayout.setVerticalGroup(
            pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pawnPromotionLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(queenType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BishopType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pawnPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(knightType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rookType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(okPromote)
                .addContainerGap())
        );

        javax.swing.GroupLayout DetailsMatchLayout = new javax.swing.GroupLayout(DetailsMatch.getContentPane());
        DetailsMatch.getContentPane().setLayout(DetailsMatchLayout);
        DetailsMatchLayout.setHorizontalGroup(
            DetailsMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        DetailsMatchLayout.setVerticalGroup(
            DetailsMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        alertCheckMate.setModal(true);
        alertCheckMate.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setText("label1");
        alertCheckMate.getContentPane().add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-32604, -32684, -1, -1));

        jLabel12.setFont(new java.awt.Font("Ubuntu Mono", 3, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Winner : Player 1");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        alertCheckMate.getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 330, 40));

        jLabel13.setFont(new java.awt.Font("Ubuntu Mono", 3, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Check Mate !");
        jLabel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        alertCheckMate.getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 50));

        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        alertCheckMate.getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 140, 80));

        alertCheck.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        playerClone.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        playerClone.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        playerClone.setText("Check in player X");
        playerClone.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        alertCheck.getContentPane().add(playerClone, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 240, 50));

        Alert.setFont(new java.awt.Font("Ubuntu Mono", 3, 24)); // NOI18N
        Alert.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Alert.setText("Check !");
        Alert.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Alert.setMaximumSize(new java.awt.Dimension(400, 400));
        Alert.setMinimumSize(new java.awt.Dimension(100, 150));
        Alert.setPreferredSize(new java.awt.Dimension(100, 150));
        alertCheck.getContentPane().add(Alert, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 52));

        okCheck.setFocusPainted(false);
        okCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okCheckActionPerformed(evt);
            }
        });
        alertCheck.getContentPane().add(okCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 127, -1));

        javax.swing.GroupLayout PreferencesLayout = new javax.swing.GroupLayout(Preferences.getContentPane());
        Preferences.getContentPane().setLayout(PreferencesLayout);
        PreferencesLayout.setHorizontalGroup(
            PreferencesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PreferencesLayout.setVerticalGroup(
            PreferencesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chess Game v1.0");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(850, 650));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(816, 615));
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        boardPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        boardPanel.setAlignmentX(1.0F);
        boardPanel.setAlignmentY(1.0F);
        boardPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        boardPanel.setInheritsPopupMenu(true);
        boardPanel.setMaximumSize(new java.awt.Dimension(545, 545));
        boardPanel.setMinimumSize(new java.awt.Dimension(535, 535));
        boardPanel.setPreferredSize(new java.awt.Dimension(540, 540));

        javax.swing.GroupLayout boardPanelLayout = new javax.swing.GroupLayout(boardPanel);
        boardPanel.setLayout(boardPanelLayout);
        boardPanelLayout.setHorizontalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 536, Short.MAX_VALUE)
        );
        boardPanelLayout.setVerticalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 536, Short.MAX_VALUE)
        );

        getContentPane().add(boardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        boardPanel.getAccessibleContext().setAccessibleName("");
        boardPanel.getAccessibleContext().setAccessibleDescription("");

        NomeDoJogo.setFont(new java.awt.Font("Ubuntu Mono", 3, 24)); // NOI18N
        NomeDoJogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NomeDoJogo.setText("DeskChess v1.0");
        NomeDoJogo.setOpaque(true);
        getContentPane().add(NomeDoJogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 220, 30));

        player.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        player.setForeground(new java.awt.Color(20, 2, 106));
        player.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player.setText("PLAYER 1 TURN");
        player.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(player, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 170, 220, 20));

        Quit.setFont(new java.awt.Font("Arial Black", 3, 24)); // NOI18N
        Quit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Quit.setFocusPainted(false);
        Quit.setFocusable(false);
        Quit.setOpaque(true);
        Quit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                QuitMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                QuitMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QuitMouseClicked(evt);
            }
        });
        Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitActionPerformed(evt);
            }
        });
        getContentPane().add(Quit, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 520, 220, 40));
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(838, 12, -1, 31));

        showTimerTurn.setFont(new java.awt.Font("Ubuntu Mono", 2, 18)); // NOI18N
        showTimerTurn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showTimerTurn.setText("00:00:00");
        showTimerTurn.setToolTipText("");
        showTimerTurn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(showTimerTurn, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 120, 140, 20));
        getContentPane().add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(849, 482, -1, -1));
        getContentPane().add(filler2, new org.netbeans.lib.awtextra.AbsoluteConstraints(849, 593, -1, -1));
        getContentPane().add(filler3, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 638, -1, -1));

        nomeAluno.setFont(new java.awt.Font("Ubuntu Mono", 2, 12)); // NOI18N
        nomeAluno.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nomeAluno.setText("Aluno : Lucas V C Nicolau");
        getContentPane().add(nomeAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 180, 10));

        list1.setBackground(new java.awt.Color(178, 182, 212));
        list1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        list1.setFont(new java.awt.Font("Ubuntu Mono", 0, 12)); // NOI18N
        list1.setForeground(new java.awt.Color(1, 1, 1));
        list1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                list1MouseExited(evt);
            }
        });
        list1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                list1FocusLost(evt);
            }
        });
        getContentPane().add(list1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 220, 221, 200));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Player 1 Side");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 540, -1));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Player 2 Side");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 0, 540, 20));

        showTimerTotal.setFont(new java.awt.Font("Ubuntu Mono", 2, 18)); // NOI18N
        showTimerTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showTimerTotal.setText("00:00:00");
        showTimerTotal.setToolTipText("");
        showTimerTotal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(showTimerTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 140, 140, 20));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 180, 10));

        Load.setFont(new java.awt.Font("Arial Black", 3, 16)); // NOI18N
        Load.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Load.setFocusPainted(false);
        Load.setFocusable(false);
        Load.setOpaque(true);
        Load.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoadMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LoadMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LoadMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                LoadMouseReleased(evt);
            }
        });
        Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadActionPerformed(evt);
            }
        });
        getContentPane().add(Load, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 470, 100, 40));

        Save.setFont(new java.awt.Font("Arial Black", 3, 16)); // NOI18N
        Save.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Save.setFocusPainted(false);
        Save.setFocusable(false);
        Save.setOpaque(true);
        Save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SaveMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SaveMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                SaveMouseReleased(evt);
            }
        });
        getContentPane().add(Save, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 470, 100, 40));

        Redo.setFont(new java.awt.Font("Arial Black", 0, 8)); // NOI18N
        Redo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Redo.setFocusPainted(false);
        Redo.setFocusable(false);
        Redo.setOpaque(true);
        Redo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RedoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                RedoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                RedoMouseReleased(evt);
            }
        });
        getContentPane().add(Redo, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, 100, 30));

        Undo.setFont(new java.awt.Font("Arial Black", 0, 8)); // NOI18N
        Undo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Undo.setFocusPainted(false);
        Undo.setFocusable(false);
        Undo.setOpaque(true);
        Undo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                UndoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                UndoMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UndoMouseClicked(evt);
            }
        });
        Undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoActionPerformed(evt);
            }
        });
        getContentPane().add(Undo, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 430, 100, 30));

        nomeProf1.setFont(new java.awt.Font("Ubuntu Mono", 2, 12)); // NOI18N
        nomeProf1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nomeProf1.setText("Professor :  João Batista");
        getContentPane().add(nomeProf1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 190, 10));

        Total.setFont(new java.awt.Font("Ubuntu Mono", 3, 12)); // NOI18N
        Total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Total.setText("Total");
        Total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(Total, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 80, 20));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 180, 10));

        Timer.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        Timer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Timer.setText("Timer");
        getContentPane().add(Timer, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, 220, 20));

        Index.setFont(new java.awt.Font("Ubuntu Mono", 3, 12)); // NOI18N
        Index.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Index.setText("Notation");
        Index.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(Index, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, 100, 20));

        Turn.setFont(new java.awt.Font("Ubuntu Mono", 3, 12)); // NOI18N
        Turn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Turn.setText("Turn");
        Turn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(Turn, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 120, 80, 20));

        Index1.setFont(new java.awt.Font("Ubuntu Mono", 3, 12)); // NOI18N
        Index1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Index1.setText("Index");
        Index1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(Index1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 200, 60, 20));

        Index2.setFont(new java.awt.Font("Ubuntu Mono", 3, 12)); // NOI18N
        Index2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Index2.setText("Color");
        Index2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        getContentPane().add(Index2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 200, 60, 20));

        jMenuBar1.setBackground(new java.awt.Color(7, 1, 41));
        jMenuBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuBar1.setForeground(new java.awt.Color(254, 254, 254));
        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setBackground(new java.awt.Color(254, 254, 254));
        jMenu1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("File");
        jMenu1.setAlignmentX(2.0F);
        jMenu1.setContentAreaFilled(false);
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu1.setFocusable(false);
        jMenu1.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenu1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenu1StateChanged(evt);
            }
        });
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenu1MouseReleased(evt);
            }
        });

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        saveMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        saveMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        saveMenuItem.setText("Save");
        saveMenuItem.setActionCommand("");
        saveMenuItem.setAutoscrolls(true);
        saveMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        saveMenuItem.setBorderPainted(true);
        saveMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        saveMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                saveMenuItemMouseReleased(evt);
            }
        });
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);
        saveMenuItem.getAccessibleContext().setAccessibleName("");
        saveMenuItem.getAccessibleContext().setAccessibleDescription("");

        loadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        loadMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        loadMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        loadMenuItem.setText("Load");
        loadMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        loadMenuItem.setBorderPainted(true);
        loadMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        loadMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loadMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                loadMenuItemMouseReleased(evt);
            }
        });
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(loadMenuItem);
        loadMenuItem.getAccessibleContext().setAccessibleName("");
        loadMenuItem.getAccessibleContext().setAccessibleDescription("");

        preferMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        preferMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        preferMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        preferMenuItem.setText("Preferences");
        preferMenuItem.setActionCommand("");
        preferMenuItem.setAutoscrolls(true);
        preferMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        preferMenuItem.setBorderPainted(true);
        preferMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        preferMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                preferMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                preferMenuItemMouseReleased(evt);
            }
        });
        preferMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(preferMenuItem);
        preferMenuItem.getAccessibleContext().setAccessibleName("");
        preferMenuItem.getAccessibleContext().setAccessibleDescription("");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        exitMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        exitMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        exitMenuItem.setText("Exit");
        exitMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        exitMenuItem.setBorderPainted(true);
        exitMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exitMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                exitMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        exitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                exitMenuItemMouseReleased(evt);
            }
        });
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);
        exitMenuItem.getAccessibleContext().setAccessibleName("");
        exitMenuItem.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(254, 254, 254));
        jMenu2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Edit");
        jMenu2.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenu2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenu2StateChanged(evt);
            }
        });

        undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        undoMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        undoMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        undoMenuItem.setText("Undo");
        undoMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        undoMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        undoMenuItem.setOpaque(true);
        undoMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                undoMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                undoMenuItemMouseReleased(evt);
            }
        });
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(undoMenuItem);
        undoMenuItem.getAccessibleContext().setAccessibleName("");
        undoMenuItem.getAccessibleContext().setAccessibleDescription("");

        redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenuItem.setBackground(new java.awt.Color(254, 254, 254));
        redoMenuItem.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        redoMenuItem.setForeground(new java.awt.Color(1, 1, 1));
        redoMenuItem.setText("Redo");
        redoMenuItem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        redoMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        redoMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                redoMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                redoMenuItemMouseReleased(evt);
            }
        });
        redoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(redoMenuItem);
        redoMenuItem.getAccessibleContext().setAccessibleName("");
        redoMenuItem.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.add(jMenu2);

        jMenu4.setBackground(new java.awt.Color(254, 254, 254));
        jMenu4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Match");
        jMenu4.setToolTipText("");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu4.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenu4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenu4StateChanged(evt);
            }
        });

        jMenuItem4.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem4.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem4.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem4.setText("Details");
        jMenuItem4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem4MouseReleased(evt);
            }
        });
        jMenu4.add(jMenuItem4);
        jMenuItem4.getAccessibleContext().setAccessibleName("");
        jMenuItem4.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.add(jMenu4);

        jMenu3.setBackground(new java.awt.Color(254, 254, 254));
        jMenu3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("Profile");
        jMenu3.setAutoscrolls(true);
        jMenu3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu3.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenu3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenu3StateChanged(evt);
            }
        });
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu3MousePressed(evt);
            }
        });

        jMenuItem2.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem2.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem2.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem2.setText("View");
        jMenuItem2.setAutoscrolls(true);
        jMenuItem2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);
        jMenuItem2.getAccessibleContext().setAccessibleName("");
        jMenuItem2.getAccessibleContext().setAccessibleDescription("");

        jMenuItem3.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem3.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem3.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem3.setText("Logout");
        jMenuItem3.setAutoscrolls(true);
        jMenuItem3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseReleased(evt);
            }
        });
        jMenu3.add(jMenuItem3);
        jMenuItem3.getAccessibleContext().setAccessibleName("");
        jMenuItem3.getAccessibleContext().setAccessibleDescription("");

        jMenuItem5.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem5.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem5.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem5.setText("Edit");
        jMenuItem5.setAutoscrolls(true);
        jMenuItem5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItem5MouseReleased(evt);
            }
        });
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);
        jMenuItem5.getAccessibleContext().setAccessibleName("");
        jMenuItem5.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.add(jMenu3);

        jMenu5.setBackground(new java.awt.Color(254, 254, 254));
        jMenu5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu5.setForeground(new java.awt.Color(255, 255, 255));
        jMenu5.setText("Help");
        jMenu5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenu5.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenu5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jMenu5StateChanged(evt);
            }
        });
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu5MousePressed(evt);
            }
        });

        jMenuItem9.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem9.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem9.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem9.setText("Report Issue");
        jMenuItem9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem9MousePressed(evt);
            }
        });
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);
        jMenuItem9.getAccessibleContext().setAccessibleName("");
        jMenuItem9.getAccessibleContext().setAccessibleDescription("");

        jMenuItem6.setBackground(new java.awt.Color(254, 254, 254));
        jMenuItem6.setFont(new java.awt.Font("Ubuntu Mono", 1, 14)); // NOI18N
        jMenuItem6.setForeground(new java.awt.Color(1, 1, 1));
        jMenuItem6.setText("About");
        jMenuItem6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuItem6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);
        jMenuItem6.getAccessibleContext().setAccessibleName("");
        jMenuItem6.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.sair();
    }//GEN-LAST:event_formWindowClosing

    private void QuitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuitMouseClicked

    }//GEN-LAST:event_QuitMouseClicked

    private void QuitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuitMouseReleased
        repaint();
    }//GEN-LAST:event_QuitMouseReleased

    private void okAbandonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okAbandonMousePressed
        System.out.println("Quit com sucesso\n");
        sair();
    }//GEN-LAST:event_okAbandonMousePressed

    private void cancelReturnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelReturnMousePressed
        continueQuit.setVisible(false);
        continueQuit.setEnabled(false);
    }//GEN-LAST:event_cancelReturnMousePressed

    private void queenTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_queenTypeMousePressed
        c.selectSpecialTrade("QUEEN");
    }//GEN-LAST:event_queenTypeMousePressed

    private void BishopTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BishopTypeMousePressed
        c.selectSpecialTrade("BISHOP");
    }//GEN-LAST:event_BishopTypeMousePressed

    private void rookTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rookTypeMousePressed
        c.selectSpecialTrade("ROOK");
    }//GEN-LAST:event_rookTypeMousePressed

    private void knightTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_knightTypeMousePressed
        c.selectSpecialTrade("KNIGHT");
    }//GEN-LAST:event_knightTypeMousePressed

    private void okPromoteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okPromoteMousePressed
        pawnPromotion.setVisible(false);
        pawnPromotion.setEnabled(false);

        try {
            // Promove a peca peao caso ela atravesse todo o tabuleiro
            c.doPromotion();
        } catch (IOException ex) {
            Logger.getLogger(DeskChessFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Promotion\n");
        repaint();

    }//GEN-LAST:event_okPromoteMousePressed

    private void okAbandonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okAbandonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okAbandonActionPerformed

    private void cancelReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelReturnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelReturnActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void okPromoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okPromoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_okPromoteActionPerformed

    private void QuitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuitMousePressed
        confirmExit();
    }//GEN-LAST:event_QuitMousePressed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formMousePressed

    // Detalhes da partida atual
    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MousePressed
//        DetailsMatch.setEnabled(true);
//        DetailsMatch.setVisible(true);
        repaint();
    }//GEN-LAST:event_jMenuItem4MousePressed

    private void jMenu1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseReleased

    }//GEN-LAST:event_jMenu1MouseReleased

    private void jMenu1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MousePressed
        repaint();
    }//GEN-LAST:event_jMenu1MousePressed

    private void jMenu3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MousePressed
        repaint();
    }//GEN-LAST:event_jMenu3MousePressed

    private void jMenu5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MousePressed
        repaint();
    }//GEN-LAST:event_jMenu5MousePressed

    private void okCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okCheckActionPerformed
        alertCheck.setEnabled(false);
        alertCheck.setVisible(false);
    }//GEN-LAST:event_okCheckActionPerformed

    private void LoadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoadMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LoadMouseClicked

    private void LoadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoadMousePressed
        c.loadGame();
        repaint();
    }//GEN-LAST:event_LoadMousePressed

    private void LoadMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoadMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_LoadMouseReleased

    private void SaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_SaveMouseClicked

    private void SaveMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveMousePressed
        c.saveGame();
    }//GEN-LAST:event_SaveMousePressed

    private void SaveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_SaveMouseReleased

    private void LoadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoadMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_LoadMouseEntered

    private void LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LoadActionPerformed

    private void list1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_list1FocusLost

    }//GEN-LAST:event_list1FocusLost

    private void list1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list1MouseExited
        list1.select(-1);
//        list1.
    }//GEN-LAST:event_list1MouseExited

    private void RedoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_RedoMouseClicked

    private void RedoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedoMousePressed
        c.clickLoadRedo();
        repaint();
    }//GEN-LAST:event_RedoMousePressed

    private void RedoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedoMouseReleased
        list1.repaint();
    }//GEN-LAST:event_RedoMouseReleased

    private void UndoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UndoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_UndoMouseClicked

    private void UndoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UndoMousePressed
        c.clickLoadUndo();
        repaint();
    }//GEN-LAST:event_UndoMousePressed

    private void UndoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UndoMouseReleased
        list1.repaint();
    }//GEN-LAST:event_UndoMouseReleased

    private void loadMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadMenuItemMousePressed
        c.loadGame();
        c.clearLog();
    }//GEN-LAST:event_loadMenuItemMousePressed

    private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoMenuItemActionPerformed
        c.clickLoadRedo();
    }//GEN-LAST:event_redoMenuItemActionPerformed

    private void redoMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoMenuItemMousePressed
        c.clickLoadRedo();
        c.clearLog();
        repaint();
    }//GEN-LAST:event_redoMenuItemMousePressed

    private void undoMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoMenuItemMousePressed
        c.clickLoadUndo();
        c.clearLog();
        repaint();
    }//GEN-LAST:event_undoMenuItemMousePressed

    private void saveMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMenuItemMousePressed
        c.saveGame();
        c.clearLog();
    }//GEN-LAST:event_saveMenuItemMousePressed

    private void undoMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_undoMenuItemMouseReleased

    private void redoMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_redoMenuItemMouseReleased

    private void saveMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_saveMenuItemMouseReleased

    private void preferMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_preferMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_preferMenuItemMouseReleased

    private void preferMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_preferMenuItemMousePressed
//        Preferences.setEnabled(true);
//        Preferences.setVisible(true);
        repaint();
    }//GEN-LAST:event_preferMenuItemMousePressed

    private void loadMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_loadMenuItemMouseReleased

    private void jMenuItem4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MouseReleased
        repaint();
    }//GEN-LAST:event_jMenuItem4MouseReleased

    private void jMenuItem3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseReleased
        repaint();
    }//GEN-LAST:event_jMenuItem3MouseReleased

    private void jMenuItem5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MouseReleased
        repaint();
    }//GEN-LAST:event_jMenuItem5MouseReleased

    private void jMenuItem9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MousePressed
        repaint();
    }//GEN-LAST:event_jMenuItem9MousePressed

    private void exitMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMenuItemMouseReleased
        repaint();
    }//GEN-LAST:event_exitMenuItemMouseReleased

    private void exitMenuItemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMenuItemMousePressed
        confirmExit();
    }//GEN-LAST:event_exitMenuItemMousePressed

    private void exitMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_exitMenuItemMenuKeyPressed

    }//GEN-LAST:event_exitMenuItemMenuKeyPressed

    private void jMenu1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenu1StateChanged
        repaint();
    }//GEN-LAST:event_jMenu1StateChanged

    private void jMenu4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenu4StateChanged
        // TODO add your handling code here:
        repaint();
    }//GEN-LAST:event_jMenu4StateChanged

    private void jMenu3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenu3StateChanged
        // TODO add your handling code here:
        repaint();
    }//GEN-LAST:event_jMenu3StateChanged

    private void jMenu5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenu5StateChanged
        // TODO add your handling code here:
        repaint();
    }//GEN-LAST:event_jMenu5StateChanged

    private void jMenu2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jMenu2StateChanged
        repaint();
    }//GEN-LAST:event_jMenu2StateChanged

    private void preferMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_preferMenuItemActionPerformed

    private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
        c.clickLoadUndo();
    }//GEN-LAST:event_undoMenuItemActionPerformed

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuItemActionPerformed
        c.loadGame();
        repaint();
    }//GEN-LAST:event_loadMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        c.saveGame();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        confirmExit();
        repaint();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        this.sair();
    }//GEN-LAST:event_jButton1MousePressed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowGainedFocus

    private void QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_QuitActionPerformed

    private void UndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UndoActionPerformed
        c.removeLastLog();
    }//GEN-LAST:event_UndoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Alert;
    private javax.swing.JButton BishopType;
    private javax.swing.JDialog DetailsMatch;
    private javax.swing.JLabel Index;
    private javax.swing.JLabel Index1;
    private javax.swing.JLabel Index2;
    private javax.swing.JButton Load;
    private javax.swing.JLabel NomeDoJogo;
    private javax.swing.JDialog Preferences;
    private javax.swing.JButton Quit;
    private javax.swing.JButton Redo;
    private javax.swing.JButton Save;
    private javax.swing.JLabel Timer;
    private javax.swing.JLabel Total;
    private javax.swing.JLabel Turn;
    private javax.swing.JButton Undo;
    private javax.swing.JDialog alertCheck;
    private javax.swing.JDialog alertCheckMate;
    private javax.swing.JPanel boardPanel;
    private javax.swing.JButton cancelReturn;
    private javax.swing.JDialog continueQuit;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton knightType;
    private java.awt.Label label1;
    private javax.swing.JLabel label_confirmToQuit;
    private java.awt.List list1;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JLabel nomeAluno;
    private javax.swing.JLabel nomeProf1;
    private javax.swing.JButton okAbandon;
    private javax.swing.JButton okCheck;
    private javax.swing.JButton okPromote;
    private javax.swing.JDialog pawnPromotion;
    private javax.swing.JLabel player;
    private javax.swing.JLabel playerClone;
    private javax.swing.JMenuItem preferMenuItem;
    private javax.swing.JButton queenType;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JButton rookType;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel showTimerTotal;
    private javax.swing.JLabel showTimerTurn;
    private javax.swing.JMenuItem undoMenuItem;
    // End of variables declaration//GEN-END:variables

    private String playerTurnChangeAlert() {

        if (playerTurn == 1) {
            this.playerTurn = 2;
        } else {
            this.playerTurn = 1;
        }

        return "Player " + this.playerTurn + " turn.";
    }

    private void confirmExit() {
        continueQuit.setVisible(true);
        continueQuit.setEnabled(true);
        Dimension d = new Dimension(292, 200);
        continueQuit.setSize(d);
        repaint();
    }
}
