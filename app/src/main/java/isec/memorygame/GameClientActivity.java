package isec.memorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GameClientActivity extends AppCompatActivity {

    String ip;
    String nome;
    Socket socketGame = null;
    Handler hdl;
    Util ut = new Util();
    SharedPreferences pref;

    ObjectOutputStream output;
    ObjectInputStream input;

    Matrix matrix;

    TextView tJog1;
    TextView tJog2;
    TextView tPJog1;
    TextView tPJog2;
    Chronometer cClock;
    GridView gJogo;

    ArrayList<Carta> cards = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_client);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this,getResources().getString(R.string.Err_NoConnection), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        hdl = new Handler();
        nome = getIntent().getStringExtra("Nome");
        ip = getIntent().getStringExtra("Ip");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        tJog1 = (TextView)findViewById(R.id.GC_Jog1);
        tJog2 = (TextView)findViewById(R.id.GC_Jog2);
        tPJog1 = (TextView)findViewById(R.id.GC_LPjog1);
        tPJog2 = (TextView)findViewById(R.id.GC_LPjog2);
        cClock = (Chronometer)findViewById(R.id.GC_CClock);
        gJogo = (GridView)findViewById(R.id.GC_Grid);


        client();

    }

    void client() {
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
                    hdl.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.Err_UnableConnect, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    return;
                }
                commThread.start();
            }
        });
        t.start();
    }


    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new ObjectInputStream(socketGame.getInputStream());
                matrix = (Matrix)input.readObject();
                matrix.addJogador(nome);
                output = new ObjectOutputStream(socketGame.getOutputStream());
                output.writeObject(matrix);

                hdl.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMatrix();
                    }
                });

                while(true) {
                    Message msg = null;

                    msg = (Message) input.readObject();
                    final Message finalMsg = msg;
                    hdl.post(new Runnable() {
                        @Override
                        public void run() {
                            recebe(finalMsg);
                        }
                    });

                    hdl.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                output.writeObject(joga());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }


            } catch (Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"The game was finished",Toast.LENGTH_LONG).show();
            }
        }
    });


    private void displayMatrix(){
        tJog1.setText(matrix.getNome(0));
        tJog2.setText(matrix.getNome(1));

        tPJog1.setText("P: 0");
        tPJog2.setText("P: 0");

        gJogo.setNumColumns(matrix.col);
        gJogo.setAdapter(new GridMultiAdapter(getCartas(),matrix.col,getApplicationContext()));

    }
    private ArrayList<Carta> getCartas(){

        ArrayList<Integer> ids = matrix.getMatrix();
        ArrayList<String> turncard = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        String gallery = pref.getString("Gallery","Default");
        if(!gallery.equals("Default")){
            images = ut.getImages(GameClientActivity.this, gallery);
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
        Handler handler = new Handler();

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
                img.setImageBitmap(ut.getBitmap(GameClientActivity.this, uri));
                if(!cards.get(pos.get(i)).descoberta) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Uri uri = Uri.parse(cards.get(pos.get(finalI)).cartaPorVirarS);
                            img.setImageBitmap(ut.getBitmap(GameClientActivity.this, uri));
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



    public Message joga(){
        final ArrayList<Integer> viradas = new ArrayList<>();
        final Message msg = new Message();
        gJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!(cards.get(position).descoberta) && !(viradas.size() == 2)) {
                    if (viradas.size() == 1) {
                        if (position == viradas.get(0))
                            return;
                    }
                    final ImageView img = (ImageView) view;
                    if(pref.getString("Gallery","Default").equals("Default"))
                        img.setImageResource(cards.get(position).cartaVirada);
                    else {
                        Uri uri = Uri.parse(cards.get(position).cartaViradaS);
                        img.setImageBitmap(ut.getBitmap(GameClientActivity.this,uri));
                    }
                    viradas.add(position);

                    if (viradas.size() == 2) {
                        Carta carta1 = cards.get(viradas.get(0));
                        Carta carta2 = cards.get(viradas.get(1));
                        msg.addNum(viradas.get(0));
                        msg.addNum(viradas.get(1));

                        if (carta1.id == carta2.id) {
                            carta1.setDescoberta(true);
                            carta2.setDescoberta(true);

                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    if(pref.getString("Gallery","Default").equals("Default"))
                                        img.setImageResource(cards.get(position).cartaPorVirar);
                                    else {
                                        Uri uri = Uri.parse(cards.get(position).cartaPorVirarS);
                                        img.setImageBitmap(ut.getBitmap(GameClientActivity.this,uri));
                                    }
                                }
                            }, 700);

                        }
                        viradas.clear();
                    }
                }
            }
        });
        return msg;

    }

}
