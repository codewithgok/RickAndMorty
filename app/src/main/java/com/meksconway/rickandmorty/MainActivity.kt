package com.meksconway.rickandmorty

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.meksconway.rickandmorty.Status.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val charactersQuery = CharactersQuery(page = Input.optional(1))
        NetworkManager.request(charactersQuery) { result ->
            when(result.status) {
                SUCCESS -> {
                    //hide loading
                    //showResults(result.data?.characters?.results)
                }
                FAIL -> {
                    //hide loading
                    //show fail message
                }
                LOADING -> {
                    //show loading
                }
            }
        }

    }

    private fun simpleRequest() {
        val apolloClient = ApolloClient.builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .build()

        val charactersQuery = CharactersQuery(page = Input.optional(1))

        apolloClient.query(charactersQuery)
            .enqueue(object : ApolloCall.Callback<CharactersQuery.Data>() {
                override fun onResponse(response: Response<CharactersQuery.Data>) {
                    val characters = response.data?.characters
                    if (response.hasErrors() || characters == null) {
                        //error impl
                        Log.d(TAG, response.errors?.first()?.message.toString())
                    } else {
                        //continue with data
                        Log.d(TAG, characters.results.toString())
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d(TAG, e.message.toString())
                }

            })
    }


}