package bm.mobil.proje.penguen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;

public class AnaEkranActivity extends AppCompatActivity {
    private Intent j_incomingIntent;
    private TextView j_emailBilgisi;
    private ImageButton j_btnKelimeCalis, j_btnQuiz,j_btnSozluk;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_anaekran);

        initComponents();
        registerEventHandlers();


    }
    //id Atamaları
    private void initComponents(){
        j_emailBilgisi=findViewById(R.id.emailBilgisi);

        j_btnKelimeCalis = findViewById(R.id.btnKelimeCalis);
        j_btnQuiz = findViewById(R.id.btnQuiz);
        j_btnSozluk=findViewById(R.id.btnSozluk);


        //j_incomingIntent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        String mail = mAuth.getCurrentUser().getEmail().toString();
        int index = mail.indexOf('@');
        String ad = mail.substring(0,index);
        j_emailBilgisi.setText(ad);
    }
    private void registerEventHandlers(){
        j_btnKelimeCalis_onClick();
        j_btnQuiz_onClick();
        j_btnSozluk_onClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add("CIKIS YAP");
        MenuItem item2 = menu.add("DARK MODE");
        MenuItem item3 = menu.add("LIGHT MODE");
        MenuItem item4 = menu.add("SKOR TABLOSU");

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(AnaEkranActivity.this,GirisActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                return false;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return false;
            }
        });
        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return false;
            }
        });

        item4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(AnaEkranActivity.this,SkorActivity.class);
                startActivity(intent);
                return false;
            }
        });


        return true;
    }

    private void j_btnKelimeCalis_onClick(){
        j_btnKelimeCalis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKelimeCalis = new Intent(AnaEkranActivity.this, KelimeCalisActivity.class);
                startActivity(intentKelimeCalis);
                finish();
            }
        });
    }

    private void j_btnQuiz_onClick(){
        j_btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQuiz = new Intent(AnaEkranActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
                finish();
            }
        });
    }

    private void j_btnSozluk_onClick(){
        j_btnSozluk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSozluk = new Intent(AnaEkranActivity.this, SozlukActivity.class);
                startActivity(intentSozluk);
                finish();
            }
        });
    }

}
