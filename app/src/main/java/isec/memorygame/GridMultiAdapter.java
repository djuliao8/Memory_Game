package isec.memorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
 * Created by Luis on 02/01/2016.
 */
public class GridMultiAdapter extends BaseAdapter {

    ArrayList<Carta> cards;
    int col;
    Context context;
    SharedPreferences pref;
    Util ut;

    public GridMultiAdapter(ArrayList<Carta> cards, int col,Context context) {
        this.cards = cards;
        this.col = col;
        this.context = context;
        ut = new Util();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img = new ImageView(context);
        img.setPadding(2, 3, 2, 3);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        img.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,metrics.widthPixels/col));

        String gallery = pref.getString("Gallery","Default");

        if (cards.get(position).descoberta) {
            if(gallery.equals("Default"))
                img.setImageResource(cards.get(position).cartaVirada);
            else{
                Uri uri = Uri.parse(cards.get(position).cartaViradaS);
                img.setImageBitmap(ut.getBitmap(context, uri));
            }

        }else {
            if(gallery.equals("Default"))
                img.setImageResource(cards.get(position).cartaPorVirar);
            else{
                Uri uri = Uri.parse(cards.get(position).cartaPorVirarS);
                img.setImageBitmap(ut.getBitmap(context, uri));
            }
        }
        return img;

    }
}
