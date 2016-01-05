package isec.memorygame;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HostGamectivity extends AppCompatActivity {
    Util ut = new Util();

    TextView labelIp;
    Spinner sCol;
    Spinner sLin;
    Spinner sPar;
    EditText eNum;
    Button bCreate;

    String aCol [] = new String[] {"2", "3", "4", "5"};
    String aLin [] = new String[] {"2", "3", "4", "5","6"};

    String Col;
    String Lin;
    String Par;
    String ip;
    String Num;
    String Nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_gamectivity);
        Nome = getIntent().getStringExtra("Nome");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this,getResources().getString(R.string.Err_NoConnection), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        labelIp = (TextView)findViewById(R.id.HG_TIP);
        ip = ut.getLocalIpAddress();
        labelIp.setText(ip);


        sCol = (Spinner) findViewById(R.id.HG_SCol);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, aCol);
        sCol.setAdapter(adapter);
        sCol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Col = aCol[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Col = aCol[0];
            }
        });

        sLin = (Spinner) findViewById(R.id.HG_SLin);
        ArrayAdapter<String> adapterLin = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, aLin);
        sLin.setAdapter(adapterLin);
        sLin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Lin = aLin[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Lin = aLin[0];
            }
        });

        sPar = (Spinner) findViewById(R.id.HG_SPar);
        ArrayAdapter<String> adapterPar = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, aCol);
        sPar.setAdapter(adapterPar);
        sPar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Par = aCol[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Par = aCol[0];
            }
        });

        eNum = (EditText)findViewById(R.id.HG_ENum);



        bCreate = (Button)findViewById(R.id.HG_BCreate);
        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = eNum.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                if(!Num.equals(""))
                    smsManager.sendTextMessage(Num, null, ip, null, null);

                Intent i = new Intent(HostGamectivity.this, GameActivity.class);
                Matrix matrix = new Matrix(Integer.parseInt(Lin),Integer.parseInt(Col));
                matrix.addJogador(Nome);
                i.putExtra("Game",matrix);
                i.putExtra("ID",ut.SERVER);
                startActivity(i);

            }
        });

    }

}
