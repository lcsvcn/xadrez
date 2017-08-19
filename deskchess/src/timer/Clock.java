/**
 * @author Lucas V. C. Nicolau
 * @NUSP 8517101
 */
package timer;

/**
 *
 * @author lcsvcn
 */
public interface Clock {
    
    public int setSeconds(int value);
    
    public int setMinutes(int value);
    
    public int setHours(int value);
    
    public String completeWithZero(Integer value);
    
    public String getTimer();
}