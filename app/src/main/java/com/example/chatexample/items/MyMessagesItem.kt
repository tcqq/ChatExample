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

private const val MESSAGE_BUBBLE_RADIUS_LARGE = 24F
private const val MESSAGE_BUBBLE_RADIUS_SMALL = 4F

/**
 * @author Alan Dreamer
 * @since 2019-05-30 Created
 */
data class MyMessagesItem(
    val id: String,
    val messageText: String,
    val date: String,
    val bubbleType: MessageBubbleType
) : AbstractFlexibleItem<MyMessagesItem.ViewHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.item_my_messages
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

        when (bubbleType) {
            MessageBubbleType.TOP -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
            }
            MessageBubbleType.MIDDLE -> {
                holder.messageContent.topLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.topRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
                holder.messageContent.bottomLeftRadiusDp = MESSAGE_BUBBLE_RADIUS_LARGE
                holder.messageContent.bottomRightRadiusDp = MESSAGE_BUBBLE_RADIUS_SMALL
            }
            MessageBubbleType.BOTTOM -> {
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

/*        val time1 = "2019-06-02 19:00:00"
        val time2 = "2019-06-02 19:01:00"

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date1: Date?
        val date2: Date?
        date1 = format.parse(time1)
        date2 = format.parse(time2)
        val difference = date2.time - date1.time
        val diffMinutes = difference / (60 * 1000) % 60
        println("$diffMinutes minutes")*/
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
        var messageText: AppCompatTextView = view.findViewById(R.id.message_text)
        var messageTextAndInfo: FrameLayout = view.findViewById(R.id.message_text_and_info)
        var messageContent: RoundRectView = view.findViewById(R.id.message_content)
    }
}