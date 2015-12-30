package isec.memorygame;

import java.util.ArrayList;

/**
 * Created by Luis on 30/12/2015.
 */
public class Matrix {
    int col,lin;
    int matrix[][];
    Util ut = new Util();
    ArrayList<Jogador> jog = new ArrayList<>();

    Matrix(int lin,int col){
        this.lin = lin;
        this.col = col;
        matrix = ut.getRandMatrix(lin,col);
    }

    public void addJogador(String nome){
        jog.add(new Jogador(nome,0));
    }
    public int getNumCards(){
        return lin * col;
    }
}
