package isec.memorygame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddImageGallery extends AppCompatActivity {


    private static int IMAGE = 1;
    Util util = new Util();
    String imagePath = "";
    String nome_Colecao;
    GridView imageGallery;
    ImageView imageReset;
    ArrayList<String> img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_gallery);
        nome_Colecao = getIntent().getStringExtra("nome");


        imageGallery = (GridView)findViewById(R.id.imageContent);
        imageGallery.setNumColumns(5);

        imageReset = (ImageView)findViewById(R.id.imgView);

        final Button addImage = (Button)findViewById(R.id.addImage);
        //Se já houver alguma image adiciona à gridview
        img = util.getImages(getApplicationContext(), nome_Colecao);
        final gridImageAdapter grid = new gridImageAdapter(getApplicationContext(),img);
        imageGallery.setAdapter(grid);


        imageGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap = null;
                try {
                    bitmap = util.decodeUri(Uri.parse(img.get(position)), getApplicationContext());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageReset.setImageBitmap(bitmap);
                imagePath = img.get(position);
                addImage.setText(getResources().getString(R.string.AI_removeImage));
            }
        });

        Button buttonLoadImage = (Button) findViewById(R.id.procuraImage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE);
                addImage.setText(getResources().getString(R.string.AI_addImage));
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.Err_FaltaImage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addImage.getText().equals(getResources().getString(R.string.AI_addImage)))
                    util.addImage(getApplicationContext(), nome_Colecao, imagePath);

                if(addImage.getText().equals(getResources().getString(R.string.AI_removeImage)))
                    util.removeImage(getApplicationContext(), nome_Colecao, imagePath);

                imagePath = "";
                finish();
                Intent i = getIntent().putExtra("nome",nome_Colecao);
                startActivity(i);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imagePath = uri.toString();

            Bitmap bitmap = null;
            try {
                bitmap = util.decodeUri(uri, getApplicationContext());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddImageGallery.this,GalleryActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
