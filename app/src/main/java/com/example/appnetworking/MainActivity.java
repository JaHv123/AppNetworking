package com.example.appnetworking;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGettask().execute();
            }
        });
    }

    class HttpGettask extends AsyncTask<Void, Void, String>{

        private static final String HOST = "api.geonames.org";
        private static final String USER_NAME = "jahv123";
        private static final String HTTP_GET_REQUEST = "GET /earthquakesJSON? +" +
                "north=44.1 & south=-9.9 & east =-22.4 & west=55.25 &username= " +
                USER_NAME + "http/1.11\n"+
                "Host:" + HOST + "\n"+
                "Connection: close\n\n";

        @Override
        protected String doInBackground(Void... voids) {

            Socket socket = null;
            String data = "";

            try {
                socket = new Socket(HOST, 80);

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()) );
                pw.println(HTTP_GET_REQUEST);

                data = leerCadena(socket.getInputStream());
            }catch (IOException e){
                e.printStackTrace();
            }

            return data;
        }

        private String leerCadena(InputStream in){

            BufferedReader br = null;
            StringBuffer datos = new StringBuffer();
            String linea ="";

            br = new BufferedReader(new InputStreamReader(in));
            Log.d("mensajes","Antes de leer datos");
            try {
                while((linea = br.readLine()) != null){
                    datos.append(linea);
                    Log.d("mensajes2","Leyendo datos");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            Log.d("mensajes","FIn del Do");
            return datos.toString();


        }

        protected void onPostExecute(String resultado){

            //Asingnamos el texto a nuestro TextView
            textView.setText(resultado);
        }
    }

}


