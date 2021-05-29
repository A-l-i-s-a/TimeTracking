package com.example.timetracking.ui.loader

import android.content.Context
import android.view.View
import android.widget.ProgressBar


/**
 * @sample LoginActivity.loader
 */
class ProgressBarLoader(
    context: Context,
    private val progressBar: ProgressBar,
    rootView: View,
    anchorView: View?
) : ILoader(context, rootView, anchorView) {
    override fun isLoading(loading: Boolean, loadContent: Boolean) {
        if (loading) {
            progressBar.visibility = View.VISIBLE
            replaceableView?.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            replaceableView?.visibility = View.VISIBLE
        }

        if (loadContent) successEventFunction?.invoke()
    }

    private var replaceableView: View? = null
    fun setReplaceableView(replaceableView: View) {
        this.replaceableView = replaceableView
    }

    override fun initLoader() {
        isLoading(loading = true, loadContent = true)
    }
}