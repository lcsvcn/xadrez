/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcsvcn
 */
public class saveVariables {

    private int contWhite;
    private int contBlack;
    private final String fileSource;
    private final File file = null;

    public saveVariables(String fileSource) {
        this.fileSource = fileSource;
    }

    public void setCont(int contWhite, int contBlack) {
        this.contWhite = contWhite;
        this.contBlack = contBlack;
    }

    public int getContWhite() {
        return this.contWhite;
    }

    public int getContBlack() {
        return this.contBlack;
    }
    
    public void createFile(ArrayList<String> al, int turn) {
      
        try {
            try (PrintWriter pw = new PrintWriter(new File(fileSource))) {
                pw.println(contWhite);
                pw.println(contBlack);
                pw.println(turn);
                for(String str : al) {
                    pw.println(str);
                }
                pw.println("EOF");
                pw.close();
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(saveVariables.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void readFile(ArrayList<String> al, int turn) {
        FileReader fr = null;
        
        try {
            fr = new FileReader(new File(fileSource));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(saveVariables.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            try (BufferedReader br = new BufferedReader(fr)) {
//                System.out.printf("%d\n%d\n", contWhite, contBlack);
                contWhite = Integer.parseInt(br.readLine());
                contBlack = Integer.parseInt(br.readLine());
                turn = Integer.parseInt(br.readLine());
                System.out.println("turn : " + turn);
                String str = br.readLine();
                while(!"EOF".equals(str)) {
                    al.add(str);
                    str = br.readLine();
                }
                br.close();   
            }
            fr.close();
        } catch (IOException e) {
            System.err.println("IOException -");
            e.printStackTrace();
        }

    }

}
