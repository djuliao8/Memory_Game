package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IdentificaJogadorRede extends AppCompatActivity {

    Button button;
    int tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tipo = getIntent().getIntExtra("Tipo",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogador_rede);
        button = (Button)findViewById(R.id.button5);
        if(tipo == 0)
            button.setText(getResources().getString(R.string.ER_BHost));
        if(tipo == 1)
            button.setText(getResources().getString(R.string.ER_BJoin));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipo == 0)
                    startActivity(new Intent(IdentificaJogadorRede.this, HostGamectivity.class));
                if(tipo == 1)
                    startActivity(new Intent(IdentificaJogadorRede.this, JoinGameActivity.class));
            }
        });

    }

}
