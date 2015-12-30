package isec.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SinglePlayerActivity extends AppCompatActivity {
    Util ut = new Util();
    ArrayList<Carta> cartas;
    ArrayList<Integer> viradas = new ArrayList<>();
    private int num_corretas = 0;
    private int num_jogadas = 0;
    private int pontuacao = 0;
    private SharedPreferences pref;
    private Jogador jogador;
    private int DoubleClick = 0;
    GridJogoAdapter adapter = null;
    TextView njogadas;
    TextView pontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        jogador = (Jogador) getIntent().getSerializableExtra("jogador");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cartas = getCartas();


        GridView gJogo = (GridView) findViewById(R.id.gridViewJogo);
        gJogo.setNumColumns(getNumCol());

        adapter = new GridJogoAdapter(this, cartas, gJogo.getColumnWidth());
        gJogo.setAdapter(adapter);

        njogadas = (TextView) findViewById(R.id.njogdaslabel);
        njogadas.setText("0");

        final Chronometer counter = (Chronometer) findViewById(R.id.timer);
        counter.start();

        final TextView nome = (TextView) findViewById(R.id.nomeSP);
        nome.setText(jogador.getNome());

        pontos = (TextView) findViewById(R.id.PontoSP);
        pontos.setText("0");

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = false;
                while (run != true) {
                    run = jogoAcabou();
                }
                counter.stop();
                jogador.setPontos(pontuacao);

                String escreve = "SinglePlayer" + " " + jogador.getNome() + " " + jogador.getPontos() + " " + num_jogadas + " " + counter.getText().toString() + " " + (pref.getInt("Dificuldade", 0) + 1);
                ut.escreverFicheiro(getApplicationContext(), escreve);

                Intent i = new Intent(SinglePlayerActivity.this, FimJogoActivity.class);
                i.putExtra("id", new Jogo(counter.getText().toString(), num_jogadas, pontuacao));
                i.putExtra("jogador", jogador);
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
                        jogo(view,position);
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
                        jogo(view,position);
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
                    if(DoubleClick == 2){

                        handler.removeCallbacks(r);
                        DoubleClick = 0;
                        if (!(cartas.get(position).descoberta) && !(viradas.size() == 2)) {
                            if (viradas.size() == 1) {
                                if (position == viradas.get(0))
                                    return;
                            }
                            jogo(view,position);
                        }
                    }
                }
            });
        }
    }

    public void jogo(View view, int position){
        ImageView img = (ImageView) view;
        if(pref.getString("Gallery","Default").equals("Default"))
            img.setImageResource(cartas.get(position).cartaVirada);
        else {
            Uri uri = Uri.parse(cartas.get(position).cartaViradaS);
            img.setImageBitmap(ut.getBitmap(SinglePlayerActivity.this,uri));
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
                pontuacao += 5;
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                        viradas.clear();
                    }
                }, 700);
                pontuacao -= 1;
            }
            num_jogadas++;
            njogadas.setText(num_jogadas + "");
            pontos.setText(pontuacao + "");
        }
    }


    private ArrayList<Carta> getCartas(){
        ArrayList<Carta> cartas = new ArrayList<>();
        int num_cartas = getNum_cartas();
        ArrayList<String> turncard = new ArrayList<>();
        ArrayList<String> par = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        String gallery = pref.getString("Gallery","Default");
        if(!gallery.equals("Default")){
            images = ut.getImages(SinglePlayerActivity.this, gallery);
            par = ut.getParIntruso(getApplicationContext(), gallery);
            images.addAll(par);
            turncard = ut.getTurnCard(getApplicationContext(), gallery);
        }
        for(int i = 0; i < (num_cartas / 2);i++){

            Carta carta = null;
            if(gallery.equals("Default")){
                 carta = new Carta(i + 1, ut.Images[i], ut.Image);
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

    public boolean jogoAcabou(){
        return num_corretas == getNum_cartas();
    }

    public int getNumCol(){
        int pos = pref.getInt("Dificuldade",0);
        if(pos == 0)
            return 2;
        if(pos == 1)
            return 2;
        if(pos == 2)
            return 3;
        if(pos == 3)
            return 4;
        if(pos == 4)
            return 5;
        return 3;
    }


    public int getNumLin(){
        int pos = pref.getInt("Dificuldade",2);
        if(pos == 0)
            return 2;
        if(pos == 1)
            return 3;
        if(pos == 2)
            return 4;
        if(pos == 3)
            return 5;
        if(pos == 4)
            return 6;
        return 3;
    }

    public int getNum_cartas(){
        return getNumCol() * getNumLin();
    }

}
