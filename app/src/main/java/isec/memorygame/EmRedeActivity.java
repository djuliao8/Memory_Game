package isec.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmRedeActivity extends AppCompatActivity {

    Button bhost;
    Button bjoin;
    Util ut = new Util();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_rede);

        bhost = (Button)findViewById(R.id.ER_BHost);
        bjoin = (Button)findViewById(R.id.ER_BJoin);
        bhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmRedeActivity.this, GameActivity.class);
                i.putExtra("ID",ut.SERVER);
                startActivity(i);
            }
        });
        bjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmRedeActivity.this, GameActivity.class);
                i.putExtra("ID",ut.CLI);
                startActivity(i);
            }
        });

    }

    
}
