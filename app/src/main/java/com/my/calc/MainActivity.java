package com.my.calc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    TextView ValueHistoryTextView, ValueTextView;
    ScrollView scrollView;
    boolean ResultIsRemove = true;
    BigDecimal Operand1, Operand2, Result;
    MathContext mathContext = new MathContext(17, RoundingMode.HALF_UP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ValueHistoryTextView = findViewById(R.id.ValueHistoryTextView);
        ValueTextView = findViewById(R.id.ValueTextView);
        scrollView = findViewById(R.id.scrollView);
    }

    private void scrollToDown() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);
    }

    public void onClickButtonOperation(View view) {
        if (ValueHistoryTextView.getText().toString().endsWith("/") ||
                ValueHistoryTextView.getText().toString().endsWith("*") ||
                ValueHistoryTextView.getText().toString().endsWith("-") ||
                ValueHistoryTextView.getText().toString().endsWith("+"))
            ValueHistoryTextView.setText(ValueHistoryTextView.getText().toString().substring(0, ValueHistoryTextView.length() - 1) + ((TextView) view).getText());
        else {
            Operand1 = BigDecimal.valueOf(Double.valueOf(ValueTextView.getText().toString()));
            if (ValueHistoryTextView.length() != 0)
                ValueHistoryTextView.append("\n\n");
            ValueHistoryTextView.append(ValueTextView.getText() + " " + ((TextView) view).getText());
            ValueTextView.setText("0");
            ResultIsRemove = true;
            scrollToDown();
        }
    }

    public void onClickButtonExec(View view) {
        if (ValueHistoryTextView.getText().toString().endsWith("/") ||
                ValueHistoryTextView.getText().toString().endsWith("*") ||
                ValueHistoryTextView.getText().toString().endsWith("-") ||
                ValueHistoryTextView.getText().toString().endsWith("+")) {
            Operand2 = BigDecimal.valueOf(Double.valueOf(ValueTextView.getText().toString()));
            try {
                switch (ValueHistoryTextView.getText().toString().charAt(ValueHistoryTextView.length() - 1)) {
                    case '/':
                        Result = Operand1.divide(Operand2, mathContext);
                        break;
                    case '*':
                        Result = Operand1.multiply(Operand2, mathContext);
                        break;
                    case '-':
                        Result = Operand1.subtract(Operand2, mathContext);
                        break;
                    case '+':
                        Result = Operand1.add(Operand2, mathContext);
                        break;
                }
            } catch (ArithmeticException AE) {
                ValueHistoryTextView.append("\n" + ValueTextView.getText() + " =\nInfinite");
                ValueTextView.setText("0");
                ResultIsRemove = false;
                scrollToDown();
                return;
            }
            if (Result.doubleValue() == Double.POSITIVE_INFINITY || Result.doubleValue() == Double.NEGATIVE_INFINITY) {
                ValueHistoryTextView.append("\n" + ValueTextView.getText() + " =\nInfinite");
                ValueTextView.setText("0");
            } else {
                ValueHistoryTextView.append("\n" + ValueTextView.getText() + " =\n" + String.valueOf(Result));
                ValueTextView.setText(String.valueOf(Result));
            }
            ResultIsRemove = false;
            scrollToDown();
        }
    }

    public void onClickButtonCE(View view) {
        ValueHistoryTextView.setText("");
        ValueTextView.setText("0");
        ResultIsRemove = true;
    }

    public void onClickButtonC(View view) {
        ValueTextView.setText("0");
        ResultIsRemove = true;
    }

    public void onClickButtonNumber(View view) {
        if (ResultIsRemove) {
            if (ValueTextView.getText().length() < 18 + ValueTextView.getText().toString().indexOf("-"))
                if (!ValueTextView.getText().toString().equals("0"))
                    ValueTextView.setText(ValueTextView.getText().toString() + ((TextView) view).getText());
                else
                    ValueTextView.setText(((TextView) view).getText());
        } else {
            ValueTextView.setText(((TextView) view).getText());
            ResultIsRemove = true;
        }
    }

    public void onClickButtonPoint(View view) {
        if (ResultIsRemove) {
            if (!ValueTextView.getText().toString().contains("."))
                ValueTextView.setText(ValueTextView.getText().toString() + ".");
        } else {
            ValueTextView.setText("0.");
            ResultIsRemove = true;
        }
    }

    public void onClickButtonNeg(View view) {
        if (ValueTextView.getText().toString().startsWith("-"))
            ValueTextView.setText(ValueTextView.getText().toString().substring(1));
        else if (!ValueTextView.getText().toString().equals("0"))
            ValueTextView.setText("-" + ValueTextView.getText());
    }

    public void onClickButtonDel(View view) {
        if (ResultIsRemove) {
            ValueTextView.setText(ValueTextView.getText().toString().substring(0, ValueTextView.length() - 1));
            if (ValueTextView.length() == 0 || ValueTextView.getText().toString().equals("-"))
                ValueTextView.setText("0");
        } else {
            ValueTextView.setText("0");
            ResultIsRemove = true;
        }
    }
}
