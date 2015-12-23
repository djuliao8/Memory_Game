package isec.memorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    SharedPreferences pref;
    int width;
    int posi;

    GridJogoAdapter(SinglePlayerActivity single,ArrayList<Carta> cartas,int width){
        context = single;
        this.cartas = cartas;
        this.width = width;
    }

    GridJogoAdapter(MultiPlayerActivity multi, ArrayList<Carta> cartas) {
        context = multi;
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
        imageView.setPadding(2, 3, 2, 3);

        //Alterar tamanho das linhas com base no ecra
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        int pos = pref.getInt("Dificuldade",2);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        if(pos == 0)
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,metrics.widthPixels/2));
        else
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,metrics.widthPixels/(pos + 1)));


        if (cartas.get(position).descoberta)
            imageView.setImageResource(cartas.get(position).cartaVirada);
        else
            imageView.setImageResource(cartas.get(position).cartaPorVirar);
        return imageView;
    }


}
