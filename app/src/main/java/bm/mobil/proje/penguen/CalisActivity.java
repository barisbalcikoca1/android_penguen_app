package bm.mobil.proje.penguen;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class CalisActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ArrayList<CalisVeri> listcalisVeri = new ArrayList<>();
    ArrayList<CalisVeri> bilinenKelimeler = new ArrayList<>();
    ArrayList<CalisVeri> bilinmeyenkelimeler = new ArrayList<>();

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton btnimagecik;

    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_calis1);

        initComponents();
        registerEventHandlers();

    }
    private void registerEventHandlers() {

        listcalisVeri.addAll(GirisActivity.Giris_Kaydirveriler);

        bilinmeyenkelimeler=listcalisVeri;
        bilinenKelimeler=listcalisVeri;
        setOnClickBtnimagecik();


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
        swipeRefreshLayout.setLayoutParams(params);

        //verilerin nasıl hizalanacağı belirlendi
        LinearLayoutManager layoutManager = new LinearLayoutManager(CalisActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //layoutmanager ı reclerview a set etmemiz lazım
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//performansı arttırmak için

        //veriyi eklemek
        CalisVeriAdapter calisVeriAdapter = new CalisVeriAdapter(listcalisVeri, CalisActivity.this); // adapter oluşturduk
        recyclerView.setAdapter(calisVeriAdapter);

        //veriyi kaydırma
        new ItemTouchHelper(itemTouchhelperSC).attachToRecyclerView(recyclerView);
    }
    private void initComponents() {
        recyclerView=findViewById(R.id.calis1_recyler_id);
        swipeRefreshLayout=findViewById(R.id.calis1_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(CalisActivity.this);
        btnimagecik=findViewById(R.id.calis1_btnimagecik);

    }


    @Override
    public void onRefresh() {
        CalisVeriAdapter calisVeriAdapter = new CalisVeriAdapter(bilinmeyenkelimeler, CalisActivity.this); // adapter oluşturduk
        recyclerView.setAdapter(calisVeriAdapter);

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(CalisActivity.this,"Sayfa Yenilendi",Toast.LENGTH_SHORT).show();
        }
    }

    //Sağa Sola kaydırma işlemini düzenleme
    ItemTouchHelper.SimpleCallback itemTouchhelperSC = new ItemTouchHelper.SimpleCallback(
            0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction==ItemTouchHelper.LEFT){
                String i=bilinenKelimeler.get(viewHolder.getAdapterPosition()).getJ_kelimeTR();
                bilinmeyenkelimeler.remove(viewHolder.getAdapterPosition());
                Toast.makeText(CalisActivity.this,i,Toast.LENGTH_SHORT).show();
            }
            else if(direction==ItemTouchHelper.RIGHT){
                Toast.makeText(CalisActivity.this,"Lütfen Tekrar Dene",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CalisActivity.this,"Kelime Yok Galiba",Toast.LENGTH_SHORT).show();
            }
            onRefresh();
        }
    };
    public void setOnClickBtnimagecik(){
        btnimagecik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cikis = new Intent(CalisActivity.this, KelimeCalisActivity.class);
                startActivity(cikis);
            }
        });
    }
}