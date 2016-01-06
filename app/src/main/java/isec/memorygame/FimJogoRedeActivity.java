package isec.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FimJogoRedeActivity extends AppCompatActivity {
    Jogador jog1;
    Jogador jog2;
    String tempo;

    TextView titulo;
    TextView eTempo;
    TextView jog1Nome;
    TextView jog2Nome;
    Button again;
    Button menu;

    Util ut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fim_jogo_rede);

        ut = new Util();

        jog1 = (Jogador) getIntent().getSerializableExtra("Jog1");
        jog2 = (Jogador) getIntent().getSerializableExtra("Jog2");
        tempo = getIntent().getStringExtra("Tempo");

        titulo = (TextView) findViewById(R.id.FGA_Titulo);
        eTempo = (TextView) findViewById(R.id.FGA_TTempo);
        jog1Nome = (TextView) findViewById(R.id.FGA_Jog1);
        jog2Nome = (TextView) findViewById(R.id.FGA_Jog2);
        again = (Button) findViewById(R.id.FGA_Again);
        menu = (Button) findViewById(R.id.FGA_Menu);

        String escreve;

        if (jog1.getPontos() > jog2.getPontos()) {
            titulo.setText(getResources().getString(R.string.FM_LParabens) + " " + jog1.getNome() + "!");
            escreve = "Network" + " " + jog1.getNome() + " " + jog1.getPontos() + " " + "ND" + " " + tempo + " " + "ND";
        } else if (jog2.getPontos() > jog1.getPontos()){
            escreve = "Network" + " " + jog2.getNome() + " " + jog2.getPontos() + " " + "ND" + " " + tempo + " " + "ND";
            titulo.setText(getResources().getString(R.string.FM_LParabens) + " " + jog2.getNome() + "!");
        }else {
            titulo.setText(getResources().getString(R.string.FM_LEmpate) + "!");
            escreve = "Network" + " " + getResources().getString(R.string.FM_LEmpate) + " " + jog1.getPontos() + " " + "ND" + " " + tempo + " " + "ND";
        }
        ut.escreverFicheiro(getApplicationContext(), escreve);
        eTempo.setText(tempo);
        jog1Nome.setText(jog1.getNome() + " com " + jog1.getPontos() + " pontos");
        jog2Nome.setText(jog2.getNome() + " com " + jog2.getPontos() + " pontos");

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FimJogoRedeActivity.this, EmRedeActivity.class);
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FimJogoRedeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



    }
}
