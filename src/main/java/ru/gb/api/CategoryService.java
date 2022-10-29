package ru.gb.api;

import retrofit2.Call;
import retrofit2.http.*;
import ru.gb.dto.GetCategoryResponse;


public interface CategoryService {

    @GET("categories/{id}")
    Call<GetCategoryResponse> getCategory(@Path("id") int id);
}
