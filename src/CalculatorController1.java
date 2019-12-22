import java.util.HashMap;
import java.util.Stack;

public class CalculatorController1 implements CalculatorController {

    private final CalculatorModel model;

    private final CalculatorView view;

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
        this.view.updateTranAllowed(false);
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processEnterEvent() {
        if(!isValid(this.model.getTopLeft() + this.model.getTopRight())){
            this.model.setBottom("InValid Expression");
        }else {
            long[] res = eval(this.model.getTopLeft() + this.model.getTopRight());
            if (res[1] == 1) {
                this.model.setBottom("" + res[0]);
            } else if (res[1] == 0) {
                this.model.setBottom("NaN");
            } else {
                this.model.setBottom(res[0] + " / " + res[1]);
                this.view.updateTranAllowed(true);
            }
        }
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processDeleteEvent() {
        String temp = this.model.getTopLeft();
        if (temp.length() > 0){
            this.model.setTopLeft(temp.substring(0,temp.length()-1));
        }
        this.view.updateTranAllowed(false);
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processEditEvent(char c) {
        this.model.setTopLeft(this.model.getTopLeft() + c);
        this.view.updateTranAllowed(false);
        updateViewToMatchModel(model, view);
    }


    @Override
    public void processTranEvent() {
        if(this.model.getBottom().indexOf('/') == -1){
            processEnterEvent();
        }else{
            long[] temp = eval(this.model.getTopLeft() + this.model.getTopRight());
            this.model.setBottom(fractionToDecimal(temp[0], temp[1]));
            updateViewToMatchModel(model, view);
        }
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
        Stack<Point> stack = new Stack<>();
        long[] cur = new long[]{0,1}, pre = new long[]{0,1}, ans = new long[]{0,1};
        long sign = 1;
        int state = 0;
        boolean flag = false;
        s += "+";
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(i > 0 && (c == '(' && Character.isDigit(s.charAt(i - 1)) || Character.isDigit(c) && s.charAt(i - 1) == ')')){
                if(!flag){
                    c = '*';
                    i --;
                }
                flag = !flag;
            }
            if (Character.isDigit(c)){
                cur[0] = cur[0] * 10 + Long.parseLong("" + c);
            } else if((c == '+' || c =='-') && (i == 0 || !Character.isDigit(s.charAt(i - 1)) && s.charAt(i - 1) != ')')){
                if(c == '-') sign *= -1;
            } else if(c == '('){
                stack.push(new Point(sign, ans, pre, state));
                ans = new long[]{0,1};
                state = 0;
                sign = 1;
                pre = new long[]{0,1};
            }else{
                if(state == 1)
                    pre = time(pre, cur);
                else if(state == 2)
                    pre = divide(pre, cur);
                else
                    pre = cur;
                if(c == ')') {
                    pre = time(pre, new long[]{sign, 1});
                    cur = add(ans, pre);
                    Point temp = stack.pop();
                    sign = temp.getSign();
                    ans = temp.getAns();
                    pre = temp.getPre();
                    state = temp.getState();
                }else if(c == '-' || c == '+'){
                    pre = time(pre, new long[]{sign, 1});
                    ans = add(ans, pre);
                    state = 0;
                    pre = new long[]{0,1};
                    sign = c == '+' ? 1 : -1;
                    cur = new long[]{0,1};
                }else{
                    state = c == '*'? 1 : 2;
                    cur = new long[]{0,1};
                }
            }
        }
        if(ans[1] < 0){
            ans[1] = - ans[1];
            ans[0] = - ans[0];
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

    public long[] simplifyFraction(long a, long b){
        long gcd = gcd(a, b);
        return new long[]{a / gcd, b / gcd};
    }

    private long gcd(long a, long b){
        return b == 0? a: gcd(b, a % b);
    }

    private boolean isValid(String s){
        //check the last character of the expression
        if(s.length() > 0 && ! Character.isDigit(s.charAt(s.length() - 1)) && s.charAt(s.length() - 1) != ')')
            return false;
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

    public String fractionToDecimal(long numerator, long denominator) {
        StringBuilder result = new StringBuilder();
        String sign = (numerator < 0 == denominator < 0 || numerator == 0) ? "" : "-";
        long num = Math.abs(numerator);
        long den = Math.abs(denominator);
        result.append(sign);
        result.append(num / den);
        long remainder = num % den;
        if (remainder == 0)
            return result.toString();
        result.append(".");
        HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        while (!hashMap.containsKey(remainder)) {
            hashMap.put(remainder, result.length());
            result.append(10 * remainder / den);
            remainder = 10 * remainder % den;
        }
        int index = hashMap.get(remainder);
        result.insert(index, "(");
        result.append(")");
        return result.toString().replace("(0)", "");
    }
}
