public final class Calculator {

    public static void main(String[] args){
        CalculatorModel model = new CalculatorModel1();
        CalculatorView view = new CalculatorView1();
        CalculatorController controller = new CalculatorController1(model, view);
        view.registerObserver(controller);
    }
}
