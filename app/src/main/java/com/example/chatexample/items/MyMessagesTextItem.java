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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Perry Lance
 * @since 2019-06-02 Created
 */
public class MyMessagesTextItem extends AbstractFlexibleItem<MyMessagesTextItem.ViewHolder> {

    private String id;
    private String messageText;
    private String date;

    public MyMessagesTextItem(String id, String messageText, String date) {
        this.id = id;
        this.messageText = messageText;
        this.date = date;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MyMessagesTextItem) {
            MyMessagesTextItem inItem = (MyMessagesTextItem) inObject;
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
        return R.layout.item_my_messages_text;
    }

    @Override
    public MyMessagesTextItem.ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new MyMessagesTextItem.ViewHolder(view, adapter);
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
        holder.messageStatus.setText(date);

        if (position != 0 && adapter.getItemViewType(position - 1) == getLayoutRes()) {
            MyMessagesTextItem afterItem = (MyMessagesTextItem) adapter.getCurrentItems().get(position - 1);
            //本条消息距离上一条消息超过一分钟，本条消息要么是头，要么是整个
            if (isCloseEnough(strToTime(date), strToTime(afterItem.date))) {
                if (position == adapter.getItemCount() - 1) {
                    //最后一条消息，那就是整个
                    setBubbleType(holder, MessageBubbleType.SINGLE);
                } else {
                    //如果本条消息距离下一条消息超过1分钟，那就是整个
                    if (position < adapter.getItemCount() - 1
                            && adapter.getItemViewType(position + 1) == getLayoutRes()
                    ) {
                        MyMessagesTextItem beforeItem = (MyMessagesTextItem) adapter.getCurrentItems().get(position + 1);
                        if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                            setBubbleType(holder, MessageBubbleType.SINGLE);
                        } else {
                            setBubbleType(holder, MessageBubbleType.HEADER);
                        }
                    } else {
                        setBubbleType(holder, MessageBubbleType.SINGLE);
                    }
                }
            } else {
                //本条消息距离上一条消息不超过一分钟，本条消息要么是身体，要么是尾
                if (position < adapter.getItemCount() - 1
                        && adapter.getItemViewType(position + 1) == getLayoutRes()
                ) {
                    MyMessagesTextItem beforeItem = (MyMessagesTextItem) adapter.getCurrentItems().get(position + 1);
                    if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                        setBubbleType(holder, MessageBubbleType.FOOTER);
                    } else {
                        setBubbleType(holder, MessageBubbleType.BODY);
                    }
                } else {
                    //最后一条消息，是气泡尾
                    setBubbleType(holder, MessageBubbleType.FOOTER);
                }
            }
        } else if (adapter.getItemViewType(position + 1) == getLayoutRes()) {
            MyMessagesTextItem beforeItem = (MyMessagesTextItem) adapter.getCurrentItems().get(position + 1);
            if (adapter.getItemCount() != 1) {
                if (adapter.getItemCount() >= position + 2) {
                    if (isCloseEnough(strToTime(date), strToTime(beforeItem.date))) {
                        setBubbleType(holder, MessageBubbleType.SINGLE);
                    } else {
                        setBubbleType(holder, MessageBubbleType.HEADER);
                    }
                } else {
                    setBubbleType(holder, MessageBubbleType.SINGLE);
                }
            } else {
                setBubbleType(holder, MessageBubbleType.SINGLE);
            }
        }
    }

    private long strToTime(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }

    private boolean isCloseEnough(long current, long last) {
        long var4 = current - last;
        if (var4 < 0) {
            var4 = -var4;
        }
        return var4 > 60000L;
    }

    private void setBubbleType(ViewHolder holder, MessageBubbleType bubbleType) {
        float MESSAGE_BUBBLE_RADIUS_LARGE = 24F;
        float MESSAGE_BUBBLE_RADIUS_SMALL = 4F;
        switch (bubbleType) {
            case HEADER:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                break;
            case BODY:
                holder.messageContent.setTopLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setTopRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                holder.messageContent.setBottomLeftRadiusDp(MESSAGE_BUBBLE_RADIUS_LARGE);
                holder.messageContent.setBottomRightRadiusDp(MESSAGE_BUBBLE_RADIUS_SMALL);
                break;
            case FOOTER:
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

    public class ViewHolder extends FlexibleViewHolder {

        AppCompatTextView messageText;
        FrameLayout messageTextAndInfo;
        RoundRectView messageContent;
        public AppCompatTextView messageStatus;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            messageText = view.findViewById(R.id.message_text);
            messageTextAndInfo = view.findViewById(R.id.message_text_and_info);
            messageContent = view.findViewById(R.id.message_content);
            messageStatus = view.findViewById(R.id.message_status);
        }
    }
}
