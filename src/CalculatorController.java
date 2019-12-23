public interface CalculatorController {
    /**
     * process the event when the clear button was pressed
     */
    void processClearEvent();

    /**
     * process the event when the enter button was pressed
     */
    void processEnterEvent();

    /**
     * process the event when the delete button was pressed
     */
    void processDeleteEvent();

    /**
     * process the event when the user add a number or an operator at the position of the cursor
     * @param c
     */
    void processEditEvent(char c);

    /**
     * process the event when the user wants to transform a fraction to digit number or digit number to fraction on the bottom screen
     */
    void processTranEvent();

    /**
     * process the event when the user wants to shift the cursor to left or to right.
     * @param i
     */
    void processShiftEvent(int i);

    /**
     * process the event when the user wants to take the power of the number in the bottom screen
     */
    void processPowerEvent();

    /**
     * process the event when the user wants to take the sqrt of the number in the bottom screen
     */
    void processRootEvent();

}
