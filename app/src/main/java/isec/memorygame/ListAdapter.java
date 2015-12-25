package isec.memorygame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Luis on 24/12/2015.
 */
public class ListAdapter extends BaseAdapter {
    ArrayList<String> hist;
    private static LayoutInflater inflater;
    Context context;
    ListAdapter(Activity x,ArrayList<String> hist){
        this.hist = hist;
        inflater = (LayoutInflater)x.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = x.getApplicationContext();
    }
    @Override
    public int getCount() {
        return hist.size();
    }

    @Override
    public Object getItem(int position) {
        return hist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String string[] = hist.get(position).split(" ");

        View rowView = inflater.inflate(R.layout.list_layout, null);
        TextView tipo = (TextView)rowView.findViewById(R.id.tipojogo);
        tipo.setText(string[0]);

        TextView nome = (TextView)rowView.findViewById(R.id.nome);
        nome.setText(string[1]);

        TextView njogadas = (TextView)rowView.findViewById(R.id.njogadas);
        njogadas.setText("J: " + string[3]);

        TextView pontos = (TextView)rowView.findViewById(R.id.pontos);
        pontos.setText("P: " + string[2]);

        TextView dificuldade = (TextView)rowView.findViewById(R.id.dificuldade);
        dificuldade.setText(" - " + string[5]);

        return rowView;
    }
}
