import java.util.HashMap;
import java.util.Stack;

/**
 * @author Merciless
 */
public class CalculatorController1 implements CalculatorController {

    //Model Object
    private final CalculatorModel model;

    //View Object
    private final CalculatorView view;

    /**
     * Constructor
     * @param model
     * @param view
     */
    protected CalculatorController1(CalculatorModel model, CalculatorView view){
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private void updateViewToMatchModel(CalculatorModel model, CalculatorView view){
        //Combine the top left String and top Right string with a '|' as a cursor between
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
        //Check if power is allowed
        view.updatePowerAllowed(this.allowRootOrPower());
        //Check if root is allowed
        view.updateSqrtAllowed(this.allowRootOrPower());
        //inform view to display top and bottom panel
        view.updateTopDisplay(left.toString());
        view.updateBottomDisplay(bottom);
    }

    @Override
    public void processClearEvent() {
        //when the top has content, clear top, otherwise clear the bottom
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
        /**
         * After the user hit enter, then try to solve the equation
         */
        if(!isValid(this.model.getTopLeft() + this.model.getTopRight())){
            this.model.setBottom("InValid Expression");
        }else {
            long[] res;
            try {
                //call the eval function to evaluate the top equation
                res = eval(this.model.getTopLeft() + this.model.getTopRight());
            }catch(ArithmeticException e){
                //case when 0/0
                this.model.setBottom("NaN");
                updateViewToMatchModel(model, view);
                return;
            }catch(NumberFormatException e){
                //case where a float number cannot be parsed, such as "0.23.1"
                this.model.setBottom("Invalid float number");
                updateViewToMatchModel(model, view);
                return;
            }
            if (res[1] == 1) {
                //case where the denominator is 1, then only display the numerator
                this.model.setBottom("" + res[0]);
            } else if (res[1] == 0) {
                //case where the denominator is 0
                this.model.setBottom("NaN");
            } else {
                //Other wise display as a fraction
                this.model.setBottom(res[0] + " / " + res[1]);
                this.view.updateTranAllowed(true);
            }
        }
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processDeleteEvent() {
        //delete a character to the left of the cursor. If not character is available then maintain no change
        String temp = this.model.getTopLeft();
        if (temp.length() > 0){
            this.model.setTopLeft(temp.substring(0,temp.length()-1));
        }
        this.view.updateTranAllowed(false);
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processEditEvent(char c) {
        //add a character to the left of the cursor
        this.model.setTopLeft(this.model.getTopLeft() + c);
        this.view.updateTranAllowed(false);
        updateViewToMatchModel(model, view);
    }


    @Override
    public void processTranEvent() {
        //Transform the fraction to digit or from float to fraction.
        if(this.model.getBottom().indexOf('/') == -1){
            //case where the bottom is a float, then recalculate the equation
            processEnterEvent();
        }else{
            //case where the bottom is a fraction, then call a function to do the transformation
            long[] temp = eval(this.model.getTopLeft() + this.model.getTopRight());
            this.model.setBottom(fractionToDecimal(temp[0], temp[1]));
            updateViewToMatchModel(model, view);
        }
    }

    @Override
    public void processShiftEvent(int i) {
        /**
         * user wants to shift the cursor to the left or to right. If the cursor is already
         * at the left most position, then shift to the right most position, and vise versa.
         */
        String left = this.model.getTopLeft(), right = this.model.getTopRight();
        if(i > 0){
            //user wants to shift to right
             if(right.length() > 0 && i == 1) {
                 //truncate the first character of the top right string to concatenate to the end of the top left string
                 this.model.setTopLeft(left + right.charAt(0));
                 this.model.setTopRight(right.substring(1));
             }else{
                 //shift to left most
                 this.model.setTopRight(left + right);
                 this.model.setTopLeft("");
             }
        }else if(i < 0){
            if(left.length() > 0 && i == -1) {
                //truncate the last character of the top left string to concatenate to the beginning top right string
                this.model.setTopRight(left.charAt(left.length() - 1) + right);
                this.model.setTopLeft(left.substring(0, left.length() - 1));
            }else{
                //shift to right most
                this.model.setTopLeft(left + right);
                this.model.setTopRight("");
            }
        }
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processPowerEvent() {
        //get the bottom string
        String bottom = this.model.getBottom();
        //parse the double number, and then take the power
        this.model.setBottom("" + Math.pow(parseDouble(bottom),2));
        //disable the transform button
        this.view.updateTranAllowed(false);
        this.updateViewToMatchModel(this.model, this.view);
    }

    /**
     * parse the String on the bottom part of the screen as a double. The original string could be a fraction (a/b), recurring float (0.0(a)) or a normal float
     * @param bottom the string on the bottom screen
     * @return a double number
     */
    private double parseDouble(String bottom){
        double res;
        //Case where the String is a fraction
        if(bottom.indexOf('/') != -1){
            int index = bottom.indexOf('/');
            res = Double.parseDouble(bottom.substring(0, index)) / Double.parseDouble(bottom.substring(index + 1));
        //case where the String is a recurring float, then first transform it to a normal float with at least 10 digits.
        }else if(bottom.indexOf('(') != -1){
            int index = bottom.indexOf('(');
            String temp = bottom.substring(0, index), repeat = bottom.substring(index + 1, bottom.length() - 1);
            temp += repeat;

            while(temp.length() < 20){
                temp += repeat;
            }
            res = Double.parseDouble(temp);
        //case where the string is a normal number
        }else{
            res = Double.parseDouble(bottom);
        }
        return res;
    }

    @Override
    public void processRootEvent() {
        //get the bottom string
        String bottom = this.model.getBottom();
        //parse the double number, and then take the sqrt root
        this.model.setBottom("" + Math.sqrt(parseDouble(bottom)));
        //disable the transform button
        this.view.updateTranAllowed(false);
        this.updateViewToMatchModel(this.model, this.view);
    }

    /**
     * evaluate the given statement and return a long[2] as the fraction, where the first number is the numerator, and the second number is the denominator
     * the function used a single stack to keep track of the arithmetic order and constrains.
     * for more info, please refer to https://leetcode.com/problems/basic-calculator-iii/discuss/406328/Python-SINGLE-traversal-and-SINGLE-Stack-solution-beats-95
     * @param s a string containing the arithmetic equation
     * @return a fraction
     */
    private long[] eval(String s){
        Stack<Point> stack = new Stack<>();
        //cur, pre, ans refers to the current number been editing, the previous number, and the final answer
        long[] cur = new long[]{0,1}, pre = new long[]{0,1}, ans = new long[]{0,1};
        //the sign of the current fraction, where 1 refer to positive number, and -1 refers to negative number.
        long sign = 1;
        //the token between the previous number and the current number, where 1 refers to multiplication, 2 refers to division, 0 refers to there is not previous number
        int state = 0;
        //a string to build the current number
        StringBuilder builder = new StringBuilder();
        //a boolean indicating if there is a abbreviated '*' between a digit and '(', or between ')' and a digit. once such situation detected, add a temporary * sign between them and change the flag.
        boolean flag = false;
        //in order to update the current number one more time to the final answer after the loop, add a fake plus operator.
        s += "+";
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            //case where abbreviated times sign detected
            if(i > 0 && (c == '(' && (Character.isDigit(s.charAt(i - 1)) || s.charAt(i - 1) == '.') || (c == '.' || Character.isDigit(c)) && s.charAt(i - 1) == ')')){
                if(!flag){
                    c = '*';
                    i --;
                }
                flag = !flag;
            }
            //if the character is part of the digit, add to the builder
            if (Character.isDigit(c) || c == '.'){
                builder.append(c);
                //cur[0] = cur[0] * 10 + Long.parseLong("" + c);
            //a special situation where "+" and "-" does not serve as a operator, but a sign instead. For instance, 6 *- 3 or -5+-6
            } else if((c == '+' || c =='-') && (i == 0 || !(Character.isDigit(s.charAt(i - 1)) || s.charAt(i - 1) == '.') && s.charAt(i - 1) != ')')){
                if(c == '-') sign *= -1;
            //Case where a parentheses is met, push the current status to the stack
            } else if(c == '('){
                stack.push(new Point(sign, ans, pre, state));
                ans = new long[]{0,1};
                state = 0;
                sign = 1;
                pre = new long[]{0,1};
            //case where met an arithmetic operator or ')'
            }else{
                //if the builder has something, it is guaranteed that cur is long[]{0,1}, so update the current fraction as the builder.
                if(builder.length() > 0){
                    cur = convertDoubleToFraction(builder.toString());
                    //reset the builder;
                    builder = new StringBuilder();
                }
                //case where the previous token is '*'
                if(state == 1)
                    pre = time(pre, cur);
                //case where the previous token is '/'
                else if(state == 2)
                    pre = divide(pre, cur);
                //case where the previous token in '+-'
                else
                    pre = cur;
                //case where the current is ')'
                if(c == ')') {
                    //first update the current number with the previous number
                    pre = time(pre, new long[]{sign, 1});
                    cur = add(ans, pre);
                    //pop the last status from the stack and recover to the original status
                    Point temp = stack.pop();
                    sign = temp.getSign();
                    ans = temp.getAns();
                    pre = temp.getPre();
                    state = temp.getState();
                //if the current token is in '+-', directly update all stuff to the final answer, since '+-' is operator with least priority
                }else if(c == '-' || c == '+'){
                    pre = time(pre, new long[]{sign, 1});
                    ans = add(ans, pre);
                    state = 0;
                    pre = new long[]{0,1};
                    sign = c == '+' ? 1 : -1;
                //case where current token is in "*/", update the state with corresponding value
                }else{
                    state = c == '*'? 1 : 2;
                }
            }
        }
        //if the denominator is negative, change the sign of both numerator and denominator
        if(ans[1] < 0){
            ans[1] = - ans[1];
            ans[0] = - ans[0];
        }
        return ans;
    }


    /**
     * divide two fraction and return the simplified result
     * @param a the first fraction
     * @param b the second fraction
     * @return the result fraction
     */
    private long[] divide(long[] a, long[] b){
        long n = a[0] * b[1], d = a[1] * b[0];
        return simplifyFraction(n, d);
    }

    /**
     * multiply two fraction and return the simplified result
     * @param a the first fraction
     * @param b the second fraction
     * @return the result fraction
     */
    private long[] time(long[] a, long[] b){
        long n = a[0] * b[0], d = a[1]  *  b[1];
        return simplifyFraction(n, d);
    }

    /**
     * add two fraction and return the simplified result
     * @param a the first fraction
     * @param b the second fraction
     * @return the result fraction
     */
    private long[] add(long[] a, long[] b){
        long n = a[0] * b[1] + b[0] * a[1], d = a[1]  *  b[1];
        return simplifyFraction(n, d);
    }

    /**
     * given a fraction as two numbers, simplify this fraction until the greatest common divisor is 1.
     * @param a the numerator of the fraction
     * @param b the denominator of the fraction
     * @return the simplified fraction
     */
    public long[] simplifyFraction(long a, long b){
        long gcd = gcd(a, b);
        return new long[]{a / gcd, b / gcd};
    }

    /**
     * calculate the greatest common divisor of two number
     * @param a the first number
     * @param b the second number
     * @return the greatest common divisor
     */
    private long gcd(long a, long b){
        return b == 0? a: gcd(b, a % b);
    }

    /**
     * Check if the string is valid to be evaluate as an equation
     * @param s the string
     * @return true if it is valid, otherwise false
     */
    private boolean isValid(String s){
        //check the last character of the expression
        if(s.length() > 0 && ! Character.isDigit(s.charAt(s.length() - 1)) && s.charAt(s.length() - 1) != ')' && s.charAt(s.length() - 1) != '.')
            return false;
        //Check if the parentheses is balanced, left is the number of left parentheses
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
                //'*' and '/' cannot be the first letter
                if(i == 0)
                    return false;
                char pre = s.charAt(i - 1);
                //the previous character cannot be in "(+-*/"
                if(pre == '-' || pre == '+' || pre == '(' || pre == '*' || pre == '/')
                    return false;
            }else if(cur == ')'){
                char pre = s.charAt(i - 1);
                // the previous character must be digit or ')'
                if(!Character.isDigit(pre) && pre != ')' && pre != '.')
                    return false;
            }
        }
        return true;
    }

    /**
     * transform a fraction to a special recurring decimal. For instance, 1/3 = "0.(3)", where the digits in the parentheses is the recurring decimals.
     * for more info, please refer to https://leetcode.com/problems/fraction-to-recurring-decimal/discuss/459008/Python-solution
     * @param numerator the numerator
     * @param denominator the dominator
     * @return a string as the special recurring decimal
     */
    public String fractionToDecimal(long numerator, long denominator) {
        //initialize the result as a string builder
        StringBuilder result = new StringBuilder();
        //record and truncate the sign
        String sign = (numerator < 0 == denominator < 0 || numerator == 0) ? "" : "-";
        long num = Math.abs(numerator);
        long den = Math.abs(denominator);
        //append to the result
        result.append(sign);
        result.append(num / den);
        long remainder = num % den;
        //if there is no remainder, return the result
        if (remainder == 0)
            return result.toString();
        result.append(".");
        //create a hashmap to store the remainders
        HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        //keep doing division until the remainder was seen
        while (!hashMap.containsKey(remainder)) {
            hashMap.put(remainder, result.length());
            result.append(10 * remainder / den);
            remainder = 10 * remainder % den;
        }
        //get the remainder position from the hashmap and update the result
        int index = hashMap.get(remainder);
        //adapt the result to the format
        result.insert(index, "(");
        result.append(")");
        return result.toString().replace("(0)", "");
    }

    /**
     * Convert a string containing a double number to a simplified fraction represented by an array
     * @param s the String
     * @return the array representing the fraction
     */
    public long[] convertDoubleToFraction(String s){
        //parse the double and reformat it with a formal format
        double a = Double.parseDouble(s);
        String newS = Double.toString(a);
        //split the String by the '.'
        String[] fraction = newS.split("\\.");

        //calculate the top and bottom
        long bottom = (new Double(Math.pow(10, fraction[1].length()))).longValue();
        long top = Long.parseLong(fraction[0] + "" + fraction[1]);
        //return the simplified answer
        return simplifyFraction(top, bottom);
    }

    /**
     * Check if the string in the bottom screen is available to be powered or rooted, which means the String must be representing a fraction, recurring decimal or normal decimal.
     * @return true if it can be powered or rooted, otherwise false.
     */
    public boolean allowRootOrPower(){
        String bottom = this.model.getBottom();
        //The String is representing a fraction or recurring decimal
        if(bottom.indexOf('/') != -1 || bottom.indexOf('(') != -1) return true;
        //try to parse the String
        try{
            Double.parseDouble(bottom);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
