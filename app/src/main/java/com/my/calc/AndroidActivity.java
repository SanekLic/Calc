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

    public static final String NEGATIVE = "-";
    public static final String NUMBER_ZERO = "0";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    public static final String INFINITE = " =\nInfinite";
    public static final String POINT = ".";
    public static final String EMPTY = "";
    public static final String EQUALS = " =";
    private static final int CALCULATION_PRECISION = 17;
    private static final char OPERATION_DEL = '/';
    private static final char OPERATION_MUL = '*';
    private static final char OPERATION_SUB = '-';
    private static final char OPERATION_ADD = '+';
    private TextView valueHistoryTextView;
    private TextView valueTextView;
    private ScrollView historyScrollView;
    private boolean resultIsRemove = true;
    private BigDecimal operandOne;
    private BigDecimal operandTwo;
    private BigDecimal result;
    private MathContext mathContext = new MathContext(CALCULATION_PRECISION, RoundingMode.HALF_UP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        valueHistoryTextView = findViewById(R.id.value_history_text_view);
        valueTextView = findViewById(R.id.value_text_view);
        historyScrollView = findViewById(R.id.history_scroll_view);
    }

    public void onClickButtonOperation(View view) {
        if (isLastOperationAlreadySelected()) {
            valueHistoryTextView.setText(new StringBuilder()
                    .append(valueHistoryTextView.getText().toString().substring(0, valueHistoryTextView.length() - 1))
                    .append(((TextView) view).getText()).toString());
        } else {
            double operandOneDouble = Double.parseDouble(valueTextView.getText().toString());
            operandOne = BigDecimal.valueOf(operandOneDouble);

            if (valueHistoryTextView.length() != 0) {
                valueHistoryTextView.append(new StringBuilder()
                        .append(NEW_LINE)
                        .append(NEW_LINE).toString());
            }

            valueHistoryTextView.append(new StringBuilder()
                    .append(valueTextView.getText())
                    .append(SPACE)
                    .append(((TextView) view).getText()).toString());
            valueTextView.setText(NUMBER_ZERO);
            resultIsRemove = true;
            scrollHistoryToDown();
        }
    }

    public void onClickButtonExec(View view) {
        if (isLastOperationAlreadySelected()) {
            double operandTwoDouble = Double.parseDouble(valueTextView.getText().toString());
            operandTwo = BigDecimal.valueOf(operandTwoDouble);
            try {
                switch (valueHistoryTextView.getText().toString().charAt(valueHistoryTextView.length() - 1)) {
                    case OPERATION_DEL:
                        result = operandOne.divide(operandTwo, mathContext);

                        break;
                    case OPERATION_MUL:
                        result = operandOne.multiply(operandTwo, mathContext);

                        break;
                    case OPERATION_SUB:
                        result = operandOne.subtract(operandTwo, mathContext);

                        break;
                    case OPERATION_ADD:
                        result = operandOne.add(operandTwo, mathContext);

                        break;
                }
            } catch (ArithmeticException AE) {
                addInfiniteResultToHistory();
                resultIsRemove = false;
                scrollHistoryToDown();

                return;
            }

            validateResult();
            resultIsRemove = false;
            scrollHistoryToDown();
        }
    }

    private void validateResult() {
        if (isResultInfinite()) {
            addInfiniteResultToHistory();
        } else {
            valueHistoryTextView.append(new StringBuilder()
                    .append(NEW_LINE)
                    .append(valueTextView.getText())
                    .append(EQUALS)
                    .append(NEW_LINE)
                    .append(String.valueOf(result)).toString());
            valueTextView.setText(String.valueOf(result));
        }
    }

    private void addInfiniteResultToHistory() {
        valueHistoryTextView.append(new StringBuilder()
                .append(NEW_LINE)
                .append(valueTextView.getText())
                .append(INFINITE).toString());
        valueTextView.setText(NUMBER_ZERO);
    }

    private boolean isResultInfinite() {
        return result.doubleValue() == Double.POSITIVE_INFINITY || result.doubleValue() == Double.NEGATIVE_INFINITY;
    }

    private boolean isLastOperationAlreadySelected() {
        return valueHistoryTextView.getText().toString().endsWith(String.valueOf(OPERATION_DEL))
                || valueHistoryTextView.getText().toString().endsWith(String.valueOf(OPERATION_MUL))
                || valueHistoryTextView.getText().toString().endsWith(String.valueOf(OPERATION_SUB))
                || valueHistoryTextView.getText().toString().endsWith(String.valueOf(OPERATION_ADD));
    }

    public void onClickCEButton(View view) {
        valueHistoryTextView.setText(EMPTY);
        valueTextView.setText(NUMBER_ZERO);
        resultIsRemove = true;
    }

    public void onClickCButton(View view) {
        valueTextView.setText(NUMBER_ZERO);
        resultIsRemove = true;
    }

    public void onClickNumberButton(View view) {
        if (resultIsRemove) {
            if (valueTextView.getText().length() < 18 + valueTextView.getText().toString().indexOf(NEGATIVE))
                if (!valueTextView.getText().toString().equals(NUMBER_ZERO)) {
                    valueTextView.setText(new StringBuilder()
                            .append(valueTextView.getText().toString())
                            .append(((TextView) view).getText()).toString());
                } else {
                    valueTextView.setText(((TextView) view).getText());
                }
        } else {
            valueTextView.setText(((TextView) view).getText());
            resultIsRemove = true;
        }
    }

    public void onClickPointButton(View view) {
        if (resultIsRemove) {
            if (!valueTextView.getText().toString().contains(POINT))
                valueTextView.setText(new StringBuilder()
                        .append(valueTextView.getText().toString())
                        .append(POINT).toString());
        } else {
            valueTextView.setText(new StringBuilder()
                    .append(NUMBER_ZERO)
                    .append(POINT).toString());
            resultIsRemove = true;
        }
    }

    public void onClickNegButton(View view) {
        if (valueTextView.getText().toString().startsWith(NEGATIVE)) {
            valueTextView.setText(valueTextView.getText().toString().substring(1));
        } else if (!valueTextView.getText().toString().equals(NUMBER_ZERO)) {
            valueTextView.setText(new StringBuilder()
                    .append(NEGATIVE)
                    .append(valueTextView.getText()).toString());
        }
    }

    public void onClickDelButton(View view) {
        if (resultIsRemove) {
            valueTextView.setText(valueTextView.getText().toString().substring(0, valueTextView.length() - 1));

            if (valueTextView.length() == 0 || valueTextView.getText().toString().equals(NEGATIVE)) {
                valueTextView.setText(NUMBER_ZERO);
            }
        } else {
            valueTextView.setText(NUMBER_ZERO);
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
