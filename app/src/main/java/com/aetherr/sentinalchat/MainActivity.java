package com.aetherr.sentinalchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private MaterialButton btnLogin;
    private ProgressBar pbLoading;
    private View contentContainer;

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
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        btnLogin = findViewById(R.id.btn_login);
        pbLoading = findViewById(R.id.pb_loading);

        // Initial Fade-in Animation
        contentContainer.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(300)
                .start();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = tilEmail.getEditText().getText().toString().trim();
        String password = tilPassword.getEditText().getText().toString().trim();

        // Reset errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        if (email.isEmpty()) {
            tilEmail.setError("Email is required for secure access");
            return;
        }
        if (password.isEmpty()) {
            tilPassword.setError("Password cannot be empty");
            return;
        }

        // Show loading state
        setLoading(true);

        // Simulate network delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setLoading(false);
            // Simulate error for demonstration if password is "error"
            if (password.equals("error")) {
                tilPassword.setError("Invalid credentials. Please try again.");
            } else {
                Toast.makeText(MainActivity.this, "Encrypted Session Established", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    private void setLoading(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnLogin.setText(isLoading ? "" : getString(R.string.login));
        pbLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}