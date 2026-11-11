package com.example.garage_examen_mob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HOME_DEBUG";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);

        ImageView btnBack            = findViewById(R.id.btnBack);
        MaterialCardView cardMeca    = findViewById(R.id.cardMeca);
        MaterialCardView cardVehi    = findViewById(R.id.cardVehi);
        MaterialCardView cardInter   = findViewById(R.id.cardInter);
        MaterialCardView cardClients = findViewById(R.id.cardClients);
        MaterialCardView cardStats   = findViewById(R.id.cardStats);

        // 🛡️ Si un ID est manquant dans le XML, on prévient au lieu de crasher
        if (btnBack == null || cardMeca == null || cardVehi == null ||
                cardInter == null || cardClients == null || cardStats == null) {
            Log.e(TAG, "ERREUR : Une vue est null. Vérifiez activity_home.xml !");
            Toast.makeText(this, "Erreur d'interface : ID manquant", Toast.LENGTH_LONG).show();
            return;
        }

        btnBack.setOnClickListener(v -> confirmerDeconnexion());

        cardMeca.setOnClickListener(v    -> ouvrirTable("mecaniciens",   "Mécaniciens"));
        cardVehi.setOnClickListener(v    -> ouvrirTable("vehicules",     "Véhicules"));
        cardInter.setOnClickListener(v   -> ouvrirTable("interventions", "Interventions"));
        cardClients.setOnClickListener(v -> ouvrirTable("clients",       "Clients"));
        cardStats.setOnClickListener(v   -> ouvrirTable("statistiques",  "Statistiques"));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { confirmerDeconnexion(); }
        });
    }

    private void ouvrirTable(String table, String title) {
        try {
            Intent i = new Intent(this, DataActivity.class);
            i.putExtra("table", table);
            i.putExtra("title", title);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir les données", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmerDeconnexion() {
        new AlertDialog.Builder(this)
                .setTitle("Déconnexion").setMessage("Voulez-vous vous déconnecter ?")
                .setPositiveButton("Oui", (d, w) -> {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }).setNegativeButton("Annuler", null).show();
    }
}