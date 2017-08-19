/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Wildcard;
import java.util.ArrayList;

/**
 *
 * @author lcsvcn
 */
public class Log implements LogBasic {
    private final ArrayList<String> pColor;
    private final ArrayList<String> pTeam;
    private final ArrayList<String> pNotation;
    private int color;
    
    public Log() {
        pColor = new ArrayList<>();
        pTeam = new ArrayList<>();
        pNotation = new ArrayList<>();
    }
    
    @Override
    public String get(int index) {
        return (pColor.isEmpty() || pColor.size() < index) ? "Error" : pColor.get(index)+pTeam.get(index)+pNotation.get(index);
    }
    
    public String getLastIndex() {
        int iLast = pColor.size() - 1;
        return (iLast < 0) ? "Error" : pColor.get(iLast)+" "+pTeam.get(iLast)+" "+pNotation.get(iLast)  ;
   }
    
    protected enum AL {
        COLOR, TEAM, NOTATION;
    };
    
    private void setColor(String color) {
        pColor.add(color.toUpperCase());
    }
    
    private void setTeam(int player) {
        pTeam.add("P"+player);
    }
    
    private void setNotation(String notation) {
        pNotation.add(notation.toUpperCase());
    }
    
    public void add(String color, int player, String notation) {
        setColor(color);
        setNotation(notation);
        setTeam(player);
    }
    
    @Override
    public void removeAtIndex(int index) {
        pColor.remove(index);
        pNotation.remove(index);
        pTeam.remove(index);
    }

    @Override
    public void removeInterval(int from, int to) {
        for(int i=from; i < to; i++) {
            pColor.remove(i);
            pNotation.remove(i);
            pTeam.remove(i);
        }
    }

    @Override
    public void clear() {
        pColor.clear();
        pNotation.clear();
        pTeam.clear();
    }
    
    public void printAll() {
        int iLast = pColor.size() - 1;
        for(int i=0; i< iLast; i++) {
            System.out.println(get(i));
        }
    }
    
    
}
