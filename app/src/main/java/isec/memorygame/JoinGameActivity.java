package isec.memorygame;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;

public class JoinGameActivity extends AppCompatActivity {

    Socket socketGame;
    EditText eNome;
    EditText eIp;
    Button bJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        eNome = (EditText)findViewById(R.id.JG_ENome);
        eIp = (EditText)findViewById(R.id.JG_EIp);
        bJoin = (Button)findViewById(R.id.JG_BJoin);
        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = eNome.getText().toString();
                String ip = eIp.getText().toString();
                if(nome.equals("") || ip.equals("")){
                    Toast.makeText(getApplicationContext(),R.string.Err_FaltaNome, Toast.LENGTH_LONG).show();
                }
                Intent i = new Intent(JoinGameActivity.this, GameClientActivity.class);
                i.putExtra("Nome",nome);
                i.putExtra("Ip",ip);
                startActivity(i);
            }
        });






    }
}
