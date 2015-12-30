package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class IdentificaJogadorRede extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogador_rede);
    }

    public void onBotaoCriar(View v) {

    }

    public void onBotaoJuntar() {
        Intent i = new Intent(this, JoinActivity.class);
        startActivity(i);
    }
}
