package com.indie.apps.pannypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton imgbtnContact, imgbtnHome, imgbtnCalculator;

    private TextView inputTextView;

    private StringBuilder userInput = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        init();

    }

    void init()
    {
        imgbtnContact = findViewById(R.id.imgbtnContact);
        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnCalculator = findViewById(R.id.imgbtnCalculator);

        imgbtnHome.setSelected(false);
        imgbtnContact.setSelected(false);
        imgbtnCalculator.setSelected(true);


        imgbtnHome.setOnClickListener(this);
        imgbtnContact.setOnClickListener(this);
        imgbtnCalculator.setOnClickListener(this);

        inputTextView = findViewById(R.id.inputTextView);

        // Set click listeners for all buttons
        findViewById(R.id.btn00).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn0).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn1).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn2).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn3).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn4).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn5).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn6).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn7).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn8).setOnClickListener(buttonClickListener);
        findViewById(R.id.btn9).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnDot).setOnClickListener(buttonClickListener);
        findViewById(R.id.btnPlus).setOnClickListener(operatorClickListener);
        findViewById(R.id.btnMinus).setOnClickListener(operatorClickListener);
        findViewById(R.id.btnMult).setOnClickListener(operatorClickListener);
        findViewById(R.id.btnDiv).setOnClickListener(operatorClickListener);
        findViewById(R.id.btnPercent).setOnClickListener(operatorClickListener);

        ImageButton btnAC = findViewById(R.id.btnAC);
        ImageButton btnDel = findViewById(R.id.btnDel);
        ImageButton btnEqual = findViewById(R.id.btnEqual);

        btnAC.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnEqual.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgbtnContact:
                Intent i1 = new Intent(CalculatorActivity.this, ContactActivity.class);
                startActivity(i1);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;

            case R.id.imgbtnHome:
                Intent i = new Intent(CalculatorActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;

            case R.id.imgbtnCalculator:
                break;

            case R.id.btnAC:
                userInput.setLength(0);
                updateTextView();
                break;

            case R.id.btnDel:
                if (userInput.length() > 0) {
                    userInput.deleteCharAt(userInput.length() - 1);
                    updateTextView();
                }
                break;

            case R.id.btnEqual:
                calculateResult();
                break;

        }
    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton button = (ImageButton) v;
            userInput.append(button.getTag().toString());
            updateTextView();
        }
    };

    private View.OnClickListener operatorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton button = (ImageButton) v;
            int length = userInput.length();
            if(button.getTag().toString().equals("-") && length == 0)
            {
                userInput.append(button.getTag().toString());
                updateTextView();
            }else if (length > 0) {
                if(isLastCharacterDigit())
                {
                    calculateResult();
                    userInput.append(button.getTag().toString());
                }else {
                    if(!(length ==1 && userInput.charAt(0) == '-'))
                        userInput.setCharAt(length -1, button.getTag().toString().toCharArray()[0]);
                }

                updateTextView();
            }
        }
    };

    private void updateTextView() {
        inputTextView.setText(userInput.toString());
    }

    private void calculateResult() {
        if (userInput.length() > 0) {
            try {
                String result = Globle.getValue(evaluateExpression(userInput.toString()));
                inputTextView.setText(result);
                userInput.setLength(0);
                userInput.append(result);
            } catch (ArithmeticException | IllegalArgumentException e) {
                //inputTextView.setText("Error");
                updateTextView();
            }
        }
    }

    private double evaluateExpression(String expression) {
        char operator = ' ';
        int length = expression.length();
        char[] arr = expression.toCharArray();
        double operand1 = 0, operand2 = 0;
        int i=0;

        if(length > 0 && arr[0] == '-')
        {
            i = 1;
        }

        // Find the operator and extract the operands
        for (; i < length; i++) {
            char ch = expression.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%') {
                operator = ch;
                operand1 = Double.parseDouble(expression.substring(0, i));
                operand2 = Double.parseDouble(expression.substring(i+1));
                break;
            }
        }
        return applyOperator(operand1, operand2, operator);
    }

    private double applyOperator(double operand1, double operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    return 0;
                }
                return operand1 / operand2;
            case '%':
                return (operand1 * operand2)/100;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private boolean isLastCharacterDigit() {
        if (userInput.length() > 0) {
            char lastChar = userInput.charAt(userInput.length() - 1);
            return Character.isDigit(lastChar) || lastChar == '.';
        }
        return false;
    }
}