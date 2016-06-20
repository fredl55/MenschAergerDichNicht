package com.gruppe4.menschaergerdichnicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import com.gruppe4.Logic.Player;

public class InstructionsActivity extends Activity {

    private LinearLayout container;
    private int currentX;
    private int currentY;

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
