package isec.memorygame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
    ArrayList<String> img,par,turnCard;
    PopupWindow pw;
    Bitmap bitmap = null;
    AlertDialog.Builder builder,addBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_gallery);
        nome_Colecao = getIntent().getStringExtra("nome");


        imageGallery = (GridView)findViewById(R.id.imageContent);
        imageGallery.setNumColumns(5);
        MsgBoxRemove();
        MsgAddBack();

        imageReset = (ImageView)findViewById(R.id.imgView);

        final Button addImage = (Button)findViewById(R.id.addImage);

        new Thread(new Runnable() {
            public void run() {
                //Se já houver alguma image adiciona à gridview
                turnCard = util.getTurnCard(getApplicationContext(), nome_Colecao);
                img = util.getImages(getApplicationContext(), nome_Colecao);
                par = util.getParIntruso(getApplicationContext(),nome_Colecao);

                img.addAll(turnCard);
                img.addAll(par);
                final gridImageAdapter grid = new gridImageAdapter(AddImageGallery.this, img);
                imageGallery.setAdapter(grid);
            }
        }).start();


        imageGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageReset.setImageResource(android.R.color.transparent);
                initiatePopupWindow(img.get(position));
            }
        });

        Button buttonLoadImage = (Button) findViewById(R.id.procuraImage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(img.size() > 15){
                    Toast.makeText(getApplicationContext(), R.string.Err_ExcessoImage, Toast.LENGTH_SHORT).show();
                    return;
                }
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
                if(img.size() > 15){
                    Toast.makeText(getApplicationContext(), R.string.Err_ExcessoImage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(img.contains(imagePath)) {
                    Toast.makeText(getApplicationContext(), R.string.Err_ImageAdd, Toast.LENGTH_SHORT).show();
                    return;
                }
                util.addImage(getApplicationContext(), nome_Colecao, imagePath);
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

    public void MsgBoxRemove() {
        builder = new AlertDialog.Builder(AddImageGallery.this);
        builder.setMessage(getResources().getString(R.string.MSG_Box));
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.MSG_Box_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        util.removeImage(getApplicationContext(), nome_Colecao, imagePath);
                        dialog.cancel();
                        pw.dismiss();
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
    }

    public void MsgAddBack() {
        addBack = new AlertDialog.Builder(AddImageGallery.this);
        addBack.setMessage(getResources().getString(R.string.MSG_Box));
        addBack.setCancelable(true);
        addBack.setPositiveButton(getResources().getString(R.string.MSG_Box_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        util.removeTurnCard(AddImageGallery.this, nome_Colecao, imagePath);
                        dialog.cancel();
                        pw.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
        addBack.setNegativeButton(getResources().getString(R.string.MSG_Box_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    private void initiatePopupWindow(final String imagepath) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) AddImageGallery.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.popup));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            imagePath = imagepath;
            try {
                bitmap = util.decodeUri(Uri.parse(imagepath), getApplicationContext());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final ImageView img = (ImageView) layout.findViewById(R.id.PO_Image);
            img.setImageBitmap(bitmap);

            Button remImage = (Button) layout.findViewById(R.id.PO_BRemove);
            remImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            final Button addTurnCard = (Button) layout.findViewById(R.id.PO_BDefineCarta);
            turnCard = util.getTurnCard(getApplicationContext(), nome_Colecao);
            if (turnCard.size() != 0) {
                if (turnCard.get(0).equals(imagepath))
                    addTurnCard.setText(getResources().getString(R.string.PO_BRemParteCarta));
                else
                    addTurnCard.setText(getResources().getString(R.string.PO_BCarta));
            } else
                addTurnCard.setText(getResources().getString(R.string.PO_BCarta));
            addTurnCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addTurnCard.getText().equals(getResources().getString(R.string.PO_BCarta))) {
                        util.addTurnCard(AddImageGallery.this, nome_Colecao, imagepath);
                        pw.dismiss();
                        finish();
                        startActivity(getIntent());
                    } else if (addTurnCard.getText().equals(getResources().getString(R.string.PO_BRemParteCarta))) {
                        AlertDialog alert = addBack.create();
                        alert.show();
                    }
                }
            });

            final Button addParIntruso = (Button) layout.findViewById(R.id.PO_BDefinePar);
            par = util.getParIntruso(getApplicationContext(), nome_Colecao);
            if (par.size() != 0) {
                if (par.contains(imagepath))
                    addParIntruso.setText(getResources().getString(R.string.PO_BRemParIntruso));
                else
                    addParIntruso.setText(getResources().getString(R.string.PO_BPar));
            } else
                addParIntruso.setText(getResources().getString(R.string.PO_BPar));

            addParIntruso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (addParIntruso.getText().equals(getResources().getString(R.string.PO_BRemParIntruso))) {
                        util.removeParIntruso(AddImageGallery.this, nome_Colecao, imagepath);
                        pw.dismiss();
                        finish();
                        startActivity(getIntent());
                    } else if (addParIntruso.getText().equals(getResources().getString(R.string.PO_BPar))) {
                        if (par.size() > 4) {
                            Toast.makeText(getApplicationContext(), R.string.Err_ExcessoPar, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        util.addParIntruso(AddImageGallery.this, nome_Colecao, imagepath);
                        pw.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                }
            });


            Button cancel = (Button) layout.findViewById(R.id.PO_BCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagePath = "";
                    pw.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
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
