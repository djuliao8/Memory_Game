package isec.memorygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String gallerySelected;
    Util util = new Util();
    ArrayList<String> gallery;
    Button remGallery;
    Button addGallery;
    Button editGallery;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ListView lst = (ListView) findViewById(R.id.galleryList);
        remGallery = (Button)findViewById(R.id.removeGallery);
        addGallery = (Button)findViewById(R.id.addGallery);
        editGallery = (Button)findViewById(R.id.editGallery);
        gallerySelected = "";


        builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.MSG_Box));
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.MSG_Box_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        util.removeGallery(getApplicationContext(), gallerySelected);
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.MSG_Box_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        gallery = new Util().getAllImages(getApplicationContext());

        lst.setAdapter(new ListGalleryAdapter(this, gallery));

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] string = gallery.get(position).split(" ");
                gallerySelected = string[0];
            }
        });

        remGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gallerySelected.equals(""))
                    Toast.makeText(getApplicationContext(), R.string.Err_NoneSelected, Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        editGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gallerySelected.equals(""))
                    Toast.makeText(getApplicationContext(), R.string.Err_NoneSelected, Toast.LENGTH_SHORT).show();
                else{
                    Intent i = new Intent(GalleryActivity.this, AddImageGallery.class);
                    i.putExtra("nome",gallerySelected);
                    startActivity(i);
                }
            }
        });

        addGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GalleryActivity.this,AddGalleryActivity.class);
                startActivity(i);
            }
        });

    }


}
