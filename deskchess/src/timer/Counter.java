/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timer;

import javax.swing.JLabel;

/**
 *
 * @author lcsvcn
 */
public class Counter extends Thread implements Clock {

    public boolean active;
    private int seconds;
    private int minutes;
    private int hours;
    private final JLabel label;
    private int cont;
    boolean reset;

    public Counter(JLabel label) {
        this.label = label;
        init();
    }

    public void setResetClock() {
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

    @Override
    public int setSeconds(int value) {
        if (value > 59) {
            this.minutes = setMinutes(this.minutes + 1);
            return 0;
        }
        this.seconds = value;
        return value;
    }

    @Override
    public int setMinutes(int value) {
        if (value > 59) {
            this.hours = setHours(this.hours + 1);
            return 0;
        }
        return value;
    }

    @Override
    public int setHours(int value) {
        if (value > 23) {
            resetClock();
            return 0;
        }
        return value;
    }

    @Override
    public String completeWithZero(Integer value) {
        return (value < 10) ? "0" + value : value.toString();
    }

    @Override
    public String getTimer() {
        return completeWithZero(this.hours) + ":" + completeWithZero(this.minutes) + ":" + completeWithZero(this.seconds);
    }

    @Override
    public void run() {
        try {
            while (active) {
                Thread.sleep(1000);
                if (this.reset) {
                    resetClock();
                    this.reset = false;
                } else {
                    cont++;
                    cont = setSeconds(cont);
                }
                try {
                    label.setText(getTimer());
                    label.repaint();
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
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
    }

}
