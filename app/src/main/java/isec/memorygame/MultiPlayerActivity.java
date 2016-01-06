package isec.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MultiPlayerActivity extends AppCompatActivity {
    Util ut;
    ArrayList<Carta> cartas;
    ArrayList<Integer> viradas = new ArrayList<>();
    private int num_corretas = 0;
    private int num_jogadas = 0;
    private int pontuacao1 = 0;
    private int pontuacao2 = 0;
    private int pontosvencedor = 0;
    private int JogadorJoga;
    private int DoubleClick = 0;
    private SharedPreferences pref;
    private TextView njogadas;
    private TextView nomejog1;
    private TextView pontosjog1;
    TextView nomejog2;
    TextView pontosjog2;
    String turn;
    GridJogoAdapter adapter;

    //Toast
    LayoutInflater inflater;
    View layout;
    TextView text;
    Toast toast;
    GridView gJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        ut = new Util();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cartas = getCartas();
        //Inicialização da variaveis
        gJogo = (GridView) findViewById(R.id.gridViewJogo);
        gJogo.setNumColumns(getNumCol());
        adapter = new GridJogoAdapter(this, cartas);
        gJogo.setAdapter(adapter);


        njogadas = (TextView) findViewById(R.id.nrjogadas);
        njogadas.setText("0");

        final Chronometer counter = (Chronometer) findViewById(R.id.timermulti);
        counter.start();

        //Dados para o jogador 1
        nomejog1 = (TextView) findViewById(R.id.nomejog1);
        pontosjog1 = (TextView) findViewById(R.id.pontos1);
        pontosjog1.setText("0");

        //Dados para o jogador 2
        nomejog2 = (TextView) findViewById(R.id.nomejog2);
        pontosjog2 = (TextView) findViewById(R.id.pontos2);
        pontosjog2.setText("0");

        //Define jogador que joga primeiro
        Random rand = new Random();
        JogadorJoga = rand.nextInt(2);

        //Toast a indicar quem joga

        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        text = (TextView) layout.findViewById(R.id.text);
        text.setTextSize(35);
        turn = getResources().getString(R.string.JogadorAJogar);
        if (JogadorJoga == 0) {
            text.setText(turn + " " + IdentificaJogadores.jog1.getNome());
            nomejog1.setText("* " + IdentificaJogadores.jog1.getNome() + ":");
            nomejog2.setText(IdentificaJogadores.jog2.getNome() + ":");
        } else {
            text.setText(turn + " " + IdentificaJogadores.jog2.getNome());
            nomejog1.setText(IdentificaJogadores.jog1.getNome() + ":");
            nomejog2.setText("* " + IdentificaJogadores.jog2.getNome() + ":");
        }
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = false;
                while (run != true) {
                    run = jogoAcabou();
                }
                counter.stop();

                IdentificaJogadores.jog1.setPontos(pontuacao1);
                IdentificaJogadores.jog2.setPontos(pontuacao2);

                String escreve;
                if (pontuacao1 > pontuacao2) {
                    pontosvencedor = pontuacao1;
                    escreve = "Multiplayer" + " " + IdentificaJogadores.jog1.getNome() + " " + IdentificaJogadores.jog1.getPontos() + " " + num_jogadas + " " + counter.getText().toString() + " " + (pref.getInt("Dificuldade", 0) + 1);
                }else {
                    pontosvencedor = pontuacao2;
                    escreve = "Multiplayer" + " " + IdentificaJogadores.jog2.getNome() + " " + IdentificaJogadores.jog2.getPontos() + " " + num_jogadas + " " + counter.getText().toString() + " " + (pref.getInt("Dificuldade", 0) + 1);
                }



                ut.escreverFicheiro(getApplicationContext(), escreve);


                toast.cancel();
                Intent i = new Intent(MultiPlayerActivity.this, fimMultiActivity.class);
                i.putExtra("id", new Jogo(counter.getText().toString(), num_jogadas, pontosvencedor));
                startActivity(i);
            }
        }).start();

        int pos = pref.getInt("Click", 0);

        if (pos == 0) {
            gJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!(cartas.get(position).descoberta) && !(viradas.size() == 2)) {
                        if (viradas.size() == 1) {
                            if (position == viradas.get(0))
                                return;
                        }
                        jogo(view, position);
                    }
                }
            });
        }
        if (pos == 1) {
            gJogo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!(cartas.get(position).descoberta) && !(viradas.size() == 2)) {
                        if (viradas.size() == 1) {
                            if (position == viradas.get(0))
                                return false;
                        }
                        jogo(view, position);
                    }
                    return false;
                }
            });
        }
        if (pos == 2) {
            DoubleClick = 0;
            gJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoubleClick++;
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {

                        @Override
                        public void run() {
                            DoubleClick = 0;
                        }
                    };

                    if (DoubleClick == 1) {
                        //Single click
                        handler.removeCallbacks(r);
                        handler.postDelayed(r, 700);
                    }
                    if (DoubleClick == 2) {

                        handler.removeCallbacks(r);
                        DoubleClick = 0;
                        if (!(cartas.get(position).descoberta) && !(viradas.size() == 2)) {
                            if (viradas.size() == 1) {
                                if (position == viradas.get(0))
                                    return;
                            }
                            jogo(view, position);
                        }
                    }
                }
            });
        }
    }

    public void jogo(View view, int position) {
        ImageView img = (ImageView) view;
        if (pref.getString("Gallery", "Default").equals("Default"))
            img.setImageResource(cartas.get(position).cartaVirada);
        else {
            Uri uri = Uri.parse(cartas.get(position).cartaViradaS);
            img.setImageBitmap(ut.getBitmap(MultiPlayerActivity.this, uri));
        }
        viradas.add(position);
        if (viradas.size() == 2) {
            Carta carta1 = cartas.get(viradas.get(0));
            Carta carta2 = cartas.get(viradas.get(1));
            if (carta1.id == carta2.id) {
                carta1.setDescoberta(true);
                carta2.setDescoberta(true);
                viradas.clear();
                num_corretas += 2;
                if (JogadorJoga == 0) {
                    pontuacao1 += 5;
                    text.setText(turn + " " + IdentificaJogadores.jog1.getNome());
                    nomejog1.setText("* " + IdentificaJogadores.jog1.getNome() + ":");
                    nomejog2.setText(IdentificaJogadores.jog2.getNome() + ":");
                    toast.show();
                } else {
                    pontuacao2 += 5;
                    nomejog1.setText(IdentificaJogadores.jog1.getNome() + ":");
                    nomejog2.setText("* " + IdentificaJogadores.jog2.getNome() + ":");
                    text.setText(turn + " " + IdentificaJogadores.jog2.getNome());
                    toast.show();
                }
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        for (int i = 0; i < viradas.size(); i++) {
                            ImageView image = (ImageView)gJogo.getChildAt(viradas.get(i));
                            if (pref.getString("Gallery", "Default").equals("Default"))
                                image.setImageResource(cartas.get(viradas.get(i)).cartaPorVirar);
                            else {
                                Uri uri = Uri.parse(cartas.get(viradas.get(i)).cartaPorVirarS);
                                image.setImageBitmap(ut.getBitmap(MultiPlayerActivity.this, uri));
                            }
                        }
                        viradas.clear();
                    }
                }, 1000);
                if (JogadorJoga == 0) {
                    pontuacao1 -= 1;
                    JogadorJoga = 1;
                    nomejog1.setText(IdentificaJogadores.jog1.getNome() + ":");
                    nomejog2.setText("* " + IdentificaJogadores.jog2.getNome() + ":");
                    text.setText(turn + " " + IdentificaJogadores.jog2.getNome());
                    toast.show();
                } else {
                    pontuacao2 -= 1;
                    JogadorJoga = 0;
                    text.setText(turn + " " + IdentificaJogadores.jog1.getNome());
                    nomejog1.setText("* " + IdentificaJogadores.jog1.getNome() + ":");
                    nomejog2.setText(IdentificaJogadores.jog2.getNome() + ":");
                    toast.show();
                }
            }
            num_jogadas++;
            njogadas.setText(num_jogadas + "");
            pontosjog1.setText(pontuacao1 + "");
            pontosjog2.setText(pontuacao2 + "");
        }
    }

    private ArrayList<Carta> getCartas() {
        ArrayList<Carta> cartas = new ArrayList<>();
        int num_cartas = getNum_cartas();
        ArrayList<String> turncard = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        String gallery = pref.getString("Gallery","Default");
        if(!gallery.equals("Default")){
            images = ut.getImages(getApplicationContext(), gallery);
            par = ut.getParIntruso(getApplicationContext(), gallery);
            images.addAll(par);
            turncard = ut.getTurnCard(getApplicationContext(), gallery);
        }
        for(int i = 0; i < (num_cartas / 2);i++){

            Carta carta = null;
            if(gallery.equals("Default")){
                carta = new Carta(i + 1, ut.Images[i], ut.Image);
                if(i > 12)
                    carta.setPar_intruso(true);
            }
            else{
                carta = new Carta(i + 1,images.get(i),turncard.get(0));
                if(i > (images.size() - par.size()))
                    carta.setPar_intruso(true);
            }
            cartas.add(carta);
            cartas.add(carta);
        }
        Collections.shuffle(cartas);
        return cartas;
    }

    public boolean jogoAcabou() {
        return num_corretas == getNum_cartas();
    }

    public int getNumCol(){
        int pos = pref.getInt("Dificuldade",0);
        if(pos == 0)
            return 2;
        else
            return pos + 1;
    }

    public int getNumLin(){
        int pos = pref.getInt("Dificuldade",2);
        return pos + 2;
    }

    public int getNum_cartas(){
        return getNumCol() * getNumLin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toast.cancel();
    }
}
