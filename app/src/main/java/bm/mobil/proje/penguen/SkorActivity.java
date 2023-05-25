package bm.mobil.proje.penguen;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class SkorActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView skorrecyclerView;
    SwipeRefreshLayout skorswipeRefreshLayout;
    ArrayList<Kullanici> skor_kullanici = new ArrayList<>();
    ArrayList<Kullanici> Anlik_skor = new ArrayList<>();
    private ImageButton buttonCikis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor);

        initComponents();
        registerEventHandlers();

        DatabaseReference dbRefKullaniciSkor = FirebaseDatabase.getInstance().getReference().child("kullanicilar");
        dbRefKullaniciSkor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anlik_skor.clear();
                for (DataSnapshot snpId : dataSnapshot.getChildren()) {

                    Kullanici kullanici = new Kullanici();
                    kullanici=snpId.getValue(Kullanici.class);
                    Anlik_skor.add(kullanici);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void registerEventHandlers() {

        //Ekranın ölçüsünü alıp ayarlıyoruz
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        //istenilen genişlik ve yüksekliği oluşturuyoruz
        int btnWidth = screenWidth ;
        int btnHeight = screenHeight ;

        //receyler view için swipelayouta ölçü veriyoruz
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth, btnHeight);
        skorswipeRefreshLayout.setLayoutParams(params);

        //verilerin nasıl hizalanacağı belirlendi
        LinearLayoutManager layoutManager = new LinearLayoutManager(SkorActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //layoutmanager ı reclerview a set etmemiz lazım
        skorrecyclerView.setLayoutManager(layoutManager);
        skorrecyclerView.setHasFixedSize(true);//performansı arttırmak için

        //veriyi eklemek
        SkorAdapter skorAdepter = new SkorAdapter(skor_kullanici, SkorActivity.this); // adapter oluşturduk
        skorrecyclerView.setAdapter(skorAdepter);

        buttonCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SkorActivity.this, AnaEkranActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initComponents() {
        skorrecyclerView=findViewById(R.id.skor_recyler_id);
        skorswipeRefreshLayout=findViewById(R.id.skor_swiperefreshlayout);
        skorswipeRefreshLayout.setOnRefreshListener(SkorActivity.this);
        buttonCikis = findViewById(R.id.skor_btnimagecik);
        skor_kullanici.clear();
        skor_kullanici.addAll(GirisActivity.Giris_Kullanici_Skor);
        Collections.sort(skor_kullanici);
    }

    @Override
    public void onRefresh() {

        if(skorswipeRefreshLayout.isRefreshing()){
            skorswipeRefreshLayout.setRefreshing(false);
            Collections.sort(Anlik_skor);
            SkorAdapter skorAdepter = new SkorAdapter(Anlik_skor, SkorActivity.this); // adapter oluşturduk
            skorrecyclerView.setAdapter(skorAdepter);
            Toast.makeText(SkorActivity.this,"Sayfa Yenilendi",Toast.LENGTH_SHORT).show();
        }
    }

}