package com.example.activity5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends BaseActivity {

    TextView lblWinners, lblPrize;
    EditText txtLotto1, txtLotto2, txtLotto3, txtLotto4, txtLotto5, txtLotto6, txtRandom1, txtRandom2, txtRandom3,
            txtRandom4, txtRandom5, txtRandom6;

    Button btnStart, btnBet;
    Random rnd = new Random();

    int bet[] = new int[6];
    int lotto[] = new int[6];
    EditText[] resultBalls;
    ArrayList<String> LottoBalls;
    InputFilterMinMaxInteger numFilter = new InputFilterMinMaxInteger(1, 58);
    long StartTime;

    Handler myHandler = new Handler();
    int lottoIndex = 0;
    final double jackpot = 50000000;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getActivityName() {
        return "Activity 5";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        //Initialize Lotto Balls
        LottoBalls = new ArrayList<>();


        rnd = new Random();
        lblWinners = findViewById(R.id.lblWinners);
        lblPrize = findViewById(R.id.lblPrize);
        txtLotto1 = findViewById(R.id.txtLotto1);
        txtLotto2 = findViewById(R.id.txtLotto2);
        txtLotto3 = findViewById(R.id.txtLotto3);
        txtLotto4 = findViewById(R.id.txtLotto4);
        txtLotto5 = findViewById(R.id.txtLotto5);
        txtLotto6 = findViewById(R.id.txtLotto6);
        txtRandom1 = findViewById(R.id.txtRandom1);
        txtRandom2 = findViewById(R.id.txtRandom2);
        txtRandom3 = findViewById(R.id.txtRandom3);
        txtRandom4 = findViewById(R.id.txtRandom4);
        txtRandom5 = findViewById(R.id.txtRandom5);
        txtRandom6 = findViewById(R.id.txtRandom6);
        btnBet = findViewById(R.id.btnBet);
        btnStart = findViewById(R.id.btnStart);

        txtLotto1.setFilters(new InputFilter[]{numFilter});
        txtLotto2.setFilters(new InputFilter[]{numFilter});
        txtLotto3.setFilters(new InputFilter[]{numFilter});
        txtLotto4.setFilters(new InputFilter[]{numFilter});
        txtLotto5.setFilters(new InputFilter[]{numFilter});
        txtLotto6.setFilters(new InputFilter[]{numFilter});

        resultBalls = new EditText[]{txtRandom1, txtRandom2, txtRandom3, txtRandom4, txtRandom5, txtRandom6};

        btnBet.setOnClickListener(v -> {
            bet[0] = Integer.parseInt(txtLotto1.getText().toString());
            bet[1] = Integer.parseInt(txtLotto2.getText().toString());
            bet[2] = Integer.parseInt(txtLotto3.getText().toString());
            bet[3] = Integer.parseInt(txtLotto4.getText().toString());
            bet[4] = Integer.parseInt(txtLotto5.getText().toString());
            bet[5] = Integer.parseInt(txtLotto6.getText().toString());
        });
        btnStart.setOnClickListener(v -> {
            if(lottoIndex==0){
                generateLottoBalls();
            }
            StartTime = System.currentTimeMillis();
            btnStart.setEnabled(false);
            setRandomInteger(resultBalls[lottoIndex]);
        });

    }

    private void setRandomInteger(EditText et) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - StartTime < 2000) {
                    myHandler.postDelayed(this, 100);
                    et.setText(LottoBalls.get(randomNumber(0, LottoBalls.size() - 1)));
                } else {
                    myHandler.removeCallbacks(this);
                    removeLottoBall(et);
                    lottoIndex++;
                    processResults();
                }
            }
        };
        myHandler.post(run);
    }

    private int randomNumber(int min, int max) {
        return rnd.nextInt(max + 1 - min) + min;
    }

    private void generateLottoBalls() {
        LottoBalls.clear();
        for (int i = 1; i <= 58; i++) {
            LottoBalls.add(i + "");
        }

    }

    private void removeLottoBall(EditText et) {
        String lottoNumber = et.getText().toString();
        for (int i = 0; i < LottoBalls.size(); i++) {
            if (LottoBalls.get(i).equals(lottoNumber)) {
                LottoBalls.remove(i);
                return;
            }
        }
    }

    private void processResults() {
        //Toast.makeText(getApplicationContext(),"Hellogit",Toast.LENGTH_LONG).show();
        btnStart.setEnabled(true);
        if (lottoIndex < resultBalls.length) {
            btnStart.performClick();
        } else {
            lottoIndex = 0;
            //Get lotto results
            lotto[0] = Integer.parseInt(txtRandom1.getText().toString());
            lotto[1] = Integer.parseInt(txtRandom2.getText().toString());
            lotto[2] = Integer.parseInt(txtRandom3.getText().toString());
            lotto[3] = Integer.parseInt(txtRandom4.getText().toString());
            lotto[4] = Integer.parseInt(txtRandom5.getText().toString());
            lotto[5] = Integer.parseInt(txtRandom6.getText().toString());

            //Count number of match numbers
            int count = 0;
            for (int i = 0; i < bet.length; i++) {

                for (int j = 0; j < lotto.length; j++) {
                    if (bet[i] == lotto[j]) {
                        count++;
                    }
                }
            }
            int winners = randomNumber(0, 5) + (count > 1 ? 1 : 0);
            lblWinners.setText("WINNERS: " + winners);
            String message = "";
            switch (count) {
                case 6:
                    message = String.format("YOU WON %,3.2f", jackpot / winners);
                    break;
                case 5:
                    message = String.format("YOU WON %,3.2f", jackpot * 0.5 / winners);
                    break;
                case 4:
                    message = String.format("YOU WON %,3.2f", jackpot * .2 / winners);
                    break;
                case 3:
                    message = String.format("YOU WON %,3.2f", 5000);
                    break;
                case 2:
                    message = "Play again with 5 lotto panels fixed!";
                    break;
                default:
                    message = "Sorry, you did not win...";
            }
            lblPrize.setText(message);
        }
    }



}