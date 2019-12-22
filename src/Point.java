public class Point {
    private long sign;
    private long[] ans, pre;
    private int state;

    protected Point(long sign, long[] ans, long[] pre, int state){
        this.sign = sign;
        this.ans = ans;
        this.pre = pre;
        this.state = state;
    }

    protected long getSign(){
        return this.sign;
    }

    protected long[] getAns(){
        return this.ans;
    }

    protected long[] getPre(){
        return this.pre;
    }

    protected int getState(){
        return this.state;
    }
}
