public class CalculatorController1 implements CalculatorController {

    private final CalculatorModel model;

    private final CalculatorView view;

    public CalculatorController1(CalculatorModel model, CalculatorView view){
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    private static void updateViewToMatchModel(CalculatorModel model, CalculatorView view){
        String topLeft = model.getTopLeft(), topRight = model.getTopRight(), bottom = model.getBottom();
        view.updateTopDisplay(topLeft + "|" + topRight);
        view.updateBottomDisplay(bottom);
    }

    @Override
    public void processClearEvent() {
        if((this.model.getTopLeft() + this.model.getTopRight()).length() == 0)
            this.model.setBottom("");
        else {
            this.model.setTopLeft("");
            this.model.setTopRight("");
        }
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processEnterEvent() {
        this.model.setBottom("" + eval(this.model.getTopLeft() + this.model.getTopRight()));
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processDeleteEvent() {
        String temp = this.model.getTopLeft();
        if (temp.length() > 0){
            this.model.setTopLeft(temp.substring(0,temp.length()-1));
        }
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processEditEvent(char c) {
        this.model.setTopLeft(this.model.getTopLeft() + c);
        updateViewToMatchModel(model, view);
    }


    @Override
    public void processTranEvent() {
        //TODO
    }

    @Override
    public void processShiftEvent(int i) {
        String left = this.model.getTopLeft(), right = this.model.getTopRight();
        if(i > 0 && right.length() > 0){
            this.model.setTopLeft(left + right.charAt(0));
            this.model.setTopRight(right.substring(1));
        }else if(i < 0 && left.length() > 0){
            this.model.setTopRight(left.charAt(left.length() - 1) + right);
            this.model.setTopLeft(left.substring(0, left.length() - 1));
        }
        updateViewToMatchModel(model, view);
    }

    private int eval(String s){
        //TODO
        return -1;
    }
}
