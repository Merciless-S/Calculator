public class CalculatorController1 implements CalculatorController {

    private final CalculatorModel model;

    private final CalculatorView view;

    public CalculatorController1(CalculatorModel model, CalculatorView view){
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    private static void updateViewToMatchModel(CalculatorModel model, CalculatorView view){
        String top = model.getTop(), bottom = model.getBottom();
        view.updateTopDisplay(top);
        view.updateBottomDisplay(bottom);
    }

    @Override
    public void processClearEvent() {
        if(this.model.getTop().length() == 0)
            this.model.setBottom("");
        else
            this.model.setTop("");
    }

    @Override
    public void processEnterEvent() {

    }

    @Override
    public void processDeleteEvent() {

    }

    @Override
    public void processEditEvent(char c) {

    }


    @Override
    public void processTranEvent() {

    }

    @Override
    public void processSwitchEvent(int i) {

    }
}
