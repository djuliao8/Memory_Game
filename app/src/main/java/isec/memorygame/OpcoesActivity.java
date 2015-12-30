package isec.memorygame;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class OpcoesActivity extends AppCompatActivity {
    SharedPreferences pref;
    Util util = new Util();
    Dialog dialog;
    SeekBar sk;
    TextView progres;

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

        //Popup para o tempo
        dialog = new Dialog(OpcoesActivity.this);
        dialog.setContentView(R.layout.tempo_layout);
        dialog.setTitle(R.string.OP_LTempo);

        sk = (SeekBar)dialog.findViewById(R.id.OP_SeekBar);
        progres = (TextView)dialog.findViewById(R.id.OP_Progress);
        progres.setText("0/25");

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressvalue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressvalue = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progres.setText(progressvalue + "/" + seekBar.getMax());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progres.setText(progressvalue + "/" + seekBar.getMax());

            }
        });

        Button cancel = (Button)dialog.findViewById(R.id.OP_Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button add = (Button)dialog.findViewById(R.id.OP_AddTempo);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Tempo",sk.getProgress());
                dialog.dismiss();
            }
        });



        //Spinner para galerias
        Spinner sGallery = (Spinner)findViewById(R.id.dropGallery);
        final ArrayList<String> gallery = util.getGallerysCompleted(getApplicationContext());
        gallery.add("Default");
        String [] string = new String[gallery.size()];
        string = gallery.toArray(string);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,string);
        sGallery.setAdapter(adapter2);
        sGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Gallery", gallery.get(position));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Gallery", "Default");
                editor.apply();
            }
        });



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
                editor.putInt("Dificuldade", pos);
                editor.apply();
            }
        });

        sClick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Click",position);

                if (position == 2) {
                    dialog.show();
                } else {
                    dialog.dismiss();
                }
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int pos = pref.getInt("Click",0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Click",pos);
                editor.apply();
                tv.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
            }
        });

        Button galleryButton = (Button)findViewById(R.id.OP_GerirGall);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpcoesActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OpcoesActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
