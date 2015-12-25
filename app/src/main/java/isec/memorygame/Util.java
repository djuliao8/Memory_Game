package isec.memorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

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
            String [] string = images.get(i).split(" ");
            imgs.add(string[0]);
        }
        return imgs;
    }

    public void addGallery(Context context,String nomeColecao){
        StringBuilder text = new StringBuilder();
        ArrayList<String> gallerys = getGallerys(context);
        for(int i = 0;i < gallerys.size();i++)
            text.append(gallerys.get(i)).append("\n");
        text.append(nomeColecao).append("\n");

        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("imgfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(text.toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage(Context context,String nomeColecao,String image){
        StringBuilder text = new StringBuilder();

        ArrayList<String> images = getAllImages(context);
        for (int i = 0; i < images.size(); i++) {
            String x = images.get(i);
            String[] string = images.get(i).split(" ");
            if (string[0].equals(nomeColecao)) {
                x += (" " + image);
            }
            text.append(x).append("\n");
        }
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("imgfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(text.toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeGallery(Context context,String nomeColecao){
        StringBuilder content = new StringBuilder();
        ArrayList<String> images = getAllImages(context);
        for(int i = 0; i < images.size();i++){
            String [] string = images.get(i).split(" ");
            if(!string[0].equals(nomeColecao))
                content.append(images.get(i)).append("\n");
        }
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("imgfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(content.toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeImage(Context context,String nomeColecao,String imagePath){
        StringBuilder text = new StringBuilder();

        ArrayList<String> images = getAllImages(context);

        for (int i = 0; i < images.size(); i++) {
            String x = images.get(i);
            String[] string = images.get(i).split(" ");
            if (string[0].equals(nomeColecao)) {
                x = string[0];
                for(int j = 1; j < string.length;j++){
                    if(!string[j].equals(imagePath))
                        x += (" " + string[j]);
                }
            }
            text.append(x).append("\n");
        }

        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("imgfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(text.toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    String []  images = receiveString.split(" ");
                    if(images[0].equals(nomeColecao)){
                        for(int i = 1; i < images.length;i++)
                            imgs.add(images[i]);
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


    public Bitmap decodeUri(Uri selectedImage,Context context) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);


        // The new size we want to scale to
        final int REQUIRED_SIZE = 128;

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
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

    }


}
