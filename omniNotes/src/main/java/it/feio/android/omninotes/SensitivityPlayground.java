package it.feio.android.omninotes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.feio.android.omninotes.smartlook.SmartlookHandler;

public class SensitivityPlayground extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensitivity_playground);

        SmartlookHandler.onSensitivityPlayground(this);
    }
}
