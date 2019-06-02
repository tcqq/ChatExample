package com.example.chatexample.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.emoji.widget.EmojiAppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatexample.R;
import com.example.chatexample.enums.MessageBubbleType;
import com.example.chatexample.items.MessagesFooterItem;
import com.example.chatexample.items.MyMessagesItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Alan Dreamer
 * @since 2019-06-02 Created
 */
public class ConversationActivity extends RxAppCompatActivity implements
        FlexibleAdapter.OnItemClickListener {

    private FlexibleAdapter<IFlexible<?>> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Alan D.");
        }

        EmojiAppCompatEditText compose_message_text = findViewById(R.id.compose_message_text);
        FloatingActionButton voice_note_button = findViewById(R.id.voice_note_button);
        RecyclerView message_list = findViewById(R.id.message_list);

        RxTextView
                .textChanges(compose_message_text)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        voice_note_button.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
                    } else {
                        voice_note_button.setImageResource(R.drawable.ic_send_black_24dp);
                    }
                }).isDisposed();

        voice_note_button.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(compose_message_text.getText())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                adapter.addItem(
                        -1,
                        new MyMessagesItem(
                                "${adapter.itemCount + 1}",
                                "${compose_message_text.text}",
                                currentDate,
                                MessageBubbleType.SINGLE
                        )
                );
                adapter.smoothScrollToPosition(adapter.getItemCount() - 1);
                compose_message_text.setText("");
            }
        });

        adapter = new FlexibleAdapter<>(null, this, true);
        SmoothScrollLinearLayoutManager layoutManager = new SmoothScrollLinearLayoutManager(this);
        message_list.setLayoutManager(layoutManager);
        message_list.setAdapter(adapter);
        message_list.setHasFixedSize(true);

        RxView
                .layoutChangeEvents(message_list)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(viewLayoutChangeEvent -> {
                    //FIXME: 打开和关闭键盘不会导致列表的位置发生改变
                    message_list.scrollToPosition(adapter.getItemCount() - 1);
/*                  if (it.bottom < it.oldBottom) {
                        Timber.d("bottom: ${it.bottom} oldBottom: ${it.oldBottom}")
                        message_list.scrollBy(0, it.oldBottom - it.bottom)
                    }*/
                }).isDisposed();

        RxView
                .touches(message_list)
                .subscribe(motionEvent -> {
                    //FIXME: 只有向下滑动时才会关闭键盘
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                }).isDisposed();

        adapter.updateDataSet(getItems());
        adapter.addScrollableFooter(new MessagesFooterItem("FI - 1"));
    }

    @Override
    public boolean onItemClick(View view, int position) {
        return false;
    }

    private List<IFlexible<?>> getItems() {
        ArrayList<IFlexible<?>> items = new ArrayList<>();
        items.add(
                new MyMessagesItem(
                        "1",
                        "Hi tcqq! This is an automated reminder that your meeting with Mackenzie from Toptal starts in 30 minutes (at 9:00 pm).",
                        "2019-06-02 10:00:00",
                        MessageBubbleType.TOP
                )
        );
        items.add(
                new MyMessagesItem(
                        "2",
                        "Thanks",
                        "2019-06-02 10:00:00",
                        MessageBubbleType.MIDDLE
                )
        );
        items.add(
                new MyMessagesItem(
                        "3",
                        "Hello",
                        "2019-06-02 10:00:00",
                        MessageBubbleType.BOTTOM
                )
        );
        return items;
    }
}
