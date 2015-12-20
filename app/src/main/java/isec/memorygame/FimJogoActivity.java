package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class FimJogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_jogo);
        Jogo jogo = (Jogo)getIntent().getSerializableExtra("id");

        TextView tempo = (TextView)findViewById(R.id.tempolabel);
        tempo.setText(jogo.getTime());

        TextView njogadas = (TextView)findViewById(R.id.numjogad);
        njogadas.setText(jogo.getNjogadas() + "");

        TextView pontos = (TextView) findViewById(R.id.fimpontos);
        pontos.setText(jogo.getPontos() + "");
    }

    public void onBotaoVoltar(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
