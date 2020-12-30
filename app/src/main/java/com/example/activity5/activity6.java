package com.example.activity5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity6 extends BaseActivity {

    EditText txtPlateNumber;
    Button btnCheck, btnClear, btnClose;
    private static final  String new4Wheel = "^[a-zA-Z]{3}[- ]*\\d{4}";
    private static final  String new2Wheel = "^[a-zA-Z]{2}[- ]*\\d{5}";
    private static final String old4Wheel = "^[a-zA-Z]{3}[- ]*\\d{3}";
    private static final String old2Wheel1 = "^[a-zA-Z]{2}[- ]*\\d{4}";
    private static final String old2Wheel2 = "^\\d{4}[- ]*[a-zA-Z]{2}";
    private static final String specialVehicle = "^[a-zA-Z]{3}[- ]*\\d{2}";
    // Dialog Box
    DialogWith2Buttons dialogResult;
    // For Fine Computation Variables
    private SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
    private String vehicleType;
    private Calendar regDeadline;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_activity6;
    }

    @Override
    protected String getActivityName() {
        return "Activity 6" ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_activity6);

        // Initialize
        txtPlateNumber = findViewById(R.id.txtPlateNumber);
        btnCheck = findViewById(R.id.btnCheck);
        btnClear = findViewById(R.id.btnClear);
        btnClose = findViewById(R.id.btnClose);

        // Initialize Dialog
        dialogResult = new DialogWith2Buttons("About vehicle", "OK", "Clear Plate", activity6.this) {
            @Override
            public void onPostiveClick() { }

            @Override
            public void onNegativeClick() {
                btnClear.performClick();
            }
        };

        // Adding Filter to Plate Number
        btnClear.setOnClickListener(v -> txtPlateNumber.setText(""));
        btnClose.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
        btnCheck.setOnClickListener(v -> checkPlateNumber());
    }

    private void checkPlateNumber(){
        String plate = txtPlateNumber.getText().toString().trim();
        if(matchesAnyPattern(plate)){
            vehicleType = getVehicleType(plate);
            regDeadline = Calendar.getInstance();
            String regRange = getRegistrationDate(plate);
            String fine = getFine();
            String message = String.format("Plate Number:\n%s\n\nPlate Type:\n%s\n\nVehicle Type:\n%s\n\nRegistration Period:\n%s\n\nLate Fees:\n%s", plate, getPlateType(plate), vehicleType, regRange, fine);
            dialogResult.setMessage(message);
            dialogResult.openDialog();
        }else{
            MsgBox("Plate number provided does not match any of the patterns.");
        }
        hideSoftKeyboard(this);
    }

    private boolean matchesAnyPattern(String plate){
        Pattern validPattern = Pattern.compile(new4Wheel+"|"+new2Wheel+"|"+old4Wheel+"|"+old2Wheel1+"|"+old2Wheel2+"|"+specialVehicle);
        return validPattern.matcher(plate).matches();
    }

    private String getPlateType(String plate) {
        Pattern newPattern = Pattern.compile(new4Wheel+"|"+new2Wheel);
        Pattern oldPattern = Pattern.compile(old4Wheel+"|"+old2Wheel1+"|"+old2Wheel2);
        Pattern specialPattern = Pattern.compile(specialVehicle);
        if(newPattern.matcher(plate).matches()) return "New Plate Number";
        else if(oldPattern.matcher(plate).matches()) return "Old Plate Number";
        else if(specialPattern.matcher(plate).matches()) return "Special Plate Number";
        else return "";
    }

    private String getVehicleType(String plate) {
        Pattern fourWheelPattern = Pattern.compile(new4Wheel+"|"+old4Wheel);
        Pattern twoWheelPattern = Pattern.compile(new2Wheel+"|"+old2Wheel1+"|"+old2Wheel2);
        Pattern specialPattern = Pattern.compile(specialVehicle);
        if(fourWheelPattern.matcher(plate).matches()) return "4-Wheeled";
        else if(twoWheelPattern.matcher(plate).matches()) return "2-Wheeled";
        else if(specialPattern.matcher(plate).matches()) return "Special Motor Vehicle";
        else return "";
    }

    private String getRegistrationDate(String plate) {
        Pattern numbersPattern = Pattern.compile("\\d+");
        Matcher matcher = numbersPattern.matcher(plate);
        String numericPart = "", monthName = "", dateRange = "";
        if(matcher.find()){
            numericPart = matcher.group();
            int monthNum = Integer.parseInt(numericPart.charAt(numericPart.length()-1)+"");
            monthNum = monthNum == 0 ? 10 : monthNum;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthNum);
            monthName = new SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.getTime());
            int dayNum = Integer.parseInt(numericPart.charAt(numericPart.length()-2)+"");
            int lastDay = 0;
            if(dayNum >= 1 && dayNum <= 3) {
                lastDay = 7;
                dateRange = "1st and 7th working day";
            }
            else if(dayNum >= 4 && dayNum <= 6) {
                lastDay = 14;
                dateRange = "8th and 14th working day";
            }
            else if(dayNum >= 7 && dayNum <= 8) {
                lastDay = 21;
                dateRange = "15th and 21st working day";
            }
            else {
                lastDay = getLastDayOfMonth(monthNum);
                dateRange = "22nd and last working day";
            }
            regDeadline.set(getYear(), monthNum-1, lastDay);
        }
        return "On or before "+monthName+", between "+dateRange;
    }

    private String getFine(){
        double fine = 0;
        Calendar today = Calendar.getInstance();
        int monthsDiff = today.get(Calendar.MONTH)
                - regDeadline.get(Calendar.MONTH);
        int daysDiff = today.get(Calendar.DATE)
                - regDeadline.get(Calendar.DATE);
        monthsDiff = monthsDiff != 0 ? monthsDiff : daysDiff <= 0 ? 0 : 1;
        if (monthsDiff <= 0){
            fine = 0;
        }
        else if(monthsDiff > 5){
            fine = 100000;
        }else{
            fine = monthsDiff;
            switch (vehicleType){
                case "2-Wheeled":
                    fine *= 1500;
                    break;
                case "4-Wheeled":
                    fine *= 3000;
                    break;
                default:
                    fine *= 0;
            }
        }
        if(fine == 0){
            return "0.00";
        }else{
            return String.format("%,3.2f [%s late]", fine, (monthsDiff != 0 ? String.format("%d months", monthsDiff) : String.format("%d days", daysDiff)));
        }
    }

    private int getLastDayOfMonth(int month){
        try{
            Date date = sdf.parse(String.format("%d-22-%d", month, getYear()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getActualMaximum(Calendar.DATE);
        }catch (Exception e){
            return 0;
        }
    }

    private int getYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}


