package com.aetherr.sentinalchat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilDisplayName;
    private TextInputEditText etDisplayName;
    private TextView tvWifiStatus;
    private ImageView ivWifiIcon;
    private MaterialButton btnInitialize;
    private ProgressBar pbLoading;
    private View contentContainer;

    private boolean isWifiConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        contentContainer = findViewById(R.id.content_container);
        tilDisplayName = findViewById(R.id.til_display_name);
        etDisplayName = findViewById(R.id.et_display_name);
        tvWifiStatus = findViewById(R.id.tv_wifi_status);
        ivWifiIcon = findViewById(R.id.iv_wifi_icon);
        btnInitialize = findViewById(R.id.btn_initialize);
        pbLoading = findViewById(R.id.pb_loading);

        // Initial Fade-in Animation
        contentContainer.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(300)
                .start();

        // WiFi Status Check
        checkWifiStatus();

        // Listeners
        etDisplayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnInitialize.setOnClickListener(v -> startSecureSession());
    }

    private void checkWifiStatus() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null && networkInfo.isConnected()) {
            isWifiConnected = true;
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String ssid = info.getSSID();
            
            // Remove quotes if present
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }

            tvWifiStatus.setText(getString(R.string.connected_to, ssid));
            tvWifiStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_cyan));
            ivWifiIcon.setColorFilter(ContextCompat.getColor(this, R.color.accent_neon_blue));
        } else {
            isWifiConnected = false;
            tvWifiStatus.setText(getString(R.string.wifi_warning));
            tvWifiStatus.setTextColor(ContextCompat.getColor(this, R.color.error_red));
            ivWifiIcon.setColorFilter(ContextCompat.getColor(this, R.color.error_red));
        }
        validateForm();
    }

    private void validateForm() {
        String name = etDisplayName.getText().toString().trim();
        btnInitialize.setEnabled(!name.isEmpty() && isWifiConnected);
    }

    private void startSecureSession() {
        setLoading(true);

        // Simulate session initialization (Key Generation delay)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setLoading(false);
            // Navigate to Key Generation screen
            Intent intent = new Intent(MainActivity.this, key_setup_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2500);
    }

    private void setLoading(boolean isLoading) {
        btnInitialize.setEnabled(!isLoading);
        btnInitialize.setText(isLoading ? "" : getString(R.string.initialize_session));
        pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        etDisplayName.setEnabled(!isLoading);
    }
}