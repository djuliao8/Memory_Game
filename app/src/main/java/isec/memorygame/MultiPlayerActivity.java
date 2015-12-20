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

public class MultiPlayerActivity extends AppCompatActivity {
    Util ut = new Util();
    ArrayList<Carta> cartas = getCartas();
    ArrayList<Integer> viradas = new ArrayList<>();
    private int num_corretas = 0;
    private int num_jogadas = 0;
    private int pontuacao1 = 0;
    private int pontuacao2 = 0;
    private int pontosvencedor = 0;
    private boolean acertou = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        GridView gJogo = (GridView) findViewById(R.id.gridViewJogo);
        gJogo.setNumColumns(ut.numCol);
        final GridJogoAdapter adapter = new GridJogoAdapter(this, cartas);
        gJogo.setAdapter(adapter);

        final TextView njogadas = (TextView) findViewById(R.id.nrjogadas);
        njogadas.setText("0");

        final Chronometer counter = (Chronometer) findViewById(R.id.timermulti);
        counter.start();

        //Dados para o jogador 1
        final TextView nomejog1 = (TextView) findViewById(R.id.nomejog1);
        nomejog1.setText(IdentificaJogadores.jog1.getNome());

        final TextView pontosjog1 = (TextView) findViewById(R.id.pontos1);
        pontosjog1.setText("0");

        //Dados para o jogador 2
        final TextView nomejog2 = (TextView) findViewById(R.id.nomejog2);
        nomejog2.setText(IdentificaJogadores.jog2.getNome());

        final TextView pontosjog2 = (TextView) findViewById(R.id.pontos2);
        pontosjog2.setText("0");

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean run = false;
                while (run != true) {
                    run = jogoAcabou();
                }
                counter.stop();

                if (pontuacao1 > pontuacao2)
                    pontosvencedor = pontuacao1;
                else
                    pontosvencedor = pontuacao2;

                IdentificaJogadores.jog1.setPontos(pontuacao1);
                IdentificaJogadores.jog2.setPontos(pontuacao2);
                Intent i = new Intent(MultiPlayerActivity.this, fimMultiActivity.class);
                i.putExtra("id", new Jogo(counter.getText().toString(), num_jogadas, pontosvencedor));
                startActivity(i);
            }
        }).start();

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
                            if (acertou == true)
                                pontuacao1 += 5;
                            else
                                pontuacao2 += 5;
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    viradas.clear();
                                }
                            }, 1000);
                            if (acertou == true) {
                                pontuacao1 -= 1;
                                acertou = false;
                            } else {
                                pontuacao2 -= 1;
                                acertou = true;
                            }
                        }
                        num_jogadas++;
                        njogadas.setText(num_jogadas + "");
                        pontosjog1.setText(pontuacao1 + "");
                        pontosjog2.setText(pontuacao2 + "");
                    }
                }
            }
        });

    }

    private ArrayList<Carta> getCartas() {
        ArrayList<Carta> cartas = new ArrayList<>();

        for (int i = 0; i < (ut.num_cartas / 2); i++) {
            Carta carta = new Carta(i + 1, ut.Images[i], ut.Image);
            cartas.add(carta);
            cartas.add(carta);
        }
        Collections.shuffle(cartas);
        return cartas;
    }

    public boolean jogoAcabou() {
        return num_corretas == ut.num_cartas;
    }
}
