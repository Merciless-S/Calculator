/**
 * @author Merciless
 */
public class CalculatorModel1 implements CalculatorModel {

    private String topLeft, topRight, bottom;

    /**
     * constructor
     */
    public CalculatorModel1(){
        this.topLeft = "";
        this.topRight = "";
        this.bottom = "";
    }

    @Override
    public String getTopLeft(){
        return this.topLeft;
    }

    @Override
    public String getTopRight(){
        return this.topRight;
    }

    @Override
    public String getBottom(){
        return this.bottom;
    }

    @Override
    public void setTopLeft(String s){
        this.topLeft = s;
    }

    @Override
    public void setTopRight(String s) {
        this.topRight = s;
    }

    @Override
    public void setBottom(String bottom){
        this.bottom = bottom;
    }
}
