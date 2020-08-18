package com.kevincodes.retrofitapione;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kevincodes.retrofitapione.models.Comment;
import com.kevincodes.retrofitapione.models.Post;
import com.kevincodes.retrofitapione.utils.JsonPlaceHolderApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ProgressBar contentLoadingProgressBar;
    private JsonPlaceHolderApi placeHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        textView = findViewById(R.id.result);
        contentLoadingProgressBar = findViewById(R.id.contentLoadingProgressBar);
        initializeLogic();
    }

    private void initializeLogic() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        placeHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        // to get our network request
        getComments();
    }

    private void getPosts(){
        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");
        Call<List<Post>> listCall = placeHolderApi.getPosts(parameters);

        listCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textView.setVisibility(View.VISIBLE);
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    textView.setText("Code: " + response.code());
                    return;
                }
                List<Post> postsList = response.body();
                if (postsList != null) {
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    for (Post post : postsList) {
                        String content = "";
                        content += "ID: " + post.getId() + "\n";
                        content += "User ID: " + post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";
                        textView.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
    private void getComments(){
        Call<List<Comment>> listCall = placeHolderApi.getComments("posts/3/comments");
        listCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Code: "+response.code());
                    return;
                }
                List<Comment> comments = response.body();
                if(comments!=null){
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    for (Comment comment : comments){
                        String content = "";
                        content += "ID: " + comment.getId() + "\n";
                        content += "Post ID: " + comment.getPostId() + "\n";
                        content += "Name: " + comment.getName() + "\n";
                        content += "Email: " + comment.getEmail() + "\n";
                        content += "Text: " + comment.getText() + "\n\n";
                        textView.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable throwable) {
                contentLoadingProgressBar.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(throwable.getMessage());
            }
        });
    }
}