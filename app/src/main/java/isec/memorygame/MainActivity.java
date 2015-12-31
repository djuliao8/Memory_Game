package isec.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DefineDefault();




    }

    public void DefineDefault(){
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!pref.contains("Dificuldade") && !pref.contains("Click") && !pref.contains("Gallery")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("Dificuldade", 2);
            editor.putInt("Click", 0);
            editor.putString("Gallery","Default");
            editor.apply();
        }
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

    public void onBotaoHist(View v) {
        Intent i = new Intent(this, HistoricoActivity.class);
        startActivity(i);
    }

    public void onBotaoRede(View v) {
        Intent i = new Intent(this, EmRedeActivity.class);
        startActivity(i);
    }
}
