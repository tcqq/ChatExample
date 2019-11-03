package com.example.chatexample.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.chatexample.R
import com.example.chatexample.items.MessagesFooterItem
import com.example.chatexample.items.MessagesHeaderItem
import com.example.chatexample.items.MyMessagesTextItemKt
import com.example.chatexample.utils.RecyclerViewUtils
import com.jakewharton.rxbinding3.widget.textChanges
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.activity_conversation.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Alan Perry
 * @since 2019-05-25 Created
 */
class ConversationActivityKt : RxAppCompatActivity(),
    FlexibleAdapter.OnItemClickListener {

    private lateinit var adapter: FlexibleAdapter<IFlexible<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Alan D."

        compose_message_text
            .textChanges()
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe {
                if (it.isBlank()) {
                    voice_note_button.setImageResource(R.drawable.ic_keyboard_voice_black_24dp)
                } else {
                    voice_note_button.setImageResource(R.drawable.ic_send_black_24dp)
                }
            }.isDisposed

        voice_note_button.setOnClickListener {
            if (compose_message_text.text.isNullOrBlank().not()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDate = sdf.format(Date())
                adapter.addItem(
                    -1,
                    MyMessagesTextItemKt(
                        "${adapter.itemCount + 1}",
                        "${compose_message_text.text}",
                        currentDate
                    )
                )
                adapter.smoothScrollToPosition(adapter.itemCount - 1)
                adapter.notifyDataSetChanged()
                compose_message_text.text!!.clear()
            }
        }

        adapter = FlexibleAdapter(null, this, true)
        val layoutManager = SmoothScrollLinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        message_list.layoutManager = layoutManager
        message_list.adapter = adapter
        message_list.setHasFixedSize(true)
        RecyclerViewUtils.initMessageList(message_list, layoutManager, adapter, this)
        adapter.updateDataSet(getItems())
        adapter.addScrollableFooter(MessagesFooterItem("FI"))
        adapter.addScrollableHeader(MessagesHeaderItem("HI"))
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != adapter.itemCount - 1 && position != 0) {
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
            when (val viewHolder = message_list.findViewHolderForAdapterPosition(position)) {
                is MyMessagesTextItemKt.ViewHolder -> {
                    viewHolder.messageStatus.isVisible = !viewHolder.messageStatus.isVisible
                }
            }
        }
        return false
    }

    private fun getItems(): List<IFlexible<*>> {
        val items = arrayListOf<IFlexible<*>>()
        items.add(
            MyMessagesTextItemKt(
                "1",
                "Hi tcqq! This is an automated reminder that your meeting with Mackenzie from Toptal starts in 30 minutes (at 9:00 pm).",
                "2019-06-02 10:00:00"
            )
        )
        items.add(
            MyMessagesTextItemKt(
                "2",
                "Thanks",
                "2019-06-02 10:00:00"
            )
        )
        items.add(
            MyMessagesTextItemKt(
                "3",
                "Hello",
                "2019-06-02 10:00:00"
            )
        )
        return items
    }
}