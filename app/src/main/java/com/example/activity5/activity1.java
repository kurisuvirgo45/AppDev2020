package com.example.activity5;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class activity1 extends BaseActivity {
    EditText etNum1, etNum2;
    Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnRemainder, btnClose;
    TextView tvResult;
    private static int num1, num2, result;
    private static String message;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_activity1;
    }

    @Override
    protected String getActivityName() {
        return "Activity 1";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);
        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnRemainder = findViewById(R.id.btnRemainder);
        btnClose = findViewById(R.id.btnClose);
        tvResult = findViewById(R.id.tvResult);

// actionlisteners
        btnAdd.setOnClickListener(v -> add());
        btnSubtract.setOnClickListener(v -> subtract());
        btnMultiply.setOnClickListener(v -> multiply());
        btnDivide.setOnClickListener(v -> divide());
        btnRemainder.setOnClickListener(v -> remainder());
        btnClose.setOnClickListener(v -> close());
    }

    private void add() {
        num1 = Integer.parseInt(etNum1.getText().toString());
        num2 = Integer.parseInt(etNum2.getText().toString());
        result = num1 + num2;
        message = "The Sum is " + result;
        tvResult.setText(message);
    }

    private void subtract() {
        num1 = Integer.parseInt(etNum1.getText().toString());
        num2 = Integer.parseInt(etNum2.getText().toString());
        result = num1 - num2;
        message = "The Difference is " + result;
        tvResult.setText(message);
    }

    private void multiply() {
        num1 = Integer.parseInt(etNum1.getText().toString());
        num2 = Integer.parseInt(etNum2.getText().toString());
        result = num1 * num2;
        message = "The Product is " + result;
        tvResult.setText(message);
    }

    private void divide() {
        num1 = Integer.parseInt(etNum1.getText().toString());
        num2 = Integer.parseInt(etNum2.getText().toString());
        result = num1 / num2;
        message = "The Qoutient is " + result;
        tvResult.setText(message);
    }

    private void remainder() {
        num1 = Integer.parseInt(etNum1.getText().toString());
        num2 = Integer.parseInt(etNum2.getText().toString());
        result = num1 % num2;
        message = "The Reamainder is " + result;
        tvResult.setText(message);
    }

    private void close() {
        finish();
        System.exit(0);
    }
}