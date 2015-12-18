package isec.memorygame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class OpcoesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        //Criar o Spinner para os Niveis
        Spinner sNivel = (Spinner) findViewById(R.id.dropNivel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dropNivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sNivel.setAdapter(adapter);

        //Criar o Spinner para os Clicks
        Spinner sClick = (Spinner) findViewById(R.id.dropClick);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dropClick, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sClick.setAdapter(adapter1);
        //TESTE TESTE TESTE
        //TESTE TESTE TESTE
    }
}
