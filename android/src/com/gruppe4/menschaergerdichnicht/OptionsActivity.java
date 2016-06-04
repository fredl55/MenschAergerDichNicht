package com.gruppe4.menschaergerdichnicht;
    import android.content.Intent;
    import android.os.Bundle;
    import android.app.Activity;
    import android.view.View;
    import com.gruppe4.Logic.Player;

public class OptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_options);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
