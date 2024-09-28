package com.example.myevcarloancalculator;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    private TextView  monthlyInstallment, tvSelectedModel, tvCarAmount;
    private ImageView ivSelectedModel;
    private String selectedModel;
    private double carAmount;
    private EditText interestRate, loanPeriod;
    private RelativeLayout modelLayout;
    private Spinner spinnerVariant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button calculate;
        ImageButton iguanaModel, penguinModel;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Model selection buttons
        iguanaModel=findViewById(R.id.iguanaImageButton);
        penguinModel=findViewById(R.id.penguinImageButton);

        // Variant selection spinner and layout
        spinnerVariant = findViewById(R.id.spinnerVariant);
        tvSelectedModel = findViewById(R.id.tvSelectedModel);
        ivSelectedModel = findViewById(R.id.ivSelectedModel);
        tvCarAmount = findViewById(R.id.tvCarAmount);
        modelLayout = findViewById(R.id.selectModelRelativeLayout);
        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout purpleBoxLinear = findViewById(R.id.purpleBox);
        purpleBoxLinear.setBackgroundColor(Color.parseColor("#4D800080"));

        // Loan details input fields and layout
        interestRate = findViewById(R.id.txtInterest);
        loanPeriod = findViewById(R.id.txtLoanPeriod);

        // Calculate button and result text view
        calculate = findViewById(R.id.btnCalc);
        monthlyInstallment = findViewById(R.id.monthlyInstall);


        iguanaModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModel = "Iguana";
                tvSelectedModel.setText(selectedModel);
                ivSelectedModel.setImageResource(R.drawable.iguana);
                setupSpinner(R.array.model1_variants, R.array.model1_variant_amounts);
                modelLayout.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        penguinModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModel = "Penguin";
                tvSelectedModel.setText(selectedModel);
                ivSelectedModel.setImageResource(R.drawable.penguin2);
                setupSpinner(R.array.model2_variants, R.array.model2_variant_amounts);
                modelLayout.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcMonthlyInstallment();
            }
        });

        spinnerVariant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedModel.equals("Iguana")) {
                    String[] amounts1 = getResources().getStringArray(R.array.model1_variant_amounts);
                    carAmount = Double.parseDouble(amounts1[position]);
                } else if (selectedModel.equals("Penguin")) {
                    String[] amounts2 = getResources().getStringArray(R.array.model2_variant_amounts);
                    carAmount = Double.parseDouble(amounts2[position]);
                }
                tvCarAmount.setText("Loan Amount: RM " + carAmount); // Display the car amount
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void setupSpinner(int variantsArrayId, int amountsArrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                variantsArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariant.setAdapter(adapter);
        spinnerVariant.setTag(amountsArrayId);
    }
    private void calcMonthlyInstallment() {
        if (interestRate.getText().toString().isEmpty() || loanPeriod.getText().toString().isEmpty()) {
            // Check if interest rate or loan period is empty
            Toast.makeText(this, "Please enter both interest rate and loan period.", Toast.LENGTH_SHORT).show();
            return;
        }

        // hide the keyboard or keypad
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(loanPeriod.getWindowToken(), 0);

        double interestRateValue = Double.parseDouble(interestRate.getText().toString()) / 100 / 12;
        int loanPeriodValue = Integer.parseInt(loanPeriod.getText().toString()) * 12;

        if (carAmount <= 0 || interestRateValue <= 0 || loanPeriodValue <= 0) {
            // Check if car amount, interest rate, or loan period is zero or negative
            Toast.makeText(this, "Invalid input. Please enter valid values.", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyInstallmentValue = (carAmount * interestRateValue) / (1 - Math.pow(1 + interestRateValue, -loanPeriodValue));

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setCurrency(Currency.getInstance("MYR"));
        monthlyInstallment.setText("Monthly Installment: \n"+ formatter.format(monthlyInstallmentValue));

        // Align the text to center
        monthlyInstallment.setGravity(Gravity.CENTER);

        // Adjust spacing between lines
        float spacingMultiplier = 1.5f;
        float spacingExtra = 10;
        monthlyInstallment.setLineSpacing(spacingExtra, spacingMultiplier);
    }
}