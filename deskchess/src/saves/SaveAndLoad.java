/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saves;

import deskchess.chess.pieces.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author lcsvcn
 */
public class SaveAndLoad implements Serializable {
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private final String fileNameWhite;
    private final String fileNameBlack;
    private int contBlack;
    private int contWhite;
    private int turn;

    public SaveAndLoad(String fileNameWhite, String fileNameBlack, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.turn = 0;
        this.fileNameWhite = fileNameWhite;
        this.fileNameBlack = fileNameBlack;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.contWhite = 0;
        this.contWhite = 0;
    }

    public int getContWhite() {
        return this.contWhite;
    }

    public int getContBlack() {
        return this.contBlack;
    }

    public void loadList(int contWhite, int contBlack) {
        whitePieces.clear();
        blackPieces.clear();
        this.contWhite = contWhite;
        this.contBlack = contBlack;
        boolean sucess1 = false;
        boolean sucess2 = false;
        sucess1 = loadObjectWhite();
        sucess2 = loadObjectBlack();
        System.out.printf("%s\n", (sucess1 == true) ? "Sucessfully Load (1)!" : "Failed Load(1)!");
        System.out.printf("%s\n", (sucess2 == true) ? "Sucessfully Load (2)!" : "Failed Load(2)!");
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return this.turn;
    }

    public ArrayList<Piece> getWhiteList() {
        if (whitePieces == null) {
            System.err.println("Erro null whitePieces");
        }
        return whitePieces;
    }

    public ArrayList<Piece> getBlackList() {
        if (blackPieces == null) {
            System.err.println("Erro null blackPieces");
        }
        return blackPieces;
    }

    public void saveList() {
        System.out.printf("%s\n", (saveObject() ? "Sucessfully Save!" : "Failed Save!"));
    }

    public void updateList(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }

    private boolean loadObjectWhite() {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileNameWhite))) {
                Piece p;
                for (int i = 0; i < contWhite; i++) {
                    p = (Piece) ois.readObject();
                    if (p == null) {
                        break;
                    }
                    whitePieces.add(p);
                }
                ois.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("SAVE AND LOAD --> FileNotFoundException");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("SAVE AND LOAD --> IOException 00");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("SAVE AND LOAD --> ClassNotFoundException");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadObjectBlack() {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileNameBlack))) {
                Piece p;
                for (int i = 0; i < contBlack; i++) {
                    p = (Piece) ois.readObject();
                    if (p == null) {
                        break;
                    }
                    blackPieces.add(p);
                }
                ois.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("SAVE AND LOAD --> FileNotFoundException");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("SAVE AND LOAD --> IOException 00");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("SAVE AND LOAD --> ClassNotFoundException");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean saveObject() {
        try {
            contBlack = 0;
            contWhite = 0;
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(fileNameWhite));
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(fileNameBlack));

            for (Piece p : whitePieces) {
                oos1.writeObject(p);
                contWhite++;
            }

            for (Piece p : blackPieces) {
                oos2.writeObject(p);
                contBlack++;
            }

            oos1.close();
            oos2.close();;
        } catch (FileNotFoundException e) {
            System.out.println("SAVE AND LOAD --> FileNotFoundException");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println("SAVE AND LOAD --> IOException");
            e.printStackTrace();
            return false;
        }

        return true;
    }

//    public boolean backupSave() {
//        try {
//            ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream(fileNameWhite));
//            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(fileNameBlack));
//            ObjectOutputStream oos1 = new ObjectOutputStream(new FileInputStream("src/bin/backup0.dat"));
//            ObjectOutputStream oos2 = new ObjectOutputStream(new FileInputStream("src/bin/backup1.dat"));
//
//            for (Piece p1 : whitePieces) {
//                p = (Piece) oos2.readObject();
//            }
//
//            for (Piece p2 : blackPieces) {
//
//            }
//
//            ois1.close();
//            ois2.close();
//            oos1.close();
//            oos2.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("BACKUP --> FileNotFoundException");
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            System.out.println("BACKUP --> IOException");
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    public boolean backupLoad() {
//        try {
//            ObjectInputStream ois2;
//            try (ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("src/bin/backup0.dat"))) {
//                ois2 = new ObjectInputStream(new FileInputStream("src/bin/backup1.dat"));
//                ObjectOutputStream oos2;
//                try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileInputStream(fileNameWhite))) {
//                    oos2 = new ObjectOutputStream(new FileInputStream(fileNameBlack));
//                    whitePieces.clear();
//                    Piece p1 = null;
//
//                    try {
//                        p1 = (Piece) ois1.readObject();
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(SaveAndLoad.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    while (p1 != null) {
//                        try {
//                            p1 = (Piece) ois1.readObject();
//                        } catch (ClassNotFoundException ex) {
//                            Logger.getLogger(SaveAndLoad.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        whitePieces.add(p1);
//                    }
//
//                    Piece p2;
//
//                    try {
//                        p2 = (Piece) ois2.readObject();
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(SaveAndLoad.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    while (p2 != null) {
//                        p2 = (Piece) ois2.readObject();
//                        blackPieces.add(p2);
//                    }
//                }
//                oos2.close();
//            }
//            ois2.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("BACKUP --> FileNotFoundException");
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            System.out.println("BACKUP --> IOException");
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
}
