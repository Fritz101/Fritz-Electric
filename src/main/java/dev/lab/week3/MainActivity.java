package dev.lab.week3;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText etUnitsUsed, etRebate;
    private Button btnCalculate, btnReset;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        etUnitsUsed = findViewById(R.id.etUnitsUsed);
        etRebate = findViewById(R.id.etRebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvResult = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBill();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUnitsUsed.setText("");
                etRebate.setText("");
                tvResult.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            showInfoDialog();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Info");

        final TextView message = new TextView(this);
        message.setText(android.text.Html.fromHtml(
                "<h2>Electric Rates:</h2>" +
                        "<p>For the first 200 kWh (1 - 200 kWh) per month: <b>RM 0.218</b></p>" +
                        "<p>For the next 100 kWh (201 - 300 kWh) per month: <b>RM 0.334</b></p>" +
                        "<p>For the next 300 kWh (301 - 600 kWh) per month: <b>RM 0.516</b></p>" +
                        "<p>For the next 300 kWh (601 - 900 kWh) per month onwards: <b>RM 0.546</b></p>"
        ));

        message.setPadding(50, 40, 50, 40);  // Adds padding for better readability
        builder.setView(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");

        final TextView message = new TextView(this);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        message.setText(android.text.Html.fromHtml(
                "<h2>FrizBill Calculator App</h2>" +
                        "<p><b>Developer:</b> Nur Hariz Bin Zolhani</p>" +
                        "<p><b>Matric Number:</b> 2022946833</p>" +
                        "<p><b>Email:</b> 2022946833@student.uitm.edu.my</p>" +
                        "<p><b>Github:</b> <a href='https://www.yourwebsite.com'>www.yourwebsite.com</a></p>"
        ));

        message.setPadding(50, 40, 50, 40);  // Adds padding for better readability
        builder.setView(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private void calculateBill() {
        if (etUnitsUsed.getText().toString().isEmpty() || etRebate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Error: Please insert number in both form", Toast.LENGTH_SHORT).show();
            return;
        }

        double unitsUsed = Double.parseDouble(etUnitsUsed.getText().toString());
        double rebate = Double.parseDouble(etRebate.getText().toString());

        double totalCost = calculateTotalCost(unitsUsed);
        double finalCost = totalCost - (totalCost * (rebate / 100));

        tvResult.setText(String.format("Units Used: %d kWh\nTotal Charges: RM %.2f\nTotal Bill: RM %.2f", (int) unitsUsed, totalCost, finalCost));
    }

    private double calculateTotalCost(double unitsUsed) {
        double totalCost = 0;

        if (unitsUsed <= 200) {
            totalCost = unitsUsed * 0.218;
        } else if (unitsUsed <= 300) {
            totalCost = (200 * 0.218) + ((unitsUsed - 200) * 0.334);
        } else if (unitsUsed <= 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + ((unitsUsed - 300) * 0.516);
        } else if (unitsUsed > 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unitsUsed - 600) * 0.546);
        }

        return totalCost;
    }
}
