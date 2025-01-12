package com.example.remotemonitoring;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.remotemonitoring.views.fragments.BackPressedListener;
import com.example.remotemonitoring.views.fragments.MainFragment;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.androidx.AppNavigator;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Navigator navigator = new AppNavigator(this, R.id.main);

    private static BackPressedListener focusListener;

    public static void changeFocusListener(BackPressedListener listener) {
        focusListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SystemClock.sleep(TimeUnit.MILLISECONDS.toMillis(300));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RemoteMonitoringApplication.INSTANCE.getNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RemoteMonitoringApplication.INSTANCE.getNavigatorHolder().removeNavigator();
    }

    @Override
    public void onBackPressed() {
        if (focusListener != null) {
            focusListener.OnBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}