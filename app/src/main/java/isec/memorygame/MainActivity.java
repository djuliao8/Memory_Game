package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Botões
    public void onBotaoSobre(View v) {
        Intent i = new Intent(this, SobreActivity.class);
        startActivity(i);
    }

    public void onBotaoOps(View v) {
        Intent i = new Intent(this, OpcoesActivity.class);
        startActivity(i);

    }
}
