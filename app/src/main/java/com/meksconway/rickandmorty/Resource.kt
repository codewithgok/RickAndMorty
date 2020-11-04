package com.meksconway.rickandmorty

enum class Status {
    SUCCESS,
    FAIL,
    LOADING
}

data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val errorMessage: String? = null
) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> fail(message: String?): Resource<T> {
            return Resource(Status.FAIL, errorMessage = message)
        }

        fun <T> loading(): Resource<T> = Resource(Status.LOADING)

    }


}