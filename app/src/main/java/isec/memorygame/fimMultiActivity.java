package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class fimMultiActivity extends AppCompatActivity {
    String vencedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_multi);
        Jogo jogo = (Jogo) getIntent().getSerializableExtra("id");

        if (IdentificaJogadores.jog1.getPontos() > IdentificaJogadores.jog2.getPontos())
            vencedor = IdentificaJogadores.jog1.getNome();
        else
            vencedor = IdentificaJogadores.jog2.getNome();

        TextView msg = (TextView) findViewById(R.id.textView);
        msg.setText("Parabéns " + vencedor);

        TextView tempo = (TextView) findViewById(R.id.tempolabel);
        tempo.setText(jogo.time + "");

        TextView njogadas = (TextView) findViewById(R.id.numjogad);
        njogadas.setText(jogo.njogadas + "");

        TextView pontos = (TextView) findViewById(R.id.fimpontos);
        pontos.setText(jogo.pontuacao + "");
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

        Intent i = new Intent(this, MultiPlayerActivity.class);
        startActivity(i);
    }
}
