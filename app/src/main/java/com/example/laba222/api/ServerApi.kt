package com.example.laba222.api

import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

        @GET("?code=faculty")
        suspend fun getFaculty(): Call<List<Faculty>>
        @GET("?code-groups")
        suspend fun getGroups(@Query("faculty_id")id:Int):Call<List<Group>>
}