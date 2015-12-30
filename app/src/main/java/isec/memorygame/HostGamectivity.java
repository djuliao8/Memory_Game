package isec.memorygame;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HostGamectivity extends AppCompatActivity {
    Util ut = new Util();
    ServerSocket svSocket;
    Socket socketGame = null;
    ObjectInputStream input;
    ObjectOutputStream output;
    Matrix matrix;
    TextView labelIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_gamectivity);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this,getResources().getString(R.string.Err_NoConnection), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        labelIp = (TextView)findViewById(R.id.HG_TIP);
        String ip = ut.getLocalIpAddress();
        labelIp.setText(ip);






    }

    @Override
    protected void onResume() {
        super.onResume();
        //server();
    }

    public void server() {
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
                } catch (SocketTimeoutException e){
                    socketGame = null;
                    Toast.makeText(getApplicationContext(),R.string.Err_NoOne, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    socketGame = null;
                }
            }
        });
        if(socketGame == null)
            finish();
    }


    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new ObjectInputStream(socketGame.getInputStream());
                output = new ObjectOutputStream(socketGame.getOutputStream());
                matrix = new Matrix(2,2);
                output.writeObject(matrix);

                while (!Thread.currentThread().isInterrupted()) {

                }
            } catch (Exception e) {
                finish();
                Toast.makeText(getApplicationContext(),"The game was finished",Toast.LENGTH_LONG).show();
            }
        }
    });

}
