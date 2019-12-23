import java.awt.event.ActionListener;

public interface CalculatorView extends ActionListener  {
    /**
     * register the controller to the view
     * @param controller
     */
    void registerObserver(CalculatorController controller);

    /**
     * update the top screen content
     * @param s a string to be displayed on the top
     */
    void updateTopDisplay(String s);

    /**
     * update the bottom screen content
     * @param s a string to be displayed on the bottom
     */
    void updateBottomDisplay(String s);

    /**
     * update if the transfer from fraction to digit button (d<->f) is enabled
     * @param allowed a boolean indicating if the button is enabled
     */
    void updateTranAllowed(boolean allowed);

    /**
     * update if the transfer from fraction to digit button (d<->f) is enabled
     * @param allowed a boolean indicating if the button is enabled
     */
    void updateSqrtAllowed(boolean allowed);

    /**
     * update if the transfer from fraction to digit button (d<->f) is enabled
     * @param allowed a boolean indicating if the button is enabled
     */
    void updatePowerAllowed(boolean allowed);
}
