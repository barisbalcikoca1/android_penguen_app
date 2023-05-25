package bm.mobil.proje.penguen;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class GirisActivity extends AppCompatActivity{

    public static final List<String> Giris_Kartveriler = new ArrayList<>();
    public static final List<CalisVeri> Giris_Kaydirveriler = new ArrayList<>();
    public static final List<Kullanici> Giris_Kullanici_Skor = new ArrayList<>();
    private static final int REQUEST_CODE_USER_AL = 1;
    private static final String TAG = "GirisActivity";
    private FirebaseAuth mAuth;
    private TextInputLayout emailWrapper, sifreWrapper;
    private TextInputEditText email;
    private Button btnGiris, btnKaydol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Theme_PENGUEN);
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        initComponents();
        veritabaniVeriCek();
        registerEventHandlers();
    }
    public void veritabaniVeriCek(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Kelimeler").child("Hayvanlar");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Giris_Kaydirveriler.clear();
                for (DataSnapshot snpA1:dataSnapshot.getChildren()){
                    Giris_Kaydirveriler.add(new CalisVeri(snpA1.getKey(),snpA1.getValue().toString()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
        DatabaseReference dbRefSayilar = FirebaseDatabase.getInstance().getReference().child("Kelimeler").child("Sayılar");
        dbRefSayilar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Giris_Kartveriler.clear();
                for (DataSnapshot snpA1 : dataSnapshot.getChildren()) {
                    String a = snpA1.getKey();
                    String b = snpA1.getValue().toString();
                    Giris_Kartveriler.add(a + "\n \n" + b);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference dbRefKullaniciSkor = FirebaseDatabase.getInstance().getReference().child("kullanicilar");
        dbRefKullaniciSkor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Giris_Kullanici_Skor.clear();
                for (DataSnapshot snpId : dataSnapshot.getChildren()) {

                    Kullanici kullanici = new Kullanici();
                    kullanici=snpId.getValue(Kullanici.class);
                    Giris_Kullanici_Skor.add(kullanici);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void onStart() {
        super.onStart();
        // Kullanici daha once giris yapmis mi kontrol et; buna gore arayuzu hazirla
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void updateUI(FirebaseUser user) {
        if (user != null) {

            Intent intent = new Intent(this, AnaEkranActivity.class);
            //intent.putExtra("user", user.getEmail());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Henüz giriş yapmadınız!", Toast.LENGTH_LONG).show();
        }
    }
    private void registerEventHandlers() {

        if(emailWrapper.getEditText().equals(""))
            emailWrapper.setError("Hata!");
        else
            emailWrapper.setErrorEnabled(false);

        if(sifreWrapper.getEditText().equals(""))
            sifreWrapper.setError("Hata!");
        else
            sifreWrapper.setErrorEnabled(false);


        btnKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emailWrapper.getEditText().getText().toString().isEmpty() || !sifreWrapper.getEditText().getText().toString().isEmpty()){

                    String eposta = emailWrapper.getEditText().getText().toString();
                    String sifre = sifreWrapper.getEditText().getText().toString();

                    if(Patterns.EMAIL_ADDRESS.matcher(eposta).matches()){
                        int index = eposta.indexOf('@');
                        String ad = eposta.substring(0,index);
                        Kullanici kullanici = new Kullanici(ad,eposta,0);


                        mAuth.createUserWithEmailAndPassword(eposta, sifre).addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference dbRef = database.getReference();
                                    dbRef.child("kullanicilar").child(mAuth.getUid()).setValue(kullanici);

                                    Toast.makeText(GirisActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w(TAG, "Kayıt başarısız: ", task.getException());
                                    Toast.makeText(GirisActivity.this, "Kayıt başarısız.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(GirisActivity.this,"Lütfen geçerli bir mail giriniz",Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(GirisActivity.this,"Lütfen kutuları boş bırakmayın",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailWrapper.getEditText().getText().toString().isEmpty()|| !sifreWrapper.getEditText().getText().toString().isEmpty() ){
                    String eposta = emailWrapper.getEditText().getText().toString();
                    String sifre = sifreWrapper.getEditText().getText().toString();
                    mAuth.signInWithEmailAndPassword(eposta, sifre).addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                                Intent intent = new Intent(GirisActivity.this, AnaEkranActivity.class);
                                intent.putExtra("user", email.getText().toString());
                                startActivityForResult(intent, REQUEST_CODE_USER_AL);

                            } else {
                                Log.w(TAG, "Giriş başarısız: ", task.getException());
                                Toast.makeText(GirisActivity.this, "Giriş başarısız.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
                }else{
                    Toast.makeText(GirisActivity.this,"Lütfen kutuları boş bırakmayın",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();
        emailWrapper = findViewById(R.id.emailWrapper);
        sifreWrapper = findViewById(R.id.sifreWrapper);
        btnGiris = findViewById(R.id.btnGiris);
        btnKaydol = findViewById(R.id.btnKaydol);
        email = findViewById(R.id.email);
    }
}