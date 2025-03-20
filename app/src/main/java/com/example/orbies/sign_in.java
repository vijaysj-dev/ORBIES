package com.example.orbies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.sql.Time;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sign_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    reqdata();
    }


    private void reqdata() {
        ProgressBar pb = findViewById(R.id.vanishpb);
        TextView tv = findViewById(R.id.assigned);

        pb.setVisibility(ProgressBar.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://node-s.vercel.app/").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(() -> {
                    tv.setText("SOMETHING WENT WRONG");
                    pb.setVisibility(ProgressBar.GONE);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        pb.setVisibility(ProgressBar.GONE);

                        try {
                            tv.setText(response.body().string());

                            Intent int_next = new Intent(sign_in.this, MainActivity.class);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(
                                            int_next
                                    );
                                }
                            },4000);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        tv.setText("Response failed");
                        pb.setVisibility(ProgressBar.GONE);
                    }
                });
            }
        });
    }



}