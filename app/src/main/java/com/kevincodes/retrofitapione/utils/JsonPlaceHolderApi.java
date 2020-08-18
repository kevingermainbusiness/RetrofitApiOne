package com.kevincodes.retrofitapione.utils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import com.kevincodes.retrofitapione.models.Comment;
import com.kevincodes.retrofitapione.models.Post;

public interface JsonPlaceHolderApi {
    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId1,
            @Query("_sort") String sort, @Query("_order") String order);

    @GET("posts")
    Call<List<Post>> getPosts(
            @QueryMap Map<String,String> parameters
            );

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);
}
