package isec.memorygame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;

public class FimJogoActivity extends AppCompatActivity {
    FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_jogo);
        Jogo jogo = (Jogo)getIntent().getSerializableExtra("id");

        TextView tempo = (TextView)findViewById(R.id.tempolabel);
        tempo.setText(jogo.time + "");

        TextView njogadas = (TextView)findViewById(R.id.numjogad);
        njogadas.setText(jogo.njogadas + "");

        TextView pontos = (TextView) findViewById(R.id.fimpontos);
        pontos.setText(jogo.pontuacao + "");

        //Guardar num ficheiro
        String filename = "histfile";
        String info = "Tempo: " + tempo.getText().toString() + "Número Jogadas: " + njogadas.getText().toString() + "Pontuação: " + pontos.getText().toString() + "Modo: Um Jogador";

        try {
            out = openFileOutput(filename, Context.MODE_APPEND);
            out.write(info.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onBotaoVoltar(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onBotaoOutraVez(View v) {

        Intent i = new Intent(this, SinglePlayerActivity.class);
        startActivity(i);
    }
}
