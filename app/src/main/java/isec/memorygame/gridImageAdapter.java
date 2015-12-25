package isec.memorygame;

import android.content.Context;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Luis on 25/12/2015.
 */
public class gridImageAdapter extends BaseAdapter {

    ArrayList<String> images;
    Context context;
    Util util = new Util();

    gridImageAdapter(Context context,ArrayList<String> images){
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
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
        imageView.setPadding(2, 0, 2, 0);

        //Alterar tamanho das linhas com base no ecra
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, metrics.widthPixels / 5));
        Uri uri = Uri.parse(images.get(position));

        Bitmap bitmap = null;
        try {
            bitmap = util.decodeUri(uri, context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);

        return imageView;
    }
}
