package isec.memorygame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Luis on 30/12/2015.
 */
public class Matrix implements Serializable {
    int col,lin,turn;
    ArrayList<Integer> matrix = new ArrayList<>();
    ArrayList<Jogador> jog = new ArrayList<>();

    Matrix(int lin,int col){
        this.lin = lin;
        this.col = col;
        if(Math.random() < 0.5){turn = 0;}
        else{turn = 1;}
        getRandMatrix(lin, col);

    }

    public ArrayList<Integer>getMatrix(){
        return matrix;
    }

    public void addJogador(String nome){
        jog.add(new Jogador(nome,0));
    }
    public int getNumCards(){
        return lin * col;
    }
    public void getRandMatrix(int lin,int col){
        int num = (lin * col) / 2;
        for(int i = 1; i < (num + 1);i++){
            matrix.add(i);
            matrix.add(i);
        }
        Collections.shuffle(matrix);
    }
    public String getNome(int pos){
        return jog.get(pos).getNome();
    }
}
