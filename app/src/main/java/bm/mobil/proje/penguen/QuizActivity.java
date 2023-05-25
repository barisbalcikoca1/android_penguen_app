package bm.mobil.proje.penguen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private Button j_QbtnSeviyeA1A2;
    @Override
    protected void onCreate (Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_quiz);

        initComponents();
        registerEventHandlers();

    }

    private void registerEventHandlers() {

        j_QbtnSeviyeA1A2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this,QuizActivityA1A2.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        j_QbtnSeviyeA1A2 = findViewById(R.id.QbtnSeviyeA1A2);
    }
}
