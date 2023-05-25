package bm.mobil.proje.penguen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizActivityA1A2Baslat extends AppCompatActivity {

    private TextView sure,girilecekkelimeEN,dogruSkor,yanlisSkor,genelSkor;
    private EditText cevir;
    private Button btnCevir;
    private FirebaseAuth mAuth;

    int puan,veritabanıskor;
    int toplamKelime;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();

    final static ArrayList<CalisVeri> kelimeler = new ArrayList<>();

    @Override
    protected void onCreate (Bundle SavedInstanceState){

        FirebaseApp.initializeApp(this);

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_quiz_a1a2_baslat);

        mAuth = FirebaseAuth.getInstance();

        kelimeler.add(new CalisVeri("Start","basla"));


        initComponents();
        veritabanivericek();
        registerEventHandlers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (puan>veritabanıskor){
            dbRef.child("kullanicilar").child(mAuth.getUid()).child("j_skor").setValue(puan);
        }

    }
    private void registerEventHandlers() {
        puan=Integer.parseInt(genelSkor.getText().toString());
        timernesnesi();
        SetbtnCevirOnClick();
        toplamKelime=kelimeler.size();
        toplamKelime--;
        girilecekkelimeEN.setText(kelimeler.get(toplamKelime).getJ_kelimeEN());

    }

    private void SetbtnCevirOnClick() {
        btnCevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toplamKelime!=0){
                    if(kelimeler.get(toplamKelime).j_kelimeTR.equals(cevir.getText().toString())){
                        int skor=Integer.parseInt(dogruSkor.getText().toString())+1;
                        String stringskor=Integer.toString(skor);
                        dogruSkor.setText(stringskor);
                        puan++;


                    }else{
                        puan--;
                        int skor=Integer.parseInt(yanlisSkor.getText().toString())+1;
                        String stringskor=Integer.toString(skor);
                        yanlisSkor.setText(stringskor);

                    }
                    toplamKelime--;
                    girilecekkelimeEN.setText(kelimeler.get(toplamKelime).getJ_kelimeEN());
                    genelSkor.setText(Integer.toString(puan));

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivityA1A2Baslat.this);
                    builder.setTitle("Puan Durumunuz");
                    builder.setMessage("Quizden almış olduğunuz puan "+puan+" puandır. \nQuiz ekranına dönmek için lütfen tamam butonuna tıklayınız.");
                    builder.setIcon(R.drawable.ic_score);

                    AlertDialogClickListener alertDialogClickListener = new AlertDialogClickListener();
                    builder.setPositiveButton("TAMAM", alertDialogClickListener);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void timernesnesi() {
        new CountDownTimer(30000, 100) {
            @Override
            public void onTick(long l) {
                sure.setText("Kalan Süreniz: "+l/1000);
            }

            @Override
            public void onFinish() {
                sure.setText("Süreniz Bitti");
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivityA1A2Baslat.this);
                builder.setTitle("Puan Durumunuz");
                builder.setMessage("Quizden almış olduğunuz puan "+puan+" puandır. \nQuiz ekranına dönmek için lütfen tamam butonuna tıklayınız.");
                builder.setIcon(R.drawable.ic_score);

                AlertDialogClickListener alertDialogClickListener = new AlertDialogClickListener();
                builder.setPositiveButton("TAMAM", alertDialogClickListener);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }.start();
    }

    private void initComponents() {

        sure = findViewById(R.id.sure);
        girilecekkelimeEN=findViewById(R.id.girilecekKelime);
        btnCevir=findViewById(R.id.butonCevir);
        cevir=findViewById(R.id.cevir);
        dogruSkor=findViewById(R.id.dogruSkor);
        yanlisSkor=findViewById(R.id.yanlisSkor);
        genelSkor=findViewById(R.id.genelSkor);
    }

    private void veritabanivericek(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Kelimeler").child("Hayvanlar");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot hayvanlar:snapshot.getChildren()){
                    kelimeler.add(new CalisVeri(hayvanlar.getKey(),hayvanlar.getValue().toString()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        DatabaseReference dbRefskor = FirebaseDatabase.getInstance().getReference().child("kullanicilar").child(mAuth.getUid()).child("j_skor");
        dbRefskor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot skor:snapshot.getChildren()){
                    veritabanıskor=Integer.parseInt(skor.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    class AlertDialogClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == AlertDialog.BUTTON_NEGATIVE || which == AlertDialog.BUTTON_NEUTRAL) {
                Toast.makeText(QuizActivityA1A2Baslat.this, "İşlem iptal edildi.", Toast.LENGTH_SHORT).show();
            } else if (which == AlertDialog.BUTTON_POSITIVE){ // veya else
                Toast.makeText(QuizActivityA1A2Baslat.this, "Çıkış başarıyla gerçekleştirildi.", Toast.LENGTH_SHORT).show();
                QuizActivityA1A2Baslat.this.finish(); // activity’nin sonlandırılması
            }
        }
    }
    public void refresh(){


    }
}