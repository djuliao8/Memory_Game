package isec.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SinglePlayerActivity extends AppCompatActivity {
    Util ut;
    ArrayList<Carta> cartas;
    ArrayList<Integer> viradas = new ArrayList<>();
    private int num_corretas = 0;
    private int num_jogadas = 0;
    private int pontuacao = 0;
    private SharedPreferences pref;
    private Jogador jogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        jogador = (Jogador) getIntent().getSerializableExtra("jogador");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cartas = getCartas();
        ut = new Util();

        GridView gJogo = (GridView) findViewById(R.id.gridViewJogo);
        gJogo.setNumColumns(getNumCol());

        final GridJogoAdapter adapter = new GridJogoAdapter(this, cartas, gJogo.getColumnWidth());
        gJogo.setAdapter(adapter);

        final TextView njogadas = (TextView) findViewById(R.id.njogdaslabel);
        njogadas.setText("0");

        final Chronometer counter = (Chronometer) findViewById(R.id.timer);
        counter.start();

        final TextView nome = (TextView) findViewById(R.id.nomeSP);
        nome.setText(jogador.getNome());

        final TextView pontos = (TextView) findViewById(R.id.PontoSP);
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
                        ImageView img = (ImageView) view;
                        img.setImageResource(cartas.get(position).cartaVirada);
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
                                }, 800);
                                pontuacao -= 1;
                            }
                            num_jogadas++;
                            njogadas.setText(num_jogadas + "");
                            pontos.setText(pontuacao + "");
                        }
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
                        ImageView img = (ImageView) view;
                        img.setImageResource(cartas.get(position).cartaVirada);
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
                                }, 800);
                                pontuacao -= 1;
                            }
                            num_jogadas++;
                            njogadas.setText(num_jogadas + "");
                            pontos.setText(pontuacao + "");
                        }
                    }
                    return false;
                }
            });
        }
        if (pos == 2) {
            View.OnTouchListener cardDoubleTouchListener = new View.OnTouchListener() {

                boolean firstTouch = false;
                Carta currentCard = null;
                long time;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() != MotionEvent.ACTION_DOWN) {
                        return true;
                    }
                    
                    return true;
                }


            };
        }
    }


    private ArrayList<Carta> getCartas(){
        ArrayList<Carta> cartas = new ArrayList<>();
        int num_cartas = getNum_cartas();
        for(int i = 0; i < (num_cartas / 2);i++){
            Carta carta = new Carta(i + 1,ut.Images[i],ut.Image);
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
