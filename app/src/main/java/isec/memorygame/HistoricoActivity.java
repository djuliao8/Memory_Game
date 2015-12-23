package isec.memorygame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HistoricoActivity extends AppCompatActivity {
    ArrayList<String> hist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        ListView lst = (ListView) findViewById(R.id.listahist);
        hist = new ArrayList<>();
        //Ler ficheiro

        try {
            InputStream inputStream = openFileInput("histfile");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);

                }

                inputStream.close();
                hist.add(stringBuilder.toString());
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hist);
                lst.setAdapter(arrayAdapter);
            }
        } catch (FileNotFoundException e) {
            System.err.println("O ficheiro não foi encontrado");
        } catch (IOException e) {
            System.err.println("Erro no tratamento do ficheiro");
        }

    }


}
