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

    gridImageAdapter(AddImageGallery ag, ArrayList<String> images) {
        this.images = images;
        this.context = context;
        this.ag = ag;
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
        String image = getPath(uri);
        Bitmap bitmap = decodeBitmapFromUri(image, 128, 128);
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    public String getPath(Uri uri)
    {
        Cursor cursor = ag.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String image = cursor.getString(idx);
        cursor.close();
        return image;
    }

    public static Bitmap decodeBitmapFromUri(String image, int reqWidth, int reqHeight) {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image,options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
