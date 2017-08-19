/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timer;

import view.Chess;

/**
 *
 * @author lcsvcn
 */
public class AutoSave extends Thread {
    private final Chess c;
    private float wait;
    private boolean reset;
    private int cont;
    private boolean active;

    public AutoSave(Chess c) {
        this.c = c;
        this.wait = c.getWaitAutoSave();
        init();
    }
    
    private void setResetClock() {
        this.reset = true;
    }

    private void init() {
        this.cont = 0;
        this.active = true;
        resetClock();
        start();
    }

    public final void stopThread() {
        this.active = false;
    }

    public final void continueThread() {
        this.active = true;
    }
    
    public void setWait(float wait) {
        this.wait = wait;
    }
    
    @Override
    public void run() {
        try {
            while (active) {
                Thread.sleep(1000);
                cont++;
                
                try {
                    if(wait < 0.1) {
                        wait = 0.5f;     // Min (30 seg)
                    }
                    if(wait > 30.0) {   // Max (30 minutos)
                        wait = 30.0f;
                    }
                    if(cont > (60 * wait)) {
                        System.out.printf("> AUTOSAVED : ");
                        c.saveGame();
                        resetClock();
                    }
                } catch (NullPointerException e) {
                    System.err.println("NullPointerException at labels in class Counter.java \n");
                }
            }
        } catch (InterruptedException e) {
            System.err.println("InterruptedException at (@Override)run in class Counter.java\n");
        }
    }

    private void resetClock() {
        this.cont = 0;
    }
}
