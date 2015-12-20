package isec.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    ArrayList<Carta> cartas = getCartas();
    ArrayList<Integer> viradas = new ArrayList<>();
    private int num_corretas = 0;
    private int num_jogadas = 0;
    private int pontuacao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        GridView gJogo = (GridView)findViewById(R.id.gridViewJogo);
        gJogo.setNumColumns(ut.numColLin);
        final GridJogoAdapter adapter = new GridJogoAdapter(this, cartas);
        gJogo.setAdapter(adapter);

        final TextView njogadas = (TextView)findViewById(R.id.njogdaslabel);
        njogadas.setText("0");

        final TextView nome = (TextView) findViewById(R.id.nometext);
        nome.setText(IdentificaJogador.jog.getNome());

        final TextView pontos = (TextView) findViewById(R.id.pontostext);
        pontos.setText("0");

        final Chronometer counter = (Chronometer)findViewById(R.id.timer);
        counter.start();

        gJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(cartas.get(position).isDescoberta()) && !(viradas.size() == 2)) {
                    if(viradas.size() == 1){
                        if(position == viradas.get(0))
                            return;
                    }
                    ImageView img = (ImageView) view;
                    img.setImageResource(cartas.get(position).getCartaVirada());
                    viradas.add(position);

                    if (viradas.size() == 2) {
                        Carta carta1 = cartas.get(viradas.get(0));
                        Carta carta2 = cartas.get(viradas.get(1));
                        if (carta1.getId() == carta2.getId()) {
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
                            }, 1000);
                            pontuacao -= 1;
                        }
                        num_jogadas++;
                        njogadas.setText(num_jogadas + "");
                        pontos.setText(pontuacao + "");
                    }
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = false;
                while(run != true){
                    run = jogoAcabou();
                }
                counter.stop();
                IdentificaJogador.jog.setPontos(pontuacao);
                Intent i = new Intent(SinglePlayerActivity.this,FimJogoActivity.class);
                i.putExtra("id", new Jogo(counter.getText().toString(), num_jogadas, pontuacao));
                startActivity(i);
            }
        }).start();
    }

    private ArrayList<Carta> getCartas(){
        ArrayList<Carta> cartas = new ArrayList<>();

        for(int i = 0; i < (ut.num_cartas / 2);i++){
            Carta carta = new Carta(i + 1,ut.Images[i],ut.Image);
            cartas.add(carta);
            cartas.add(carta);
        }
        Collections.shuffle(cartas);
        return cartas;
    }

    public boolean jogoAcabou(){
        return num_corretas == ut.num_cartas;
    }
}
