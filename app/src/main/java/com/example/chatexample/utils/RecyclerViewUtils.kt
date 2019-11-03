package com.example.chatexample.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import com.jakewharton.rxbinding3.view.layoutChangeEvents
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import timber.log.Timber

/**
 * @author Alan Perry
 * @since 2019-06-04 Created
 */
object RecyclerViewUtils {

    private var isLastItem = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    fun initMessageList(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: FlexibleAdapter<IFlexible<*>>,
        activity: RxAppCompatActivity
    ) {
        recyclerView
            .layoutChangeEvents()
            .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
            .filter {
                KeyboardUtils.isSoftInputVisible(activity) && isLastItem
            }
            .subscribe {
                Timber.d("layoutChangeEvents")
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }.isDisposed
        recyclerView.setOnTouchListener { _, _ ->
            //FIXME: 只有向下滑动时才会关闭键盘
            Timber.d("onTouchListener")
            KeyboardUtils.hideKeyboard(activity)
            false
        }
        recyclerView
            .scrollEvents()
            .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe {
                if (it.dy > 0) {
                    //scrolling down
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLastItem) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLastItem = true
                            Timber.d("scrollEvents: isLastItem = true")
                        }
                    }
                } else {
                    Timber.d("scrollEvents: isLastItem = false")
                    isLastItem = false
                }
            }.isDisposed
    }
}