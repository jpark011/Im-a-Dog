package com.cs446w18.a16.imadog.fragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;

import java.util.ArrayList;

/**
 * Created by Jean-Baptiste on 2018-03-20.
 */

public class ChatFragment extends SuperFragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    // The field to enter a new message
    EditText messageField;

    // The list view displaying all the messages
    private ListView chatListView;

    // The adapter responsible of the chat
    private ChatListAdapter adapter;

    // The list of the messages
    private ArrayList<Pair<String, String>> messages;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Message field
        messageField = view.findViewById(R.id.messageField);
        messageField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        messageField.setTypeface(Global.fonts.get("OSSemibold"));

        // Message field white background
        GradientDrawable background = (GradientDrawable)messageField.getBackground().getConstantState().newDrawable().mutate();;
        background.setColor(ContextCompat.getColor(getActivity(), R.color.white));
        messageField.setBackground(background);

        // Set the return action
        TextView.OnEditorActionListener fieldListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // When the user press enter
                    getGameActivity().hideSystemUI();
                    getGameActivity().wroteMessage(textView.getText().toString());
                    textView.setText("");


                }
                return false;

            }
        };
        messageField.setOnEditorActionListener(fieldListener);

        messages = new ArrayList<>();
        adapter = new ChatFragment.ChatListAdapter(getGameActivity());

        // List view
        chatListView = view.findViewById(R.id.chatListView);
        chatListView.setAdapter(adapter);
        chatListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        chatListView.setSelector(R.drawable.row_selector);
        chatListView.setDivider(null);
        update(Global.user.getChatHistory());


        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */

    public void update(ArrayList<Pair<String, String>> newMessages) {
        messages = newMessages;
        chatListView.setAdapter(adapter);
    }

    /* ----------------------------- LIST ADAPTER ----------------------------- */

    private class ChatListAdapter extends BaseAdapter {

        private Context mContext;

        public ChatListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) { return true; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // Get the row
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View row = inflater.inflate(R.layout.row_chat, viewGroup, false);

            // Change the name
            TextView nameLabel = row.findViewById(R.id.nameLabel);
            nameLabel.setText(messages.get(i).first);

            // Change the message
            TextView messageLabel = row.findViewById(R.id.messageLabel);
            messageLabel.setText(messages.get(i).second);


            return row;
        }
    }


}
