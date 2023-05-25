package bm.mobil.proje.penguen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class CalisKartActivity extends AppCompatActivity  {

    private ArrayAdapter<String> arrayAdapter;

    private final List<String> kartlistBilinmeyen=new ArrayList<>();

    Button btnlkartSol,btnkartSag;
    SwipeFlingAdapterView swipeFlingAdapterView;

    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_caliskart);

        initComponents();
        registerEventHandlers();
        setOnClickBtnlkartSol();
        setOnClickBtnlkartSag();
    }
    private void registerEventHandlers() {

        kartlistBilinmeyen.addAll(GirisActivity.Giris_Kartveriler);

        arrayAdapter = new ArrayAdapter<>(this,R.layout.caliskart_item,R.id.txtviewcaliskartitem,kartlistBilinmeyen);
        swipeFlingAdapterView.setAdapter(arrayAdapter);

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //listedeki elemanları tek tek siler
                kartlistBilinmeyen.remove(0);
                arrayAdapter.notifyDataSetChanged();
                if(kartlistBilinmeyen.isEmpty()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CalisKartActivity.this);
                    builder.setTitle("Tebrikler");
                    builder.setMessage("Tüm Kelimeleri Öğrendiniz");
                    builder.setIcon(R.drawable.ic_score);


                    class AlertDialogClickListener implements DialogInterface.OnClickListener {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == AlertDialog.BUTTON_POSITIVE || which == AlertDialog.BUTTON_NEUTRAL) {
                                Intent intent = new Intent(CalisKartActivity.this,KelimeCalisActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    AlertDialogClickListener alertDialogClickListener = new AlertDialogClickListener();
                    builder.setPositiveButton("TAMAM", alertDialogClickListener);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
            }
            //card sola gittikten sonra
            @Override
            public void onLeftCardExit(Object o) {

            }
            //card sağa gittikten sonra
            @Override
            public void onRightCardExit(Object o) {

            }
            //Liste bitiğinde ne olacağı yazılır
            @Override
            public void onAdapterAboutToEmpty(int i) {

            }
            //kaydırılırken
            @Override
            public void onScroll(float v) {

            }
        });
        //fling e tıklanırsa
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {

            }
        });
    }

    private void initComponents() {
        swipeFlingAdapterView=findViewById(R.id.caliskartSwipfling);
        btnlkartSol=findViewById(R.id.btncaliskartSol);
        btnkartSag=findViewById(R.id.btncaliskartSag);
        btnlkartSol.setText("Biliyorum");
        btnkartSag.setText("Bilmiyorum");

    }
    public void setOnClickBtnlkartSol(){
        btnlkartSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeFlingAdapterView.getTopCardListener().selectLeft();
            }
        });
    }
    public void setOnClickBtnlkartSag(){
        btnkartSag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeFlingAdapterView.getTopCardListener().selectRight();
            }
        });
    }
}