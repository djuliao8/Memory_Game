package isec.memorygame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Luis on 17/12/2015.
 */
public class Util{
    Activity ac;
    Util(){

    }
    Util(Activity ac){
        this.ac = ac;
    }
    public static int Image = R.drawable.cartaporvirar;
    public static int Images[] = {
            R.drawable.camaleao,
            R.drawable.cavalo,
            R.drawable.elefante,
            R.drawable.formiga,
            R.drawable.gorila,
            R.drawable.animal67,
            R.drawable.flamingo3,
            R.drawable.camel2,
            R.drawable.monkey3,
            R.drawable.animal39,
            R.drawable.bear6,
            R.drawable.chick2,
            R.drawable.deer4,
            R.drawable.dolphin1,
            R.drawable.halloween237,
            R.drawable.kangaroo,
            R.drawable.rabbit5
    };

    public void escreverFicheiro(Context context,String data) {
            ArrayList<String> strings = lerFicheiro(context);
            if(strings.size() > 50)
                strings.remove(0);
            StringBuilder content = new StringBuilder();

            for (int i = 0; i < strings.size(); i++)
                    content.append(strings.get(i)).append("\n");

            content.append(data).append("\n");

            try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("histfile.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(content.toString());
                    outputStreamWriter.close();
            } catch (IOException e) {
                    e.printStackTrace();
            }
    }

        public ArrayList<String> lerFicheiro(Context context){
            ArrayList<String> hist = new ArrayList<>();
            try {
                    InputStream inputStream = context.openFileInput("histfile.txt");

                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        String receiveString;
                        while ((receiveString = bufferedReader.readLine()) != null) {
                            hist.add(receiveString);
                        }
                        inputStream.close();
                    }


            } catch (FileNotFoundException e) {
                    System.err.println("O ficheiro não foi encontrado");
            } catch (IOException e) {
                    System.err.println("Erro no tratamento do ficheiro");
            }
            return hist;
    }

    public ArrayList<String> getGallerys(Context context){
        StringBuilder text = new StringBuilder();
        ArrayList<String> images = getAllImages(context);
        ArrayList<String> imgs = new ArrayList<>();
        for(int i = 0; i < images.size();i++){
            String [] string = images.get(i).split("-");
            imgs.add(string[0]);
        }
        return imgs;
    }
    public ArrayList<String> getGallerysCompleted(Context context){
        StringBuilder text = new StringBuilder();
        ArrayList<String> images = getAllImages(context);
        ArrayList<String> imgs = new ArrayList<>();
        for(int i = 0; i < images.size();i++){
            String [] string = images.get(i).split("-");
            String [] par = string[2].split(" ");
            String [] im = string[3].split(" ");
            if(!string[1].equals(" ") && par.length >= 2 && (par.length + im.length) ==  15)
                imgs.add(string[0]);
        }
        return imgs;
    }

    public void addGallery(Context context,String nomeColecao){
        StringBuilder text = new StringBuilder();
        ArrayList<String> gallerys = getAllImages(context);
        for(int i = 0;i < gallerys.size();i++)
            text.append(gallerys.get(i)).append("\n");
        text.append(nomeColecao + "-" + " " + "-" + " " + "-" + " ").append("\n");

        escreveImagemFicheiro(context,text.toString());
    }

    public void addImage(Context context,String nomeColecao,String image){
        StringBuilder text = new StringBuilder();

        ArrayList<String> images = getAllImages(context);
        for (int i = 0; i < images.size(); i++) {
            String x = images.get(i);
            String[] string = images.get(i).split("-");
            if (string[0].equals(nomeColecao)) {
                if(string[3].equals(" "))
                    x = string[0] + "-" + string[1] + "-" + string[2] + "-" + (image + " ");
                else
                    x += (image + " ");
            }
            text.append(x).append("\n");
        }
        escreveImagemFicheiro(context,text.toString());
    }


    public void removeGallery(Context context,String nomeColecao){
        StringBuilder content = new StringBuilder();
        ArrayList<String> images = getAllImages(context);
        for(int i = 0; i < images.size();i++){
            String [] string = images.get(i).split("-");
            if(!string[0].equals(nomeColecao))
                content.append(images.get(i)).append("\n");
        }
        escreveImagemFicheiro(context,content.toString());
    }

    public ArrayList<String> getTurnCard(Context context,String nomeColecao){
        ArrayList<String> card = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("imgfile.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                while ((receiveString = bufferedReader.readLine()) != null) {
                    String []  images = receiveString.split("-");

                    if(images[0].equals(nomeColecao)){
                        String x [] = images[1].split(" ");
                        if(x.length != 0) {
                            if (!x[0].equals(" "))
                                card.add(x[0]);
                        }
                    }
                }
                inputStream.close();
            }


        } catch (FileNotFoundException e) {
            System.err.println("O ficheiro não foi encontrado");
        } catch (IOException e) {
            System.err.println("Erro no tratamento do ficheiro");
        }
        return card;
    }

    public void addTurnCard(Context context,String nomeColecao,String imagePath){
        StringBuilder content = new StringBuilder();
        ArrayList<String> oldCard = getTurnCard(context,nomeColecao);
        if(oldCard.size() != 0){
            removeImage(context,nomeColecao,oldCard.get(0));
            addImage(context,nomeColecao,oldCard.get(0));
            removeImage(context,nomeColecao,imagePath);
        }
        else
            removeImage(context,nomeColecao,imagePath);
        ArrayList<String> images = getAllImages(context);
        for(int i = 0; i < images.size();i++){
            String text = images.get(i);
            String [] string = images.get(i).split("-");
            if(string[0].equals(nomeColecao)){
                string [1] = imagePath;
                text = string[0] + "-" + string[1] + "-" + string[2] + "-" + string[3];
            }
            content.append(text).append("\n");
        }

        escreveImagemFicheiro(context, content.toString());
    }
    public void removeTurnCard(Context context,String nomeColecao,String imagePath){
        ArrayList<String> turnCard = getTurnCard(context,nomeColecao);
        if(!turnCard.get(0).equals(imagePath))
            return;
        removeImage(context,nomeColecao,imagePath);
        addImage(context, nomeColecao, imagePath);


    }

    public void removeImage(Context context,String nomeColecao,String imagePath){
        StringBuilder text = new StringBuilder();

        ArrayList<String> images = getAllImages(context);

        for (int i = 0; i < images.size(); i++) {
            String x = images.get(i);
            String[] string = images.get(i).split("-");
            if (string[0].equals(nomeColecao)) {
                x = string[0] + "-";
                for(int j = 1; j < 4;j++){
                    String imag[] = string[j].split(" ");
                    for(int y = 0; y < imag.length; y++){
                        if(!imag[y].equals(imagePath))
                            x += (imag[y] + " ");
                        else{
                            if(imag.length == 1)
                                x += " ";
                        }

                    }
                    if(imag.length == 0)
                        x += " ";
                    if(j != 3)
                        x += "-";
                }
            }
            text.append(x).append("\n");
        }

        escreveImagemFicheiro(context, text.toString());
    }

    public ArrayList<String> getAllImages(Context context) {
        ArrayList<String> imgs = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("imgfile.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                while ((receiveString = bufferedReader.readLine()) != null) {
                    imgs.add(receiveString);
                }
                inputStream.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgs;
    }

    public ArrayList<String>getImages(Context context,String nomeColecao){
        ArrayList<String> imgs = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("imgfile.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                while ((receiveString = bufferedReader.readLine()) != null) {
                    String []  images = receiveString.split("-");
                    if(images[0].equals(nomeColecao)){
                        String oimages []= images[3].split(" ");
                        for(int i = 0; i < oimages.length;i++){
                            if(!oimages[i].equals(" "))
                                imgs.add(oimages[i]);
                        }


                    }

                }
                inputStream.close();
            }


        } catch (FileNotFoundException e) {
            System.err.println("O ficheiro não foi encontrado");
        } catch (IOException e) {
            System.err.println("Erro no tratamento do ficheiro");
        }
        return imgs;
    }

    public ArrayList<String> getParIntruso(Context context, String nomeColecao){
        ArrayList<String> images = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("imgfile.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                while ((receiveString = bufferedReader.readLine()) != null) {
                    String []  par = receiveString.split("-");
                    if(par[0].equals(nomeColecao)){
                        String [] pares = par[2].split(" ");
                        for(int i = 0; i < pares.length;i++) {
                            if (!pares[i].equals(" "))
                                images.add(pares[i]);
                        }
                    }
                }
                inputStream.close();
            }

        } catch (FileNotFoundException e) {
            System.err.println("O ficheiro não foi encontrado");
        } catch (IOException e) {
            System.err.println("Erro no tratamento do ficheiro");
        }
        return images;
    }

    public void addParIntruso(Context context,String nomeColecao,String imagePath){
        StringBuilder content = new StringBuilder();
        StringBuilder write = new StringBuilder();
        ArrayList<String> parIntruso = getParIntruso(context,nomeColecao);

        removeImage(context,nomeColecao,imagePath);

        ArrayList<String> images = getAllImages(context);
        for(int i = 0; i < images.size();i++){
            String text = images.get(i);
            String [] string = images.get(i).split("-");
            if(string[0].equals(nomeColecao)){
                for(int j = 0; j < parIntruso.size();j++)
                    content.append(parIntruso.get(j) + " ");
                if(parIntruso.size() == 0)
                    string [2] = imagePath;
                else
                    string [2] = content.toString() + imagePath;
                text = string[0] + "-" + string[1] + "-" + string[2] + "-" + string[3];
            }
            write.append(text).append("\n");
        }

        escreveImagemFicheiro(context, write.toString());
    }

    public void removeParIntruso(Context context,String nomeColecao,String imagePath){
        ArrayList<String> pares = getParIntruso(context,nomeColecao);
        if(pares.contains(imagePath)) {
            removeImage(context, nomeColecao, imagePath);
            addImage(context, nomeColecao, imagePath);
        }
    }

    public void escreveImagemFicheiro(Context context,String content){
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("imgfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getPath(Context context,Uri uri)
    {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String image = cursor.getString(idx);
        cursor.close();
        return image;
    }

    /*public static Bitmap decodeBitmapFromUri(String image, int reqWidth, int reqHeight) {


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
*/

    public static Bitmap getBitmap(Context context,Uri image) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 128,128);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /*public Bitmap getBitmap(Uri selectedImage,Context context)	{
        //	Get the dimensions of the View
        int targetW	=	128;
        int targetH	=	128;

        //	Get the dimensions of the	bitmap
        BitmapFactory.Options bmOptions	=	new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds	=	true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, bmOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int photoW	=	bmOptions.outWidth;
        int photoH	=	bmOptions.outHeight;

        //	Determine	how much	to	scale down the image
        int scaleFactor	=	Math.min(photoW/targetW,	photoH/targetH);

        //	Decode the image	file	into	a	Bitmap	sized	to	fill the View
        bmOptions.inJustDecodeBounds	=	false;
        bmOptions.inSampleSize	=	scaleFactor;

        Bitmap	bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, bmOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    */
    public Bitmap decodeUri(Uri selectedImage,Context context){

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // The new size we want to scale to
        final int REQUIRED_SIZE = 12;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(input,null, o2);

    }


}
