package com.example.example2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button allPostBtn, onePostBtn;
    private EditText idEt;
    private TextView tvBody;
    private final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allPostBtn = (Button) findViewById(R.id.btn_all_posts);
        onePostBtn = (Button) findViewById(R.id.btn_one_post);

        idEt = (EditText) findViewById(R.id.et_id);
        tvBody = (TextView) findViewById(R.id.tv_body);

        allPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllPosts();
            }
        });

        onePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getPostId();
                Log.d("SSIRI", ""+id);
                getOnePost(id);

            }
        });
    }

    private void getAllPosts() {
        tvBody.setText("");
        tvBody.setTextColor(Color.BLACK);

        Retrofit retrofit = new Retrofit.Builder()
                . baseUrl(BASE_URL)
                . addConverterFactory(GsonConverterFactory.create())
                . build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    tvBody.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID : " + post.getId();
                    content += "\nUser Id: " + post.getUserId();
                    content += "\nTitle: " + post.getTitle();
                    content += "\nText: " + post.getText() + "\n\n";
                    tvBody.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvBody.setText(t.getLocalizedMessage());
            }
        });

    }

    private void getOnePost(int id) {
        tvBody.setText("");

        if(id == -1) {
            Log.d("SSiri", "Inside");
            tvBody.setText("Enter an id # below");
            tvBody.setTextColor(Color.RED);
        } else {
            tvBody.setTextColor(Color.BLACK);

            Retrofit retrofit = new Retrofit.Builder()
                    . baseUrl(BASE_URL)
                    . addConverterFactory(GsonConverterFactory.create())
                    . build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<Post> call = jsonPlaceHolderApi.getSinglePost(id);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (!response.isSuccessful()) {
                        tvBody.setText("Code: " + response.code());
                        return;
                    }

                    Post post = response.body();
                    Log.d("SSiri", post.getText());

                    String content = "";
                    content += "ID : " + post.getId();
                    content += "\nUser Id: " + post.getUserId();
                    content += "\nTitle: " + post.getTitle();
                    content += "\nText: " + post.getText() + "\n\n";

                    tvBody.append(content);

                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    tvBody.setText(t.getLocalizedMessage());
                }
            });


        }
    }

    private int getPostId() {
        if(idEt.getText().length() > 0) {
            Log.d("SSIRI", idEt.getText().toString());
           return  Integer.valueOf(idEt.getText().toString());
        }

        return -1;
    }
}
