package com.example.chatexample.items

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatexample.R
import com.example.chatexample.enums.MessageBubbleType
import com.example.chatexample.utils.ColorUtils
import com.example.chatexample.utils.PalletUtils
import com.github.florent37.shapeofview.shapes.RoundRectView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.SimpleDateFormat
import java.util.*

private const val MESSAGE_BUBBLE_RADIUS_LARGE = 24F
private const val MESSAGE_BUBBLE_RADIUS_SMALL = 4F

/**
 * @author Alan Dreamer
 * @since 2019-05-30 Created
 */
data class MyMessagesTextItemKt(
    val id: String,
    val messageText: String,
    val date: String
) : AbstractFlexibleItem<MyMessagesTextItemKt.ViewHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.item_my_messages_text
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
        val context = holder.itemView.context
        holder.messageTextAndInfo.setBackgroundColor(
            PalletUtils.modifyAlpha(
                ColorUtils.getThemeColor(
                    R.attr.colorSecondary,
                    context
                ), 0.25F
            )
        )
        holder.messageText.text = messageText
        holder.messageStatus.text = date

        if (position != 0 && adapter.getItemViewType(position - 1) == layoutRes) {
            val afterItem = adapter.currentItems[position - 1] as MyMessagesTextItemKt
            //本条消息距离上一条消息超过一分钟，本条消息要么是头，要么是整个
            if (isCloseEnough(strToTime(date), strToTime(afterItem.date))) {
                if (position == adapter.itemCount - 1) {
                    //最后一条消息，那就是整个
                    setBubbleType(holder, MessageBubbleType.SINGLE)
                } else {
                    //如果本条消息距离下一条消息超过1分钟，那就是整个
                    if (position < adapter.itemCount - 1
                        && adapter.getItemViewType(position + 1) == layoutRes
                    ) {
                        val beforeItem = adapter.currentItems[position + 1] as MyMessagesTextItemKt
                        if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                            setBubbleType(holder, MessageBubbleType.SINGLE)
                        } else {
                            setBubbleType(holder, MessageBubbleType.HEADER)
                        }
                    }
                }
            } else {
                //本条消息距离上一条消息不超过一分钟，本条消息要么是身体，要么是尾
                if (position < adapter.itemCount - 1
                    && adapter.getItemViewType(position + 1) == layoutRes
                ) {
                    val beforeItem = adapter.currentItems[position + 1] as MyMessagesTextItemKt
                    if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                        setBubbleType(holder, MessageBubbleType.FOOTER)
                    } else {
                        setBubbleType(holder, MessageBubbleType.BODY)
                    }
                } else {
                    //最后一条消息，是气泡尾
                    setBubbleType(holder, MessageBubbleType.FOOTER)
                }
            }
        } else if (adapter.getItemViewType(position + 1) == layoutRes) {
            val beforeItem = adapter.currentItems[position + 1] as MyMessagesTextItemKt
            if (adapter.itemCount != 1) {
                if (adapter.itemCount >= position + 2) {
                    if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                        setBubbleType(holder, MessageBubbleType.SINGLE)
                    } else {
                        setBubbleType(holder, MessageBubbleType.HEADER)
                    }
                } else {
                    setBubbleType(holder, MessageBubbleType.SINGLE)
                }
            } else {
                setBubbleType(holder, MessageBubbleType.SINGLE)
            }
        }
    }

    private fun strToTime(str: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date?
        date = simpleDateFormat.parse(str)
        return date.time
    }

    private fun isCloseEnough(current: Long, last: Long): Boolean {
        var var4 = current - last
        if (var4 < 0) {
            var4 = -var4
        }
        return var4 > 60000L
    }

    private fun setBubbleType(holder: ViewHolder, bubbleType: MessageBubbleType) {
        when (bubbleType) {
            MessageBubbleType.HEADER -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
            }
            MessageBubbleType.BODY -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
            }
            MessageBubbleType.FOOTER -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
            }
            MessageBubbleType.SINGLE -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
            }
        }
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
        var messageText: AppCompatTextView = view.findViewById(R.id.message_text)
        var messageTextAndInfo: FrameLayout = view.findViewById(R.id.message_text_and_info)
        var messageContent: RoundRectView = view.findViewById(R.id.message_content)
        var messageStatus: AppCompatTextView = view.findViewById(R.id.message_status)
    }
}