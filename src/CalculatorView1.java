//import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CalculatorView1 extends JFrame implements CalculatorView, KeyListener {

    private CalculatorController controller;

    private final JTextArea tTop, tBottom;

    private final JButton bClear, bEnter, bDelete, bAdd, bSubtract, bMultiply,
            bDivide, bPower, bRoot, bLeftParen, bRightParen, bLeftArrow, bRightArrow, bTran, bDot;

    private static final int TEXT_AREA_HEIGHT = 5, TEXT_AREA_WIDTH = 20,
            DIGIT_BUTTONS = 10, MAIN_BUTTON_PANEL_GRID_ROWS = 5,
            MAIN_BUTTON_PANEL_GRID_COLUMNS = 4, SIDE_BUTTON_PANEL_GRID_ROWS = 5,
            SIDE_BUTTON_PANEL_GRID_COLUMNS = 1, CALC_GRID_ROWS = 3,
            CALC_GRID_COLUMNS = 1;
    /**
     * Digit entry buttons.
     */
    private final JButton[] bDigits;

    public CalculatorView1(){
        super("Merciless's Calculator");


        this.tTop = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        //this.tTop.addKeyListener(this);
        this.tBottom = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.bClear = new JButton("Clear");
        this.bEnter = new JButton("Enter");
        this.bAdd = new JButton("+");
        this.bSubtract = new JButton("-");
        this.bMultiply = new JButton("*");
        this.bDivide = new JButton("/");
        this.bPower = new JButton("^");
        this.bRoot = new JButton("sqrt");
        this.bLeftParen = new JButton("(");
        this.bRightParen = new JButton(")");
        this.bLeftArrow = new JButton("<-");
        this.bRightArrow = new JButton("->");
        this.bTran = new JButton("d<->f");
        this.bDelete = new JButton("Delete");
        this.bDigits = new JButton[10];
        this.bDot = new JButton(".");
        for (int i = 0; i < 10; ++i) {
            this.bDigits[i] = new JButton("" + i);
        }

        this.tTop.setEditable(false);
        this.tTop.setLineWrap(true);
        this.tTop.setWrapStyleWord(true);
        this.tBottom.setEditable(false);
        this.tBottom.setLineWrap(true);
        this.tBottom.setWrapStyleWord(true);
        this.bRoot.setEnabled(false);
        this.bPower.setEnabled(false);
        this.bTran.setEnabled(false);

        JScrollPane tTopScrollPane = new JScrollPane(this.tTop);
        JScrollPane tBottomScrollPane = new JScrollPane(this.tBottom);

        JPanel mainButtonPanel = new JPanel(new GridLayout(
                MAIN_BUTTON_PANEL_GRID_ROWS, MAIN_BUTTON_PANEL_GRID_COLUMNS));

        mainButtonPanel.add(this.bDigits[7]);
        mainButtonPanel.add(this.bDigits[8]);
        mainButtonPanel.add(this.bDigits[9]);
        mainButtonPanel.add(this.bAdd);
        mainButtonPanel.add(this.bDigits[4]);
        mainButtonPanel.add(this.bDigits[5]);
        mainButtonPanel.add(this.bDigits[6]);
        mainButtonPanel.add(this.bSubtract);
        mainButtonPanel.add(this.bDigits[1]);
        mainButtonPanel.add(this.bDigits[2]);
        mainButtonPanel.add(this.bDigits[3]);
        mainButtonPanel.add(this.bMultiply);
        mainButtonPanel.add(this.bDigits[0]);
        mainButtonPanel.add(this.bPower);
        mainButtonPanel.add(this.bRoot);
        mainButtonPanel.add(this.bDivide);
        mainButtonPanel.add(this.bLeftParen);
        mainButtonPanel.add(this.bRightParen);
        mainButtonPanel.add(this.bLeftArrow);
        mainButtonPanel.add(this.bRightArrow);


        JPanel sideButtonPanel = new JPanel(new GridLayout(
                SIDE_BUTTON_PANEL_GRID_ROWS, SIDE_BUTTON_PANEL_GRID_COLUMNS));

        sideButtonPanel.add(this.bClear);
        sideButtonPanel.add(this.bEnter);
        sideButtonPanel.add(this.bDelete);
        sideButtonPanel.add(this.bTran);
        sideButtonPanel.add(this.bDot);

        JPanel combinedButtonPanel = new JPanel(new FlowLayout());


        combinedButtonPanel.add(mainButtonPanel);
        combinedButtonPanel.add(sideButtonPanel);

        this.setLayout(new GridLayout(CALC_GRID_ROWS, CALC_GRID_COLUMNS));
        this.add(tTopScrollPane);
        this.add(tBottomScrollPane);
        this.add(combinedButtonPanel);

        for (JButton b : this.bDigits) {
            b.addActionListener(this);
            b.addKeyListener(this);
        }
        this.bClear.addActionListener(this);
        this.bTran.addActionListener(this);
        this.bEnter.addActionListener(this);
        this.bAdd.addActionListener(this);
        this.bSubtract.addActionListener(this);
        this.bMultiply.addActionListener(this);
        this.bDivide.addActionListener(this);
        this.bPower.addActionListener(this);
        this.bRoot.addActionListener(this);
        this.bDelete.addActionListener(this);
        this.bLeftParen.addActionListener(this);
        this.bRightParen.addActionListener(this);
        this.bLeftArrow.addActionListener(this);
        this.bRightArrow.addActionListener(this);
        this.bDot.addActionListener(this);
        this.tTop.addKeyListener(this);
        this.tBottom.addKeyListener(this);
        this.bClear.addKeyListener(this);
        this.bTran.addKeyListener(this);
        this.bEnter.addKeyListener(this);
        this.bAdd.addKeyListener(this);
        this.bSubtract.addKeyListener(this);
        this.bMultiply.addKeyListener(this);
        this.bDivide.addKeyListener(this);
        this.bPower.addKeyListener(this);
        this.bRoot.addKeyListener(this);
        this.bDelete.addKeyListener(this);
        this.bLeftParen.addKeyListener(this);
        this.bRightParen.addKeyListener(this);
        this.bLeftArrow.addKeyListener(this);
        this.bRightArrow.addKeyListener(this);
        this.bDot.addKeyListener(this);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void registerObserver(CalculatorController controller) {
        this.controller = controller;
    }

    @Override
    public void updateTopDisplay(String s) {
        this.tTop.setText(s);
    }

    @Override
    public void updateBottomDisplay(String s) {
        this.tBottom.setText(s);
    }

    @Override
    public void updateTranAllowed(boolean allowed) {
        this.bTran.setEnabled(allowed);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.bClear) {
            this.controller.processClearEvent();
        } else if (source == this.bAdd) {
            this.controller.processEditEvent('+');
        } else if (source == this.bSubtract) {
            this.controller.processEditEvent('-');
        } else if (source == this.bMultiply) {
            this.controller.processEditEvent('*');
        } else if (source == this.bDivide) {
            this.controller.processEditEvent('/');
        } else if (source == this.bLeftParen) {
            this.controller.processEditEvent('(');
        } else if (source == this.bRightParen) {
            this.controller.processEditEvent(')');
        } else if (source == this.bDot) {
            this.controller.processEditEvent('.');
        } else if (source == this.bLeftArrow) {
            this.controller.processShiftEvent(-1);
        } else if (source == this.bRightArrow) {
            this.controller.processShiftEvent(1);
        } else if (source == this.bEnter) {
            this.controller.processEnterEvent();
        }else if (source == this.bDelete){
            this.controller.processDeleteEvent();
        }else if(source == this.bTran){
            this.controller.processTranEvent();
        }else{
            for(int i = 0; i < DIGIT_BUTTONS; ++i){
                if(source == this.bDigits[i]){
                    this.controller.processEditEvent((char)(i + '0'));
                    break;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT){
            this.controller.processShiftEvent(-1);
        }else if(key == KeyEvent.VK_RIGHT){
            this.controller.processShiftEvent(1);
        }else if(key == KeyEvent.VK_ENTER){
            this.controller.processEnterEvent();
        }else if(key == KeyEvent.VK_MINUS){
            this.controller.processEditEvent('-');
        }else if(key == KeyEvent.VK_EQUALS && e.isShiftDown()){
            this.controller.processEditEvent('+');
        }else if(key == KeyEvent.VK_SLASH){
            this.controller.processEditEvent('/');
        }else if(key == KeyEvent.VK_8 && e.isShiftDown()){
            this.controller.processEditEvent('*');
        }else if(key == KeyEvent.VK_9 && e.isShiftDown()){
            this.controller.processEditEvent('(');
        }else if(key == KeyEvent.VK_0 && e.isShiftDown()){
            this.controller.processEditEvent(')');
        } else if(key == KeyEvent.VK_BACK_SPACE){
            this.controller.processDeleteEvent();
        }else if(key == KeyEvent.VK_PERIOD){
            this.controller.processEditEvent('.');
        }else if(key == KeyEvent.VK_C && !e.isControlDown()){
            //this.controller.processClearEvent();
        }else if(key == KeyEvent.VK_T && this.bTran.isEnabled()){
            this.controller.processTranEvent();
        }else{
            for(int i = 0; i < DIGIT_BUTTONS; i++){
                if(key == i + KeyEvent.VK_0){
                    this.controller.processEditEvent((char)(i + '0'));
                    break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
