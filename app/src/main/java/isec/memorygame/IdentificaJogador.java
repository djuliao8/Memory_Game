package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IdentificaJogador extends AppCompatActivity {
    static Jogador jog;
    final EditText et = (EditText) findViewById(R.id.editText2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogador);
        jog = new Jogador(et.toString(), 0);
    }

    public void onBotaoJogar(View v) {
        //Se houver algum nome escrito passa para o jogo
        if (jog.getNome() != null) {
            Intent i = new Intent(this, SinglePlayerActivity.class);
            startActivity(i);
        } else {
            //Cria toast a avisar que é necessário preencher o nome do jogador!
            Toast.makeText(getApplicationContext(), "É necessário escrever um nome para jogar",
                    Toast.LENGTH_LONG).show();
        }
    }
}
