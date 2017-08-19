/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

/**
 *
 * @author lcsvcn
 */
public interface LogBasic {
    public void removeAtIndex(int index);
    public void removeInterval(int from, int to);
    public void clear();
    public String get(int index);
}   
