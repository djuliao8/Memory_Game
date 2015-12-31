package isec.memorygame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Luis on 30/12/2015.
 */
public class Matrix implements Serializable {
    int col,lin,par;
    int matrix[][];
    ArrayList<Jogador> jog = new ArrayList<>();

    Matrix(int lin,int col,int par){
        this.lin = lin;
        this.col = col;
        this.par = par;
        matrix = getRandMatrix(lin,col);
    }

    public void addJogador(String nome){
        jog.add(new Jogador(nome,0));
    }
    public int getNumCards(){
        return lin * col;
    }
    public int[][] getRandMatrix(int lin,int col){
        ArrayList<Integer> nums = new ArrayList<>();
        int k = 0;
        int Matrix [][] = new int[lin][col];
        int num = (lin * col) / 2;
        for(int i = 1; i < (num + 1);i++){
            nums.add(i);
            nums.add(i);
        }
        Collections.shuffle(nums);
        for(int i = 0; i < lin;i++){
            for(int j = 0; j < col;j++){
                Matrix[i][j] = nums.get(k);
                k++;
            }
        }
        return Matrix;
    }
}
