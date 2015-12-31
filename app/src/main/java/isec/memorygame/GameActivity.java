package isec.memorygame;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GameActivity extends AppCompatActivity {

    ServerSocket svSocket;
    Socket socketGame;
    Matrix matrix;
    Util ut = new Util();
    ObjectOutputStream output;
    ObjectInputStream input;
    ProgressDialog waitDialog;
    CountDownTimer cdt;
    Handler hdl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hdl = new Handler();
        matrix = (Matrix)getIntent().getSerializableExtra("Game");
        server();
    }

    public void server() {


        launchDialog();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    svSocket = new ServerSocket(ut.PORT);
                    svSocket.setSoTimeout(ut.WAIT);
                    socketGame = svSocket.accept();
                    svSocket.close();
                    svSocket = null;
                    commThread.start();
                } catch (SocketTimeoutException e) {
                    socketGame = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                hdl.post(new Runnable() {
                    @Override
                    public void run() {
                        waitDialog.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.Err_NoOne, Toast.LENGTH_SHORT).show();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();

    }


    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new ObjectInputStream(socketGame.getInputStream());
                output = new ObjectOutputStream(socketGame.getOutputStream());
                output.writeObject(matrix);

                while (!Thread.currentThread().isInterrupted()) {

                }
            } catch (Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"The game was finished",Toast.LENGTH_LONG).show();
            }
        }
    });


    public void launchDialog(){
        waitDialog = new ProgressDialog(GameActivity.this);
        waitDialog.setTitle(getResources().getString(R.string.GA_waitPl));
        waitDialog.setMessage(getResources().getString(R.string.GA_Aguarde));
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (svSocket != null) {
                    try {
                        svSocket.close();
                    } catch (IOException e) {
                    }
                    svSocket = null;
                }
            }
        });
        waitDialog.show();

    }

}
