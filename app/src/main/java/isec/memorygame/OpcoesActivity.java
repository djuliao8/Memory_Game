package isec.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OpcoesActivity extends AppCompatActivity {
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Criar o Spinner para os Niveis
        final Spinner sNivel = (Spinner) findViewById(R.id.dropNivel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dropNivel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sNivel.setAdapter(adapter);
        sNivel.setSelection(pref.getInt("Dificuldade",2));

        //Criar o Spinner para os Clicks
        Spinner sClick = (Spinner) findViewById(R.id.dropClick);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dropClick, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sClick.setAdapter(adapter1);
        sClick.setSelection(pref.getInt("Click",0));


        //Para tornar o tempo do duplo click visivel
        final TextView tv = (TextView) findViewById(R.id.Tempolabel);
        final EditText et = (EditText) findViewById(R.id.editText);

        sNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Dificuldade",position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int pos = pref.getInt("Dificuladade",0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Dificuldade",pos);
                editor.apply();
            }
        });

        sClick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Click",position);

                if (position == 2) {
                    tv.setVisibility(View.VISIBLE);
                    et.setVisibility(View.VISIBLE);
                    editor.putString("autoSave", et.getText().toString());
                } else {
                    tv.setVisibility(View.INVISIBLE);
                    et.setVisibility(View.INVISIBLE);
                }
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int pos = pref.getInt("Click",0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Click",pos);
                editor.apply();
                tv.setVisibility(View.INVISIBLE);
                et.setVisibility(View.INVISIBLE);
            }
        });

    }
}
