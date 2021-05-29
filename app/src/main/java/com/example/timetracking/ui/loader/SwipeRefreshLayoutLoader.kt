package com.example.timetracking.ui.loader

import android.content.Context
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * @sample CustomersFragment.loader
 */
class SwipeRefreshLayoutLoader(
    context: Context,
    private val swipeRefreshLayout: SwipeRefreshLayout,
    rootView: View,
    anchorView: View? = null
) : ILoader(context, rootView, anchorView) {

    override fun initLoader() {
        swipeRefreshLayout.setOnRefreshListener {
            successEventFunction?.invoke()
        }

        swipeRefreshLayout.isRefreshing = true
        successEventFunction?.invoke()
    }

    override fun isLoading(loading: Boolean, loadContent: Boolean) {
        swipeRefreshLayout.isRefreshing = loading
    }
}