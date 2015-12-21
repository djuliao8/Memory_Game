package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
    public void onBotao1jog(View v){
        Intent i = new Intent(this, IdentificaJogador.class);
        startActivity(i);
    }

    public void onBotao2jog(View v) {
        Intent i = new Intent(this, IdentificaJogadores.class);
        startActivity(i);
    }
}
