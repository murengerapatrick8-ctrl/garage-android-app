package com.example.garage_examen_mob;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        // 🛡️ Protection Marquee
        TextView marquee = findViewById(R.id.marquee);
        if (marquee != null) {
            marquee.setSelected(true);
        }

        // 🛡️ Protection Bouton
        MaterialButton btn = findViewById(R.id.btnCommencer);
        if (btn != null) {
            btn.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, LoginActivity.class))
            );
        } else {
            Toast.makeText(this, "Bouton introuvable dans le XML", Toast.LENGTH_SHORT).show();
        }
    }
}