package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddGalleryActivity extends AppCompatActivity {
    EditText et;
    Util util = new Util();
    ArrayList<String> gallerys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery);

        et = (EditText)findViewById(R.id.AG_addtext);
        Button add = (Button)findViewById(R.id.AG_BAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = et.getText().toString();
                nome.replaceAll(" ", "");
                if(nome.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.Err_FaltaNome, Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(nome.length() > 12) {
                    Toast.makeText(getApplicationContext(), R.string.Err_Nomegrande, Toast.LENGTH_LONG).show();
                    et.setText("");
                    return;
                }
                gallerys = util.getGallerys(getApplicationContext());
                if(gallerys.contains(nome)){
                    Toast.makeText(getApplicationContext(), R.string.Err_Nomeexistente, Toast.LENGTH_LONG).show();
                    et.setText("");
                    return;
                }
                util.addGallery(getApplicationContext(),nome);
                Intent i = new Intent(AddGalleryActivity.this,AddImageGallery.class);
                i.putExtra("nome",nome);
                startActivity(i);
            }
        });




    }
}
