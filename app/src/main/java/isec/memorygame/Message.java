package isec.memorygame;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luis on 30/12/2015.
 */
public class Message implements Serializable {
    ArrayList<Integer> pos = new ArrayList<>();
    int point;

    public void addPoint(int point){
        this.point = point;
    }
    public int getPoint(){
        return point;
    }
    public void addNum(int posi){
        pos.add(posi);
    }
    public ArrayList<Integer> getNum(){
        return pos;
    }
}

