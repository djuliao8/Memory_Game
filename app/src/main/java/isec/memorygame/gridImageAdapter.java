package isec.memorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Uri uri;
        ImageView imageView = new ImageView(context);
        imageView.setPadding(2, 0, 2, 0);

        //Alterar tamanho das linhas com base no ecra
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, metrics.widthPixels / 5));

        uri = Uri.parse(images.get(position));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 128, 128, false));

        return imageView;
    }
}
