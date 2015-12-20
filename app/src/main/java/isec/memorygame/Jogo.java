package isec.memorygame;

import java.io.Serializable;

/**
 * Created by Luis on 17/12/2015.
 */
public class Jogo implements Serializable {
    String time;
    int njogadas;
    int pontos;

    public Jogo(String time, int njogadas, int pontos) {
        this.time = time;
        this.njogadas = njogadas;
        this.pontos = pontos;
    }

    public String getTime() {
        return time;
    }

    public int getNjogadas() {
        return njogadas;
    }

    public int getPontos() {
        return pontos;
    }

}
