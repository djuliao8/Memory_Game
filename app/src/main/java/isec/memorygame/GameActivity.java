package isec.memorygame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;

import static android.view.Gravity.*;

public class GameActivity extends AppCompatActivity {

    //Sockets
    ServerSocket svSocket;
    Socket socketGame = null;
    ObjectOutputStream output;
    ObjectInputStream input;


    //Game Activity
    TextView tJog1;
    TextView tJog2;
    TextView tPJog1;
    TextView tPJog2;
    Chronometer cClock;
    GridView gJogo;

    //Client Layout
    EditText eIp;
    EditText eNomeCli;

    //Server layout
    TextView tIp;
    Spinner sCol;
    Spinner sLin;
    EditText eNome;
    EditText eNum;

    //Utils
    Handler func;
    Util ut;
    SharedPreferences pref;

    //Cliente ou Servidor
    int mode;

    //
    AlertDialog.Builder builder;
    AlertDialog.Builder builderCli;
    ProgressDialog waitDialog;

    //
    Message msg = null;

    ArrayList<Integer>viradas = new ArrayList<>();
    ArrayList<Carta> cards = new ArrayList<>();
    String aCol [] = new String[] {"2", "3", "4", "5"};
    String aLin [] = new String[] {"2", "3", "4", "5","6"};
    Matrix matrix = null;
    String Col;
    String Lin;
    String Num;
    String Nome = "";
    String ip;

    int pontosJog1 = 0;
    int pontosJog2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        func = new Handler();
        ut = new Util();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mode = getIntent().getIntExtra("ID",ut.SERVER);

        tJog1 = (TextView)findViewById(R.id.GA_Jog1);
        tJog2 = (TextView)findViewById(R.id.GA_Jog2);
        tPJog1 = (TextView)findViewById(R.id.GA_LPjog1);
        tPJog2 = (TextView)findViewById(R.id.GA_LPjog2);
        cClock = (Chronometer)findViewById(R.id.GA_CClock);
        gJogo = (GridView)findViewById(R.id.GA_Grid);


        if(mode == ut.SERVER)
            initPopup();
        else if(mode == ut.CLI)
            initPopupCli();


    }


    //Server Functions
    void initPopup() {

        builder = new AlertDialog.Builder(GameActivity.this);
        LayoutInflater inflater = GameActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.server_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        tIp = (TextView)dialogView.findViewById(R.id.SV_LIp);
        sCol = (Spinner)dialogView.findViewById(R.id.SV_SCol);
        sLin = (Spinner)dialogView.findViewById(R.id.SV_SLin);
        eNome = (EditText)dialogView.findViewById(R.id.SV_ENome);
        eNum = (EditText)dialogView.findViewById(R.id.SV_ENum);

        ip = ut.getLocalIpAddress();
        tIp.setText(ip);

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

        builder.setPositiveButton(R.string.SV_Host, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Nome = eNome.getText().toString();
                if (Nome.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.Err_FaltaNome, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else if (Nome.length() > 7) {
                    Toast.makeText(getApplicationContext(), R.string.Err_Nomrgrande7, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
                Num = eNum.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                if (!Num.equals(""))
                    smsManager.sendTextMessage(Num, null, ip, null, null);

                matrix = new Matrix(Integer.parseInt(Lin), Integer.parseInt(Col));

                dialog.cancel();
                launchDialog();


            }
        });
        builder.setNegativeButton(R.string.SV_Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        builder.create();
        builder.show();
    }
    public void launchDialog(){
        waitDialog = new ProgressDialog(GameActivity.this);
        waitDialog.setTitle(getResources().getString(R.string.GA_waitPl));
        waitDialog.setMessage(getResources().getString(R.string.GA_Aguarde));
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (svSocket != null) {
                    try {
                        svSocket.close();
                    } catch (IOException e) {
                    }
                    svSocket = null;
                }
            }
        });
        waitDialog.show();

        server();
    }
    void server(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    svSocket = new ServerSocket(ut.PORT);
                    svSocket.setReuseAddress(true);
                    svSocket.setSoTimeout(ut.WAIT);
                    socketGame = svSocket.accept();
                    svSocket.close();
                    svSocket = null;

                } catch (SocketTimeoutException e) {
                    socketGame = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                func.post(new Runnable() {
                    @Override
                    public void run() {
                        waitDialog.dismiss();
                        if (socketGame == null) {
                            Toast.makeText(getApplicationContext(), R.string.Err_NoOne, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                commThread.start();
            }
        });
        t.start();
    }

    //Client functions
    void initPopupCli(){
        builderCli = new AlertDialog.Builder(GameActivity.this);
        LayoutInflater inflater = GameActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.clint_layout, null);
        builderCli.setView(dialogView);
        builderCli.setCancelable(false);
        eNomeCli = (EditText)dialogView.findViewById(R.id.CL_ENome);
        eIp = (EditText)dialogView.findViewById(R.id.CL_EIp);

        builderCli.setPositiveButton(R.string.JG_LJoin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Nome = eNomeCli.getText().toString();
                ip = eIp.getText().toString();
                if (Nome.equals("") && ip.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.Err_FaltaNome, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else if (Nome.length() > 7) {
                    Toast.makeText(getApplicationContext(), R.string.Err_Nomrgrande7, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    client();
                    dialog.cancel();
                }
            }
        });
        builderCli.setNegativeButton(R.string.SV_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builderCli.create();
        builderCli.show();
    }

    void client(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketGame = new Socket(ip,ut.PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                if (socketGame == null) {
                    func.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.Err_UnableConnect, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    finish();
                }else
                    commThread.start();
            }
        });
        t.start();
    }

    Thread acabou = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean run = false;
            while (run != true) {
                run = jogoAcabou();
            }
            if(run == true) {
                Intent i = new Intent(GameActivity.this, FimJogoRedeActivity.class);
                cClock.stop();
                i.putExtra("Jog1", new Jogador(matrix.getNome(0), pontosJog1));
                i.putExtra("Jog2", new Jogador(matrix.getNome(1), pontosJog2));
                i.putExtra("Tempo",cClock.getText().toString());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        }



    });

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                geraMatrixInicial();
                acabou.start();
                gJogo.setOnItemClickListener(null);
                msg = null;
                func.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMatrix();
                    }
                });
                if(mode == ut.SERVER) {
                    while (true) {
                        turnCards();
                        msg = (Message) input.readObject();
                        if (msg != null) {
                            func.post(new Runnable() {
                                @Override
                                public void run() {
                                    recebe(msg);
                                    msg = null;
                                }
                            });

                        }
                    }
                }
                else if(mode == ut.CLI){
                    while(true) {
                        msg = (Message) input.readObject();
                        if (msg != null) {
                            func.post(new Runnable() {
                                @Override
                                public void run() {
                                    recebe(msg);
                                    msg = null;
                                }
                            });

                            turnCards();
                        }
                    }
                }

            } catch (Exception e) {
                func.post(new Runnable() {
                    @Override
                    public void run() {
                        if(socketGame != null) {
                            try {
                                socketGame.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        finish();
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.Err_GameOver), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
    });

    void geraMatrixInicial() throws Exception{
        output = new ObjectOutputStream(socketGame.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socketGame.getInputStream());
        do {
            if (mode == ut.SERVER) {
                matrix.addJogador(Nome);
                output.writeObject(matrix);
                matrix = (Matrix) input.readObject();
            } else if (mode == ut.CLI) {
                matrix = (Matrix) input.readObject();
                matrix.addJogador(Nome);
                output.writeObject(matrix);
            }
        }while(matrix == null);
    }


    void displayMatrix(){
        cClock.start();
        tJog1.setText(matrix.getNome(0));
        tJog2.setText(matrix.getNome(1));
        tPJog1.setText(pontosJog1 + "");
        tPJog2.setText(pontosJog2 + "");
        gJogo.setNumColumns(matrix.col);
        gJogo.setAdapter(new GridMultiAdapter(getCartas(), matrix.col, getApplicationContext()));
    }

    void turnCards(){
        gJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!(cards.get(position).descoberta) && !(viradas.size() == 2)) {
                    if (viradas.size() == 1) {
                        if (position == viradas.get(0))
                            return;
                    }
                    final ImageView img = (ImageView) view;
                    if (pref.getString("Gallery", "Default").equals("Default"))
                        img.setImageResource(cards.get(position).cartaVirada);
                    else {
                        Uri uri = Uri.parse(cards.get(position).cartaViradaS);
                        img.setImageBitmap(ut.getBitmap(GameActivity.this, uri));
                    }
                    viradas.add(position);
                    if (viradas.size() == 2) {
                        gJogo.setOnItemClickListener(null);
                        msg = verificaCartas();
                        try {
                            output.writeObject(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    Message verificaCartas(){
        Message msg = new Message();
        Carta carta1 = cards.get(viradas.get(0));
        Carta carta2 = cards.get(viradas.get(1));
        msg.addNum(viradas.get(0));
        msg.addNum(viradas.get(1));

        if (carta1.id == carta2.id) {
            carta1.setDescoberta(true);
            carta2.setDescoberta(true);
            viradas.clear();
            if(mode == ut.SERVER) {
                pontosJog1 += 5;
                tPJog1.setText(pontosJog1 + "");
                msg.addPoint(pontosJog1);
            }
            else if(mode == ut.CLI){
                pontosJog2 += 5;
                tPJog2.setText(pontosJog2 + "");
                msg.addPoint(pontosJog2);
            }



        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    for(int i = 0; i < viradas.size();i++) {
                        ImageView img = (ImageView)gJogo.getChildAt(viradas.get(i));
                        if (pref.getString("Gallery", "Default").equals("Default"))
                            img.setImageResource(cards.get(viradas.get(i)).cartaPorVirar);
                        else {
                            Uri uri = Uri.parse(cards.get(viradas.get(i)).cartaPorVirarS);
                            img.setImageBitmap(ut.getBitmap(GameActivity.this, uri));
                        }
                    }
                    viradas.clear();
                }
            }, 700);
            if(mode == ut.SERVER) {
                pontosJog1 -= 1;
                tPJog1.setText(pontosJog1 + "");
                msg.addPoint(pontosJog1);
            }
            else if(mode == ut.CLI){
                pontosJog2 -= 1;
                tPJog2.setText(pontosJog2 + "");
                msg.addPoint(pontosJog2);
            }
        }
        return msg;
    }
    private ArrayList<Carta> getCartas(){

        ArrayList<Integer> ids = matrix.getMatrix();
        ArrayList<String> turncard = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        String gallery = pref.getString("Gallery","Default");
        if(!gallery.equals("Default")){
            images = ut.getImages(GameActivity.this, gallery);
            par = ut.getParIntruso(getApplicationContext(), gallery);
            images.addAll(par);
            turncard = ut.getTurnCard(getApplicationContext(), gallery);
        }
        for(int i = 0; i < ids.size() ;i++){

            Carta carta = null;
            if(gallery.equals("Default")){
                carta = new Carta(ids.get(i), ut.Images[ids.get(i)], ut.Image);
            }
            else{
                carta = new Carta(ids.get(i),images.get(ids.get(i)),turncard.get(0));
                if(i > (images.size() - par.size()))
                    carta.setPar_intruso(true);
            }
            cards.add(carta);
        }
        return cards;
    }


    public void recebe(Message msg){
        final ArrayList<Integer> pos = msg.getNum();
        int point = msg.getPoint();
        Handler handler = new Handler();

        if(mode == ut.SERVER) {
            pontosJog2 = point;
            tPJog2.setText(pontosJog2 + "");
        }else if(mode == ut.CLI) {
            pontosJog1 = point;
            tPJog1.setText(pontosJog1 + "");
        }

        checkId(pos);

        for(int i = 0; i < pos.size();i++) {
            final ImageView img = (ImageView)gJogo.getChildAt(pos.get(i));
            final int finalI = i;
            if (pref.getString("Gallery", "Default").equals("Default")) {
                img.setImageResource(cards.get(pos.get(i)).cartaVirada);
                if(!cards.get(pos.get(i)).descoberta) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            img.setImageResource(cards.get(pos.get(finalI)).cartaPorVirar);
                        }
                    }, 600);
                }
            }else {
                Uri uri = Uri.parse(cards.get(pos.get(i)).cartaViradaS);
                img.setImageBitmap(ut.getBitmap(GameActivity.this, uri));
                if(!cards.get(pos.get(i)).descoberta) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Uri uri = Uri.parse(cards.get(pos.get(finalI)).cartaPorVirarS);
                            img.setImageBitmap(ut.getBitmap(GameActivity.this, uri));
                        }
                    }, 600);
                }
            }
        }
    }

    public void checkId(ArrayList<Integer> pos){
        Carta carta1 = cards.get(pos.get(0));
        Carta carta2 = cards.get(pos.get(1));
        if(carta1.id == carta2.id){
            carta1.setDescoberta(true);
            carta2.setDescoberta(true);
        }
    }

    public boolean jogoAcabou(){
        int size = cards.size();
        int descobertas = 0;
        for(int i = 0; i < size; i++){
            if(cards.get(i).descoberta)
                descobertas++;
        }
        return size == descobertas && size != 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(socketGame != null) {
            try {
                socketGame.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
