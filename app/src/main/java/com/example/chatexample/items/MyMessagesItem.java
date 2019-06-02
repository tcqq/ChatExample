package com.example.chatexample.items;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.chatexample.R;
import com.example.chatexample.enums.MessageBubbleType;
import com.example.chatexample.utils.ColorUtils;
import com.example.chatexample.utils.PalletUtils;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.List;

/**
 * @author Alan Dreamer
 * @since 2019-06-02 Created
 */
public class MyMessagesItem extends AbstractFlexibleItem<MyMessagesItem.ViewHolder> {

    private String id;
    private String messageText;
    private String date;
    private MessageBubbleType bubbleType;

    public MyMessagesItem(String id, String messageText, String date, MessageBubbleType bubbleType) {
        this.id = id;
        this.messageText = messageText;
        this.date = date;
        this.bubbleType = bubbleType;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MyMessagesItem) {
            MyMessagesItem inItem = (MyMessagesItem) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_my_messages;
    }

    @Override
    public MyMessagesItem.ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new MyMessagesItem.ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        Context context = holder.itemView.getContext();
        holder.messageTextAndInfo.setBackgroundColor(
                PalletUtils.INSTANCE.modifyAlpha(
                        ColorUtils.INSTANCE.getThemeColor(
                                R.attr.colorSecondary,
                                context
                        ), 0.25F
                )
        );
        holder.messageText.setText(messageText);

        float MESSAGE_BUBBLE_RADIUS_LARGE = 24F;
        float MESSAGE_BUBBLE_RADIUS_SMALL = 4F;

        switch (bubbleType) {
            case TOP:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                break;
            case MIDDLE:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                break;
            case BOTTOM:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                break;
            case SINGLE:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                break;
        }
    }


    class ViewHolder extends FlexibleViewHolder {

        AppCompatTextView messageText;
        FrameLayout messageTextAndInfo;
        RoundRectView messageContent;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            messageText = view.findViewById(R.id.message_text);
            messageTextAndInfo = view.findViewById(R.id.message_text_and_info);
            messageContent = view.findViewById(R.id.message_content);
        }
    }
}
