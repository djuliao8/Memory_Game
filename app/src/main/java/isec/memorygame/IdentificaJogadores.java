package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IdentificaJogadores extends AppCompatActivity {
    static Jogador jog1;
    static Jogador jog2;
    EditText et1;
    EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogadores);
        et1 = (EditText) findViewById(R.id.jog1Text);
        et2 = (EditText) findViewById(R.id.jog2Text);
    }

    public void onBotaoJogar2(View v) {
        String jogador1 = et1.getText().toString();
        String jogador2 = et2.getText().toString();
        jog1 = new Jogador(jogador1, 0);
        jog2 = new Jogador(jogador2, 0);
        if (jogador1.equals("") || jogador2.equals("")) {
            //Cria toast a avisar que é necessário preencher o nome do jogador!
            Toast.makeText(getApplicationContext(), R.string.Err_FaltaNome, Toast.LENGTH_LONG).show();
        }else if(jogador1.length() > 7 || jogador2.length() > 7) {
            Toast.makeText(getApplicationContext(), R.string.Err_Nomrgrande7, Toast.LENGTH_LONG).show();
        }else if(jogador1.equals(jogador2)){
            Toast.makeText(getApplicationContext(), R.string.Err_Nomeigual, Toast.LENGTH_LONG).show();
        }
        else {
            //Se houver algum nome escrito passa para o jogo
            Intent i = new Intent(this, MultiPlayerActivity.class);
            startActivity(i);
        }
    }
}
