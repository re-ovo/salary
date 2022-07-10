package me.rerere.salary.api

import me.rerere.salary.model.Salary
import me.rerere.salary.model.Stats
import me.rerere.salary.model.User
import retrofit2.http.*

interface SalaryAPI {
    @POST("/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("eid") eid: Int,
        @Field("password") password: String
    ): Response<String>

    @GET("/self")
    suspend fun getSelf(): Response<User>

    @GET("/users")
    suspend fun getUsers(): Response<List<User>>

    @PUT("/user")
    @FormUrlEncoded
    suspend fun addUser(
        @Field("eid") eid: Int,
        @Field("name") name: String,
        @Field("rank") rank: String,
        @Field("department") department: String,
        @Field("role") role: Int,
        @Field("password") password: String
    ): Response<Nothing>

    @DELETE("/user/{eid}")
    suspend fun deleteUser(@Path("eid") eid: Int): Response<Nothing>

    @PATCH("/user/{eid}")
    @FormUrlEncoded
    suspend fun updateUser(
        @Path("eid") eid: Int,
        @Field("name") name: String,
        @Field("rank") rank: String,
        @Field("department") department: String,
        @Field("role") role: Int,
        @Field("password") password: String
    ): Response<Nothing>

    @GET("/salary/{eid}")
    suspend fun getSalary(@Path("eid") eid: Int): Response<List<Salary>>

    @POST("/salary/{eid}")
    suspend fun addSalary(
        @Body salary: Salary
    ): Response<Nothing>

    @GET("/stats")
    suspend fun getStats(): Response<Stats>
}