package com.example.activity5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Toolbar toolbar = null;
    private TextView lblAppName = null;
    private Spinner spinActivities = null;
    private Button btnLaunch = null;
    private EditText txtWebsite = null;
    private String activityName = null;
    public WebView viewWebsite = null;
    private Toolbar.LayoutParams lp = null;
    protected abstract int getLayoutResId();
    protected abstract String getActivityName();
    LinearLayout.LayoutParams wrapContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams spinWeight = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams btnWeight = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        activityName = getActivityName();

        // Instantiate Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate Views
        lblAppName = findViewById(R.id.lblAppName);
        spinActivities = findViewById(R.id.spinActivities);
        txtWebsite = findViewById(R.id.txtWebsite);
        btnLaunch = findViewById(R.id.btnLaunch);

        // Add Listeners, etc.
        prepareLaunchButton();
        prepareSearchEditText();
        prepareActivitySpinner();
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout linearLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        FrameLayout activityContainer = linearLayout.findViewById(R.id.layout_container);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(linearLayout);
    }

    private void prepareLaunchButton(){
        if(btnLaunch == null) return;
        btnLaunch.setText("Launch");
        btnLaunch.setOnClickListener(v -> launchOrVisit());
    }

    private void prepareSearchEditText(){
        if(txtWebsite == null) return;
        txtWebsite.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        // Update UI based on current activity
        if(activityName.equals("Others")) {
            lblAppName.setVisibility(View.GONE);
            txtWebsite.setVisibility(View.VISIBLE);
            btnLaunch.setText("Visit");
            // Update UI
            spinWeight.weight = 1.5f;
            spinActivities.setLayoutParams(spinWeight);
            btnWeight.weight = 1;
            btnLaunch.setLayoutParams(btnWeight);
        }
        else {
            lblAppName.setVisibility(View.VISIBLE);
            txtWebsite.setVisibility(View.GONE);
            btnLaunch.setText("Launch");
            // Update UI
            spinActivities.setLayoutParams(wrapContent);
            btnLaunch.setLayoutParams(wrapContent);
        }
        // Add Listener to Search
        txtWebsite.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                btnLaunch.performClick();
                return true;
            }
            return false;
        });
        txtWebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnLaunch.setText("Visit");
                btnLaunch.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void prepareActivitySpinner(){
        if(spinActivities == null) return;
        // Get Activities List and Populate Spinner
        String activities[] = getResources().getStringArray(R.array.apps);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, activities);
        spinActivities.setAdapter(adapter); // set the adapter to provide layout of rows and content
        if (activityName != null) {
            int spinnerPosition = adapter.getPosition(activityName);
            spinActivities.setSelection(spinnerPosition);
        }
        // Add Listener to Spinner On Item Selected
        spinActivities.setOnItemSelectedListener(this);
    }

    private void launchOrVisit(){
        try{
            String str = btnLaunch.getText().toString();
            if(str.equals("Launch")){
                switchActivity(spinActivities.getSelectedItem().toString());
            }else if (str.equals("Visit") && viewWebsite != null){
                String search = txtWebsite.getText().toString();
                try{
                    if(!search.toLowerCase().contains("https://")) search = "https://"+search;
                    viewWebsite.loadUrl(search);
                }catch (Exception e){
                    MsgBox(e.getMessage());
                }
            }
        }catch (Exception e){
            MsgBox(e.getMessage());
        }
    }

    public void MsgBox(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        btnLaunch.setText("Launch");
        if(!selectedItem.equals(activityName)){
            btnLaunch.setEnabled(true);
        }else{
            btnLaunch.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        btnLaunch.setText("Launch");
    }

    private void switchActivity(String activity){
        if(activity.equalsIgnoreCase(activityName)){

            return;
        }
        try{
            Intent intent = null;
            switch (activity){
                case "Activity 1":
                    intent = new Intent(this, activity1.class);
                    break;
                case "Activity 2":
//                    intent = new Intent(this, Selection.class);
                    MsgBox("Selection");
                    break;
                case "Activity 3":
//                    intent = new Intent(this, MyPet.class);
                    MsgBox("MyPet");
                    break;
                case "Activity 4":
//                    intent = new Intent(this, LoopTimer.class);
                    MsgBox("LoopTimer");
                    break;
                case "Activity 5":
                    intent = new Intent(this, MainActivity.class);
                    break;
                case "Activity 6":
                    intent = new Intent(this, activity6.class);
                    break;
                case "Activity 7":
                    MsgBox("Activity 7");
                    break;
                case "Activity 8":
                    MsgBox("Activity 8");
                    break;
                case "Others":
//                    intent = new Intent(this, ViewWebsite.class);
                    MsgBox("View Website");
                    break;
                default:
                    MsgBox("Not selected...");
                    break;
            }
            if(intent != null){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            MsgBox(e.getMessage());
        }
    }
}