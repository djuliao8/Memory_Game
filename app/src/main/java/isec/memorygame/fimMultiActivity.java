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

public class fimMultiActivity extends AppCompatActivity {
    String vencedor;
    FileOutputStream out;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_multi);
        Jogo jogo = (Jogo) getIntent().getSerializableExtra("id");

        TextView msg = (TextView) findViewById(R.id.textView);

        if (IdentificaJogadores.jog1.getPontos() > IdentificaJogadores.jog2.getPontos()) {
            vencedor = IdentificaJogadores.jog1.getNome();
            msg.setText(R.string.FM_LParabens + vencedor + "!");
        }else if(IdentificaJogadores.jog2.getPontos() > IdentificaJogadores.jog1.getPontos()) {
            vencedor = IdentificaJogadores.jog2.getNome();
            msg.setText(R.string.FM_LParabens + vencedor + "!");
        }else{
            msg.setText(R.string.FM_LEmpate);
        }




        TextView tempo = (TextView) findViewById(R.id.tempolabel);
        tempo.setText(jogo.time + "");

        TextView njogadas = (TextView) findViewById(R.id.numjogad);
        njogadas.setText(jogo.njogadas + "");

        TextView pontos = (TextView) findViewById(R.id.fimpontos);
        pontos.setText(jogo.pontuacao + "");

        //Guardar num ficheiro
        file = new File("histfile");

        ArrayList<String> info = new ArrayList<>();
        info.add("Vencedor:  " + vencedor);
        info.add("Tempo: " + tempo.getText().toString());
        info.add("Jogadas: " + njogadas.getText().toString());
        info.add("Pontuação: " + pontos.getText().toString());
        info.add("Modo: Dois Jogador");
        try {
            out = openFileOutput(file.getName(), Context.MODE_APPEND);
            out.write(info.toString().getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void onBotaoVoltar(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void onBotaoOutraVez(View v) {
        Intent i = new Intent(this, MultiPlayerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
