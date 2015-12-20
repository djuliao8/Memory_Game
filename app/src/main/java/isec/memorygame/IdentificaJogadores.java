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
        jog1 = new Jogador(et1.getText().toString(), 0);
        jog2 = new Jogador(et2.getText().toString(), 0);
        if (et1.getText().toString().matches("") || et2.getText().toString().matches("")) {
            //Cria toast a avisar que é necessário preencher o nome do jogador!
            Toast.makeText(getApplicationContext(), "É necessário escrever os nomes para jogar", Toast.LENGTH_LONG).show();
        } else {
            //Se houver algum nome escrito passa para o jogo
            Intent i = new Intent(this, MultiPlayerActivity.class);
            startActivity(i);
        }
    }
}
