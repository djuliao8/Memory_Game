package isec.memorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Luis on 17/12/2015.
 */
public class GridJogoAdapter extends BaseAdapter{

    ArrayList<Carta> cartas;
    Context context;
    SharedPreferences pref;
    int width;
    Util util = new Util();
    Uri uri;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
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


        String gallery = pref.getString("Gallery","Default");

        if (cartas.get(position).descoberta) {
            if(gallery.equals("Default"))
                imageView.setImageResource(cartas.get(position).cartaVirada);
            else{
                uri = Uri.parse(cartas.get(position).cartaViradaS);
                imageView.setImageBitmap(util.getBitmap(context, uri));
            }

        }else {
            if(gallery.equals("Default"))
                imageView.setImageResource(cartas.get(position).cartaPorVirar);
            else{
                uri = Uri.parse(cartas.get(position).cartaPorVirarS);
                imageView.setImageBitmap(util.getBitmap(context, uri));
            }
        }
        return imageView;
    }


}
