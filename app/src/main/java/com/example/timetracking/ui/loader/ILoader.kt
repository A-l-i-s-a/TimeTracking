package com.example.timetracking.ui.loader

import android.content.Context
import android.view.View
import com.example.timetracking.R
import com.google.android.material.snackbar.Snackbar
import java.net.ConnectException

/**
 * ILoader - базовый класс, отображающий загрузку данных.
 *
 * Вызвать [initLoader], когда по завершению ниже описанных настроек.
 *
 * [errorEvent] - метод, который обрабатывает ошибки
 * и выводит на экран сообшение через [Snackbar] (ОБЯЗАТЕЛЬНО ОБРАБАТЫВАТЬ ОШИБКИ)
 * [setSuccessEvent] - метод, который запускает логику, после успешной загрузки
 *
 * [isLoading]: loading - отображать ли загрузку; error - если вызвана ошибка.
 *
 * @param[rootView] к нему крепится [Snackbar]
 * @param[anchorView] является якорем для [Snackbar]
 */
abstract class ILoader(
    private val context: Context,
    private val rootView: View,
    private val anchorView: View?
) {

    /**
     * Срабатывает, когда возникает ошибка. Выводит сообщение на экран.
     */
    open fun errorEvent(throwable: Throwable) {
        isLoading(loading = false, loadContent = false)
        Snackbar
            .make(
                rootView,
                getMessage(throwable),
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(anchorView)
            .show()
    }

    private fun getMessage(throwable: Throwable): String {
        return when(throwable) {
            is ConnectException -> {
                context.getString(R.string.NO_CONNECTION_ERROR_MESSAGE)
            }
            else -> throwable.message.toString()
        }
    }

    protected var successEventFunction: (() -> Unit)? = null
    fun setSuccessEvent(function: (() -> Unit)?) {
        successEventFunction = function
    }

    open fun initLoader() {}
    abstract fun isLoading(loading: Boolean, loadContent: Boolean)
}