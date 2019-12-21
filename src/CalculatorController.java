public interface CalculatorController {
    void processClearEvent();

    void processEnterEvent();

    void processDeleteEvent();

    void processEditEvent(char c);

    void processTranEvent();

    void processShiftEvent(int i);


}
