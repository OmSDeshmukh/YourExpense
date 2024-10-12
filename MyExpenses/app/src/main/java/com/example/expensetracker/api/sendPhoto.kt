package com.example.expensetracker.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

//interface sendPhoto
//{
//    @Multipart
//    @Headers("Content-Type: multipart/form-data")
//    @POST("/predict")
//    suspend fun sendPhoto(
//        @Body photo: MultipartBody.Part
//    ) : Response<Void>
//}

interface sendPhoto
{
    @Headers("Content-Type: application/json")
    @POST("/predict")
    suspend fun sendPhoto(@Body photo: PhotoRequest): Response<ImageData>
}

data class PhotoRequest(val image_base64: String)