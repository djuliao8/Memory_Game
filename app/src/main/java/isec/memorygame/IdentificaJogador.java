package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IdentificaJogador extends AppCompatActivity {
    static Jogador jog;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogador);
        et = (EditText) findViewById(R.id.editText2);


    }

    public void onBotaoJogar(View v) {
        jog = new Jogador(et.getText().toString(), 0);
        String nome = et.getText().toString();
        if (nome.equals("")) {
            //Cria toast a avisar que é necessário preencher o nome do jogador!
            Toast.makeText(getApplicationContext(),R.string.Err_FaltaNome, Toast.LENGTH_LONG).show();
        }
        else if(nome.length() > 12) {
            Toast.makeText(getApplicationContext(),R.string.Err_Nomegrande, Toast.LENGTH_LONG).show();
            et.setText("");
        }else {
            //Se houver algum nome escrito passa para o jogo
            Intent i = new Intent(this, SinglePlayerActivity.class);
            startActivity(i);
        }
    }
}
