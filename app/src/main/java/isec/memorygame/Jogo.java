package isec.memorygame;

import java.io.Serializable;

/**
 * Created by Luis on 17/12/2015.
 */
public class Jogo implements Serializable {
    String time;
    int njogadas;
    int pontuacao;

    public Jogo(String time, int njogadas, int pontuacao) {
        this.time = time;
        this.njogadas = njogadas;
        this.pontuacao = pontuacao;
    }
}
