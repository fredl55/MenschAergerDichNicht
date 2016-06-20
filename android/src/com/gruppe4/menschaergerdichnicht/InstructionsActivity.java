package com.gruppe4.menschaergerdichnicht;
import android.os.Bundle;
import android.app.Activity;

public class InstructionsActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_instructions);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


}
