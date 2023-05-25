package bm.mobil.proje.penguen;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KelimeCalisActivity extends AppCompatActivity {

    private Button btnKelimeCalis, btnkelimeBil;
    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_kelimecalis);

        initComponents();
        registerEventHandlers();

    }
    private void registerEventHandlers() {
        btnKelimeCalis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calis1 = new Intent(KelimeCalisActivity.this, CalisActivity.class);
                startActivity(calis1);
            }
        });

        btnkelimeBil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calis2 = new Intent(KelimeCalisActivity.this, CalisKartActivity.class);
                startActivity(calis2);
            }
        });

    }
    private void initComponents() {
        btnKelimeCalis = findViewById(R.id.btnSeviyeA1);
        btnkelimeBil = findViewById(R.id.btnSeviyeA2);
    }
}
