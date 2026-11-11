package com.example.garage_examen_mob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        ImageView btnBack        = findViewById(R.id.btnBack);
        TextInputEditText etUser = findViewById(R.id.etUsername);
        TextInputEditText etPass = findViewById(R.id.etPassword);
        MaterialButton btnLogin  = findViewById(R.id.btnLogin);
        ProgressBar progress     = findViewById(R.id.progress);

        if (etUser == null || etPass == null || btnLogin == null) {
            Toast.makeText(this, "Erreur : XML de login incomplet", Toast.LENGTH_LONG).show();
            return;
        }

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> {
            String user = etUser.getText() != null ? etUser.getText().toString().trim() : "";
            String pass = etPass.getText() != null ? etPass.getText().toString().trim() : "";

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (progress != null) progress.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);

            Map<String, String> creds = new HashMap<>();
            creds.put("username", user);
            creds.put("password", pass);

            Api.get().login(creds).enqueue(new Callback<Api.Res<Models.LoginRes>>() {
                @Override
                public void onResponse(Call<Api.Res<Models.LoginRes>> c, Response<Api.Res<Models.LoginRes>> r) {
                    if (progress != null) progress.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    // 🔍 ON AFFICHE LE VRAI MESSAGE POUR DEBUGER
                    String msg = "Code HTTP: " + r.code();
                    if (r.body() != null && r.body().message != null) {
                        msg += "\nMessage: " + r.body().message;
                    }
                    Log.d("LOGIN_DEBUG", msg);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();

                    if (r.isSuccessful() && r.body() != null && r.body().success) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Api.Res<Models.LoginRes>> c, Throwable t) {
                    if (progress != null) progress.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    // 🔍 ON AFFICHE L'ERREUR RESEAU EXACTE
                    Log.e("LOGIN_DEBUG", "Erreur réseau", t);
                    Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}