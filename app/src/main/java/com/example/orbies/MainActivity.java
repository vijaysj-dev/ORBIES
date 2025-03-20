package com.example.orbies;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button send = findViewById(R.id.SEND);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_send();
                receive_chat();
            }
        });
    }
    private void  receive_chat()
    {
        TextView receiver = findViewById(R.id.receiver);
        Socket socket;
        try{
            socket = IO.socket("https://node-s.vercel.app/");
            socket.connect();
            socket.on("serverMessage", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String recText = args[0].toString();
                            receiver.setText(recText);
                           // Toast.makeText(MainActivity.this, "THIS", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private void new_send()
    {
        EditText send = findViewById(R.id.sender);
        String message = send.getText().toString();
        Socket socket;
        try {
            socket = IO.socket("https://node-s.vercel.app/"); // Replace with your server's URL
            socket.connect();

            // Send message to the server
            socket.emit("sendMessage", message);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private void send_chat()
    {
        EditText send = findViewById(R.id.sender);
        String data_send_nonwidged = send.getText().toString();
        RequestBody plt_rb = RequestBody.create(data_send_nonwidged, MediaType.get("text/plain; charset=utf-8"));
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url("https://node-s.vercel.app/snw").post(plt_rb).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show()
                );

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}