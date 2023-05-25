package bm.mobil.proje.penguen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivityA1A2 extends AppCompatActivity {

    private Button j_btnQuizBaslat;
    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_quiz_a1a2);

        initComponents();
        registerEventHandlers();


    }

    private void initComponents()
    {
        j_btnQuizBaslat = findViewById(R.id.btnQuizBaslat);

    }

    private void registerEventHandlers()
    {
        j_btnQuizBaslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivityA1A2.this,QuizActivityA1A2Baslat.class);
                startActivity(intent);
            }
        });

    }
}
