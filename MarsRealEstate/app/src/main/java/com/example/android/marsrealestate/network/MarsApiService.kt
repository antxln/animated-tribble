/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

/**
 * Retrofit builder requires base url and Converter factory for
 * converting network response to another data form.
 * ScalarsConverter converts JSON to String
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

/**
 * Define annotated interface for interacting with web service.
 * Retrofit returns an implementation of this interface, used to interact
 * with the web service, handling details such as running requests in the
 * background (not blocking main).
 * Call object is used to start the request.
 */
interface MarsApiService {
    @GET("realestate")
    fun getProperties(): Call<String>
}

object MarsApi {
    /**
     * Retrofit create call is expensive and only requires one instance
     * so lazy initialization is used.
     * Initialization is run when property is first used, and future
     * references return the same value, without re-running lazy block.
     */
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}