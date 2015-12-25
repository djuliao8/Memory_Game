package isec.memorygame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FimJogoActivity extends AppCompatActivity {
    Jogador jogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_jogo);
        Jogo jogo = (Jogo)getIntent().getSerializableExtra("id");
        jogador = (Jogador)getIntent().getSerializableExtra("jogador");

        TextView tempo = (TextView)findViewById(R.id.tempolabel);
        tempo.setText(jogo.time + "");

        TextView njogadas = (TextView)findViewById(R.id.numjogad);
        njogadas.setText(jogo.njogadas + "");

        TextView pontos = (TextView) findViewById(R.id.fimpontos);
        pontos.setText(jogo.pontuacao + "");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void onBotaoVoltar(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void onBotaoOutraVez(View v) {

        Intent i = new Intent(this, SinglePlayerActivity.class);
        i.putExtra("jogador",jogador);
        startActivity(i);
    }
}
