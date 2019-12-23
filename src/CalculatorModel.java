/**
 * @author Merciless
 */
public interface CalculatorModel {

    /**
     * return the string to the left of the cursor on the top screen
     * @return a string
     */
    String getTopLeft();

    /**
     * return the string to the right of the cursor on the top screen
     * @return a string
     */
    String getTopRight();

    /**
     * return the string on the bottom screen
     * @return a string
     */
    String getBottom();

    /**
     * set the bottom string
     * @param bottom a string
     */
    void setBottom(String bottom);

    /**
     * set the top left part of the String
     * @param s a string
     */
    void setTopLeft(String s);

    /**
     * Set the top right part of the String
     * @param s a string
     */
    void setTopRight(String s);
}
