import java.util.Set;
import java.util.Stack;

public class CalculatorController1 implements CalculatorController {

    private final CalculatorModel model;

    private final CalculatorView view;

    //private final Set<Character> addNotAllowed, subtractNotAllowed, divisionNotAllowed, multiplyNotAllowed, RightPNotAllowed;


    protected CalculatorController1(CalculatorModel model, CalculatorView view){
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    private static void updateViewToMatchModel(CalculatorModel model, CalculatorView view){
        String topLeft = model.getTopLeft(), topRight = model.getTopRight(), bottom = model.getBottom();
        StringBuilder left = new StringBuilder(), right = new StringBuilder();
        if(topLeft.length() > 0) {
            for (char c : topLeft.toCharArray()) {
                left.append(c + " ");
            }

        }
        if(topRight.length() > 0) {
            for (char c : topRight.toCharArray()) {
                right.append(c + " ");
            }
            right.deleteCharAt(right.length() - 1);
            if(left.length() > 0)
                left.deleteCharAt(left.length() - 1);
            left.append("|");
            left.append(right);
        }else{
            left.append("|");
        }
        //view.updateTopDisplay(topLeft + "|" + topRight);
        view.updateTopDisplay(left.toString());
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
        if(!isValid(this.model.getTopLeft() + this.model.getTopRight())){
            this.model.setBottom("InValid Expression");
        }else{
            long[] res= eval(this.model.getTopLeft() + this.model.getTopRight());
            this.model.setBottom(res[0] + " / " + res[1]);
        }
        //else
            //this.model.setBottom("" + eval(this.model.getTopLeft() + this.model.getTopRight()));
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

    private long[] eval(String s){
        System.out.println(s);
        Stack<Long> stack = new Stack<>();
        long[] cur = new long[]{0,1}, pre = new long[]{0,1}, ans = new long[]{0,1};
        long sign = 1;
        int state = 0;
        s += "+";
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)){
                cur[0] = cur[0] * 10 + Long.parseLong("" + c);
            } else{
                if(state == 1)
                    pre = time(pre, cur);
                else if(state == 2)
                    pre = divide(pre, cur);
                else
                    pre = cur;
                cur = new long[]{0,1};
                if(c == '-' || c == '+'){
                    System.out.println("sign: " + sign);
                    pre = time(pre, new long[]{sign, 1});
                    System.out.println("pre[0]: " + pre[0] + "pre[1]: " + pre[1]);
                    ans = add(ans, pre);
                    state = 0;
                    pre = new long[]{0,1};
                    sign = c == '+' ? 1 : -1;
                }else{
                    state = c == '*'? 1 : 2;
                }
            }
        }
        return ans;
    }


    private long[] divide(long[] a, long[] b){
        long n = a[0] * b[1], d = a[1] * b[0];
        return simplifyFraction(n, d);
    }

    private long[] time(long[] a, long[] b){
        long n = a[0] * b[0], d = a[1]  *  b[1];
        return simplifyFraction(n, d);
    }

    private long[] add(long[] a, long[] b){
        long n = a[0] * b[1] + b[0] * a[1], d = a[1]  *  b[1];
        return simplifyFraction(n, d);
    }

    private long[] subtract(long[] a, long[] b){
        long n = a[0] * b[1] - b[0] * a[1], d = a[1]  *  b[1];
        return simplifyFraction(n, d);
    }

    public long[] simplifyFraction(long a, long b){
        long gcd = gcd(a, b);
        return new long[]{a / gcd, b / gcd};
    }

    private long gcd(long a, long b){
        return b == 0? a: gcd(b, a % b);
    }

    private boolean isValid(String s){
        //Check parentheses
        int left = 0;
        for(char c : s.toCharArray()) {
            if (c == '('){
                left ++;
            }else if(c == ')')
                left --;
            if(left < 0)
                return false;
        }
        if(left != 0) return false;

        //Check syntax
        for(int i = 0; i < s.length(); ++i){
            char cur = s.charAt(i);
            if(cur == '*' || cur == '/'){
                if(i == 0)
                    return false;
                char pre = s.charAt(i - 1);
                if(pre == '-' || pre == '+' || pre == '(' || pre == '*' || pre == '/')
                    return false;
            }else if(cur == ')'){
                char pre = s.charAt(i - 1);
                if(!Character.isDigit(pre) && pre != ')')
                    return false;
            }
        }
        return true;
    }
}
