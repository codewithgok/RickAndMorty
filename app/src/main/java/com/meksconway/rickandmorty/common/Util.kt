package com.meksconway.rickandmorty.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <reified T : Any> T.ordinal(): Int {
    if (T::class.isSealed) {
        return T::class.java.classes.indexOfFirst { sub -> sub == javaClass }
    }

    val kClass = if (T::class.isCompanion) {
        javaClass.declaringClass
    } else {
        javaClass
    }

    return kClass.superclass?.classes?.indexOfFirst { it == kClass } ?: -1
}

fun View.addViewObserver(function: () -> Unit) {
    val view = this
    view.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            function.invoke()
        }
    })
}

fun <T> resultFlow(networkCall: suspend () -> Resource<T>): Flow<Resource<T>> {
    return flow {
        emit(Resource.loading())
        emit(networkCall.invoke())
    }.flowOn(Dispatchers.IO)
}

fun MultipleStackNavigator.safeBack() {
    if (this.canGoBack()) {
        this.goBack()
    }
}

fun getSeasonName(episodeText: String?): String {
    return episodeText?.let { e ->
        var s = e.substring(1, 3)
        if (s.first() == '0') {
            s = e.substring(2, 3)
        }
        "Season $s"
    } ?: "Season"

}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    view.requestFocus()
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

/**
 *
 * public static void showKeyboard(EditText mEtSearch, Context context) {
mEtSearch.requestFocus();
InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
}
 * */