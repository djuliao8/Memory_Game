package isec.memorygame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OpcoesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);
        //Criar o Spinner para os Niveis
        final Spinner sNivel = (Spinner) findViewById(R.id.dropNivel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dropNivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sNivel.setAdapter(adapter);

        //Criar o Spinner para os Clicks
        Spinner sClick = (Spinner) findViewById(R.id.dropClick);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dropClick, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sClick.setAdapter(adapter1);


        //Para tornar o tempo do duplo click visivel
        final TextView tv = (TextView) findViewById(R.id.Tempolabel);
        final EditText et = (EditText) findViewById(R.id.editText);

        sClick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    tv.setVisibility(View.VISIBLE);
                    et.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.INVISIBLE);
                    et.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                tv.setVisibility(View.INVISIBLE);
                et.setVisibility(View.INVISIBLE);
            }
        });

    }
}
