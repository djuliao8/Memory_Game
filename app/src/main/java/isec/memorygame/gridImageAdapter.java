package isec.memorygame;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Luis on 25/12/2015.
 */
public class gridImageAdapter extends BaseAdapter {

    ArrayList<String> images;
    Context context;
    static AddImageGallery ag;
    Util util = new Util();
    ImageLoader imgLoader;

    gridImageAdapter(AddImageGallery ag, ArrayList<String> images) {
        this.images = images;
        this.ag = ag;
        imgLoader = ImageLoader.getInstance();
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
        ImageView imageView = new ImageView(ag);
        imageView.setPadding(2, 0, 2, 0);

        //Alterar tamanho das linhas com base no ecra
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ag.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, metrics.widthPixels / 5));

        uri = Uri.parse(images.get(position));
        imageView.setImageBitmap(util.getBitmap(ag,uri));
        return imageView;
    }


}
