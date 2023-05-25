package bm.mobil.proje.penguen;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class SozlukActivity extends AppCompatActivity {
    private Spinner fromSpinner,toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    String [] fromLanguages = {"İngilizce","Türkçe"};
    String [] toLanguages = {"Türkçe","İngilizce"};

    private static final int REQUEST_PERMISSION_CODE =1;
    int languageCode,fromLanguageCode,toLanguageCode =0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sozluk);

        initComponents();
        registerEventHandlers();

    }
    private void initComponents(){
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt= findViewById(R.id.idEdtSource);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedTV = findViewById(R.id.idTVTranslatedTV);
    }

    private void registerEventHandlers(){


        spinnerSetOnItemSelectedListener();
        micBtnOnClickListener();
        translateBtnOnClickListener();

    }
    private void translateBtnOnClickListener(){

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if(sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(SozlukActivity.this,"Lütfen çevirmek için metninizi girin",Toast.LENGTH_SHORT).show();

                }else if(fromLanguageCode==0){
                    Toast.makeText(SozlukActivity.this,"Lütfen çevrilecek dili seçin",Toast.LENGTH_SHORT).show();
                }else if(toLanguageCode==0){
                    Toast.makeText(SozlukActivity.this,"Lütfen çeviri yapmak için dili seçin",Toast.LENGTH_SHORT).show();
                }else{
                    translateText(fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());

                }
            }
        });

    }
    private void micBtnOnClickListener(){

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Lütfen Konuşunuz");
                try {
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(SozlukActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void spinnerSetOnItemSelectedListener(){

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
                if(fromSpinner.getSelectedItem().equals("Türkçe")){
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR");
                }else i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-EN");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                toLanguageCode=getLanguageCode(toLanguages[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==REQUEST_PERMISSION_CODE) {
            if(resultCode==RESULT_OK && data!= null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));
            }
        }
    }

    private void translateText(int fromLanguageCode,int toLanguageCode,String source){
        translatedTV.setText("İçerik İndirililiyor...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void unused){
                translatedTV.setText("Çevriliyor...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>(){
                    @Override
                    public void onSuccess(String s){
                        translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(SozlukActivity.this,"Çeviri Hatası :"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){
                Toast.makeText(SozlukActivity.this,"İçerik indirmede hata ile karşılaşıldı..."+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public int getLanguageCode(String language){
        int languageCode=0;
        switch (language){
            case "İngilizce":
                languageCode= FirebaseTranslateLanguage.EN;
                break;
            case "Türkçe":
                languageCode=FirebaseTranslateLanguage.TR;
                break;
            default:
                languageCode=0;
        }
        return languageCode;
    }

}

