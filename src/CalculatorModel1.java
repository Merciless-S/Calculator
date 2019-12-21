public class CalculatorModel1 implements CalculatorModel {

    private String top, bottom;

    public CalculatorModel1(){
        this.top = "";
        this.bottom = "";
    }
    public String getTop(){
        return this.top;
    }

    public String getBottom(){
        return this.bottom;
    }

    public void setTop(String top){
        this.top = top;
    }

    public void setBottom(String bottom){
        this.bottom = bottom;
    }
}
