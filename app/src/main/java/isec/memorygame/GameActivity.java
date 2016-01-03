package isec.memorygame;

import android.app.ProgressDialog;
import android.content.DialogInterface;;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    ServerSocket svSocket;
    Socket socketGame;
    Matrix matrix;
    Util ut = new Util();
    ObjectOutputStream output;
    ObjectInputStream input;
    ProgressDialog waitDialog;
    Handler hdl;
    SharedPreferences pref;

    TextView tJog1;
    TextView tJog2;
    TextView tPJog1;
    TextView tPJog2;
    Chronometer cClock;
    GridView gJogo;

    ArrayList<Carta>cards;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        hdl = new Handler();
        matrix = (Matrix)getIntent().getSerializableExtra("Game");

        tJog1 = (TextView)findViewById(R.id.GA_Jog1);
        tJog2 = (TextView)findViewById(R.id.GA_Jog2);
        tPJog1 = (TextView)findViewById(R.id.GA_LPjog1);
        tPJog2 = (TextView)findViewById(R.id.GA_LPjog2);
        cClock = (Chronometer)findViewById(R.id.GA_CClock);
        gJogo = (GridView)findViewById(R.id.GA_Grid);

        server();
    }

    public void server() {


        launchDialog();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    svSocket = new ServerSocket(ut.PORT);
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
                hdl.post(new Runnable() {
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


    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(socketGame.getOutputStream());
                output.writeObject(matrix);

                input = new ObjectInputStream(socketGame.getInputStream());
                matrix = (Matrix)input.readObject();

                hdl.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMatrix();
                    }
                });

                while(true) {
                    Message msg = null;

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
                    msg = (Message) input.readObject();
                    final Message finalMsg = msg;
                    hdl.post(new Runnable() {
                        @Override
                        public void run() {
                            recebe(finalMsg);
                        }
                    });
                }


            } catch (Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"The game was finished",Toast.LENGTH_SHORT).show();
            }
        }
    });


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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void displayMatrix(){
        tJog1.setText(matrix.getNome(0));
        tJog2.setText(matrix.getNome(1));

        tPJog1.setText("P: 0");
        tPJog2.setText("P: 0");

        gJogo.setNumColumns(matrix.col);
        cards = getCartas();
        gJogo.setAdapter(new GridMultiAdapter(cards, matrix.col, getApplicationContext()));

    }
    private ArrayList<Carta> getCartas(){
        ArrayList<Carta> cartas = new ArrayList<>();
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
            cartas.add(carta);
        }
        return cartas;
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
                        img.setImageBitmap(ut.getBitmap(GameActivity.this,uri));
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
                                        img.setImageBitmap(ut.getBitmap(GameActivity.this,uri));
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
