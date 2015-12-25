package isec.memorygame;


import java.io.Serializable;

public class Jogador implements Serializable{
    String nome;
    int pontos;

    public Jogador(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
