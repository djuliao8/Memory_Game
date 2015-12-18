package isec.memorygame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Luis on 17/12/2015.
 */
public class GridJogoAdapter extends BaseAdapter{

    ArrayList<Carta> cartas;
    Context context;

    GridJogoAdapter(SinglePlayerActivity single,ArrayList<Carta> cartas){
        context = single;
        this.cartas = cartas;
    }

    @Override
    public int getCount() {
        return cartas.size();
    }

    @Override
    public Object getItem(int position) {
        return cartas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,100));
        imageView.setPadding(0,7,0,7);

        if(cartas.get(position).isDescoberta())
            imageView.setImageResource(cartas.get(position).getCartaVirada());
        else
            imageView.setImageResource(cartas.get(position).getCartaPorVirar());
        return imageView;
    }
}
