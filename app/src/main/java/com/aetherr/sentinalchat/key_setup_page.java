package com.aetherr.sentinalchat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.UUID;

public class key_setup_page extends AppCompatActivity {

    private TextView tvPublicKey;
    private MaterialButton btnGenerate, btnContinue;
    private ProgressBar pbGenerating;
    private View keySetupContainer;
    private ImageButton btnCopy, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_key_setup_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        keySetupContainer = findViewById(R.id.key_setup_container);
        tvPublicKey = findViewById(R.id.tv_public_key);
        btnGenerate = findViewById(R.id.btn_generate);
        btnContinue = findViewById(R.id.btn_continue);
        pbGenerating = findViewById(R.id.pb_generating);
        btnCopy = findViewById(R.id.btn_copy);
        btnBack = findViewById(R.id.btn_back);

        // Initial Fade-in
        keySetupContainer.animate()
                .alpha(1f)
                .setDuration(800)
                .start();

        btnBack.setOnClickListener(v -> finish());

        btnGenerate.setOnClickListener(v -> generateKeys());

        btnCopy.setOnClickListener(v -> {
            String key = tvPublicKey.getText().toString();
            if (!key.equals(getString(R.string.placeholder_public_key))) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Public Key", key);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Public Key copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        btnContinue.setOnClickListener(v -> {
            Toast.makeText(this, "Entering Secure Lobby...", Toast.LENGTH_SHORT).show();
            // Transition to lobby would go here
        });
    }

    private void generateKeys() {
        btnGenerate.setEnabled(false);
        pbGenerating.setVisibility(View.VISIBLE);
        tvPublicKey.setText("Initialising entropy source...");
        tvPublicKey.setTextColor(getColor(R.color.text_secondary));

        // Simulate cryptographic key generation delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            pbGenerating.setVisibility(View.GONE);
            
            // Mock Public Key (Base64-like string)
            String mockPublicKey = "SC-" + UUID.randomUUID().toString().replace("-", "").toUpperCase() 
                    + UUID.randomUUID().toString().replace("-", "").toUpperCase();
            
            tvPublicKey.setText(mockPublicKey);
            tvPublicKey.setTextColor(getColor(R.color.accent_neon_blue));
            
            // Enable flow
            btnContinue.setEnabled(true);
            btnContinue.setBackgroundTintList(getColorStateList(R.color.accent_neon_blue));
            btnContinue.setTextColor(getColor(R.color.bg_deep_navy));
            
            Toast.makeText(this, "Identity Keys Generated Successfully", Toast.LENGTH_SHORT).show();
        }, 3000);
    }
}