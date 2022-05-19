package it.feio.android.omninotes;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smartlook.android.core.api.Smartlook;

import it.feio.android.omninotes.smartlook.SmartlookHandler;

public class SensitivityPlayground extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensitivity_playground);

        SmartlookHandler.onSensitivityPlayground(this);

        Button a = findViewById(R.id.buttonA);
        Smartlook.getInstance().getSensitivity().setViewInstanceSensitivity(a, true);
    }
}
