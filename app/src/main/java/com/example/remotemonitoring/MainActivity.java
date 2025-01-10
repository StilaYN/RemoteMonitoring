package com.example.remotemonitoring;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.remotemonitoring.views.fragments.MainFragment;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.androidx.AppNavigator;
import com.github.terrakok.cicerone.androidx.FragmentScreen;

public class MainActivity extends AppCompatActivity {

    private Navigator navigator = new AppNavigator(this, R.id.main);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(savedInstanceState == null){
            RemoteMonitoringApplication.INSTANCE.getRouter().replaceScreen(new FragmentScreen() {
                @Override
                public boolean getClearContainer() {
                    return false;
                }

                @NonNull
                @Override
                public Fragment createFragment(@NonNull FragmentFactory fragmentFactory) {
                    return MainFragment.newInstance();
                }

                @NonNull
                @Override
                public String getScreenKey() {
                    return "MainFragment";
                }
            });
        }
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
}