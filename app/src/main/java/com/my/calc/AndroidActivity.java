package com.my.calc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AndroidActivity extends AppCompatActivity {

    TextView valueHistoryTextView;
    TextView valueTextView;
    ScrollView historyScrollView;
    boolean resultIsRemove = true;
    BigDecimal operandOne;
    BigDecimal operandTwo;
    BigDecimal result;
    MathContext mathContext = new MathContext(17, RoundingMode.HALF_UP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        valueHistoryTextView = findViewById(R.id.value_history_text_view);
        valueTextView = findViewById(R.id.value_text_view);
        historyScrollView = findViewById(R.id.history_scroll_view);
    }

    public void onClickButtonOperation(View view) {
        if (valueHistoryTextView.getText().toString().endsWith("/")
                || valueHistoryTextView.getText().toString().endsWith("*")
                || valueHistoryTextView.getText().toString().endsWith("-")
                || valueHistoryTextView.getText().toString().endsWith("+")) {
            valueHistoryTextView.setText(valueHistoryTextView.getText().toString().substring(0, valueHistoryTextView.length() - 1) + ((TextView) view).getText());
        } else {
            operandOne = BigDecimal.valueOf(Double.valueOf(valueTextView.getText().toString()));
            if (valueHistoryTextView.length() != 0) {
                valueHistoryTextView.append("\n\n");
            }
            valueHistoryTextView.append(valueTextView.getText() + " " + ((TextView) view).getText());
            valueTextView.setText("0");
            resultIsRemove = true;
            scrollHistoryToDown();
        }
    }

    public void onClickButtonExec(View view) {
        if (valueHistoryTextView.getText().toString().endsWith("/")
                || valueHistoryTextView.getText().toString().endsWith("*")
                || valueHistoryTextView.getText().toString().endsWith("-")
                || valueHistoryTextView.getText().toString().endsWith("+")) {
            operandTwo = BigDecimal.valueOf(Double.valueOf(valueTextView.getText().toString()));
            try {
                switch (valueHistoryTextView.getText().toString().charAt(valueHistoryTextView.length() - 1)) {
                    case '/':
                        result = operandOne.divide(operandTwo, mathContext);

                        break;
                    case '*':
                        result = operandOne.multiply(operandTwo, mathContext);

                        break;
                    case '-':
                        result = operandOne.subtract(operandTwo, mathContext);

                        break;
                    case '+':
                        result = operandOne.add(operandTwo, mathContext);

                        break;
                }
            } catch (ArithmeticException AE) {
                valueHistoryTextView.append("\n" + valueTextView.getText() + " =\nInfinite");
                valueTextView.setText("0");
                resultIsRemove = false;
                scrollHistoryToDown();

                return;
            }
            if (result.doubleValue() == Double.POSITIVE_INFINITY || result.doubleValue() == Double.NEGATIVE_INFINITY) {
                valueHistoryTextView.append("\n" + valueTextView.getText() + " =\nInfinite");
                valueTextView.setText("0");
            } else {
                valueHistoryTextView.append("\n" + valueTextView.getText() + " =\n" + String.valueOf(result));
                valueTextView.setText(String.valueOf(result));
            }
            resultIsRemove = false;
            scrollHistoryToDown();
        }
    }

    public void onClickCEButton(View view) {
        valueHistoryTextView.setText("");
        valueTextView.setText("0");
        resultIsRemove = true;
    }

    public void onClickCButton(View view) {
        valueTextView.setText("0");
        resultIsRemove = true;
    }

    public void onClickNumberButton(View view) {
        if (resultIsRemove) {
            if (valueTextView.getText().length() < 18 + valueTextView.getText().toString().indexOf("-"))
                if (!valueTextView.getText().toString().equals("0"))
                    valueTextView.setText(valueTextView.getText().toString() + ((TextView) view).getText());
                else
                    valueTextView.setText(((TextView) view).getText());
        } else {
            valueTextView.setText(((TextView) view).getText());
            resultIsRemove = true;
        }
    }

    public void onClickPointButton(View view) {
        if (resultIsRemove) {
            if (!valueTextView.getText().toString().contains("."))
                valueTextView.setText(valueTextView.getText().toString() + ".");
        } else {
            valueTextView.setText("0.");
            resultIsRemove = true;
        }
    }

    public void onClickNegButton(View view) {
        if (valueTextView.getText().toString().startsWith("-")) {
            valueTextView.setText(valueTextView.getText().toString().substring(1));
        } else if (!valueTextView.getText().toString().equals("0")) {
            valueTextView.setText("-" + valueTextView.getText());
        }
    }

    public void onClickDelButton(View view) {
        if (resultIsRemove) {
            valueTextView.setText(valueTextView.getText().toString().substring(0, valueTextView.length() - 1));

            if (valueTextView.length() == 0 || valueTextView.getText().toString().equals("-")) {
                valueTextView.setText("0");
            }
        } else {
            valueTextView.setText("0");
            resultIsRemove = true;
        }
    }

    private void scrollHistoryToDown() {
        historyScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                historyScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);
    }
}
