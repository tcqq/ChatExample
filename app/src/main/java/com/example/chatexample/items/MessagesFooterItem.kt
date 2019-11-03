package com.example.chatexample.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.chatexample.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author Alan Perry
 * @since 2019-05-30 Created
 */
data class MessagesFooterItem(
    val id: String
) : AbstractFlexibleItem<MessagesFooterItem.ViewHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.item_messages_footer
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter)
}