package isec.memorygame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class HistoricoActivity extends AppCompatActivity {
    ArrayList<String> hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        ListView lst = (ListView) findViewById(R.id.listahist);
        hist = new Util().lerFicheiro(getApplicationContext());
        Collections.reverse(hist);
        lst.setAdapter(new ListAdapter(this,hist));

    }

    public void onBotaoLimpa(View v) {
        File dir = getFilesDir();
        File file = new File(dir, "histfile.txt");
        file.delete();
        finish();
        startActivity(getIntent());
    }
}
