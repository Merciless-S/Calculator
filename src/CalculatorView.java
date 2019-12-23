import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public interface CalculatorView extends ActionListener  {
    void registerObserver(CalculatorController controller);

    void updateTopDisplay(String s);

    void updateBottomDisplay(String s);

    void updateTranAllowed(boolean allowed);

    void updateSqrtAllowed(boolean allowed);

    void updatePowerAllowed(boolean allowed);
}
