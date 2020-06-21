package android.example.demolistviewplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class IntroductionActivity extends AppCompatActivity {

    TextView instruction, out;
    public static String COMPLETED_ONBOARDING_PREF_NAME = "first_use";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        getSupportActionBar().hide();

        instruction = (TextView)findViewById(R.id.instruction);

        out = (TextView)findViewById(R.id.out);

        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), InstructionActivity.class);
                startActivity(intent);
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor sharedPreferencesEditor =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                sharedPreferencesEditor.putBoolean(
                        COMPLETED_ONBOARDING_PREF_NAME, true);
                sharedPreferencesEditor.apply();
                finish();
            }
        });
    }
}
