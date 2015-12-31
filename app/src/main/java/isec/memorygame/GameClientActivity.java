package isec.memorygame;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClientActivity extends AppCompatActivity {

    String ip;
    String nome;
    Socket socketGame;
    Handler hdl;

    ObjectOutputStream output;
    ObjectInputStream input;

    Matrix matrix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_client);

        ip = getIntent().getStringExtra("Nome");
        nome = getIntent().getStringExtra("Ip");



    }

    void client() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketGame = new Socket(ip,9988);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                    hdl.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.Err_UnableConnect, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    return;
                }
                commThread.start();
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
                matrix = (Matrix)input.readObject();

                while (!Thread.currentThread().isInterrupted()) {

                }
            } catch (Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"The game was finished",Toast.LENGTH_LONG).show();
            }
        }
    });

}
