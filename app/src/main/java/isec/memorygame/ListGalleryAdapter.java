package isec.memorygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luis on 25/12/2015.
 */
public class ListGalleryAdapter extends BaseAdapter {
    ArrayList<String> gallery;
    private static LayoutInflater inflater;
    Context context;

    ListGalleryAdapter(Context context,ArrayList<String> gallery){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gallery = gallery;
    }

    @Override
    public int getCount() {
        return gallery.size();
    }

    @Override
    public Object getItem(int position) {
        return gallery.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String string[] = gallery.get(position).split(" ");

        View rowView = inflater.inflate(R.layout.gallery_layout, null);
        TextView nome = (TextView)rowView.findViewById(R.id.tituGallery);
        nome.setText(string[0]);

        TextView quant = (TextView)rowView.findViewById(R.id.sizeGallery);
        if((string.length - 1) == 16)
            quant.setText(context.getResources().getString(R.string.GA_LGalleryCompleta));
        else
            quant.setText((string.length - 1) + "");

        return rowView;
    }
}
