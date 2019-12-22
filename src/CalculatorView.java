import java.awt.event.ActionListener;

public interface CalculatorView extends ActionListener {
    void registerObserver(CalculatorController controller);

    void updateTopDisplay(String s);

    void updateBottomDisplay(String s);

    void updateTranAllowed(boolean allowed);
}
