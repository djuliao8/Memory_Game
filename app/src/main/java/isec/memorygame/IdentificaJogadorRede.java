package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IdentificaJogadorRede extends AppCompatActivity {

    Button button;
    EditText tNome;
    int tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogador_rede);

        tNome = (EditText)findViewById(R.id.nomejrede);



        button = (Button)findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String nome = tNome.getText().toString();

                if (nome.equals(""))
                    Toast.makeText(getApplicationContext(), R.string.Err_FaltaNome, Toast.LENGTH_LONG).show();
                else if(nome.length() > 7)
                    Toast.makeText(getApplicationContext(), R.string.Err_Nomrgrande7, Toast.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(IdentificaJogadorRede.this, HostGamectivity.class);
                    i.putExtra("Nome", nome);
                    startActivity(i);
                }

            }
        });

    }

}
