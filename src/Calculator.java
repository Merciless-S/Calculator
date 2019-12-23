/**
 * @author Merciless
 * Any issues found, please contact 2051183268yjz@gmail.com
 */
public final class Calculator {

    public static void main(String[] args){
        /*
         * Create instances of the model, view, and controller objects;
         * controller needs to know about model and view, and view needs to know
         * about controller
         */
        CalculatorModel model = new CalculatorModel1();
        CalculatorView view = new CalculatorView1();
        CalculatorController controller = new CalculatorController1(model, view);
        view.registerObserver(controller);
    }
}
