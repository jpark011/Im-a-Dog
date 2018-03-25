package com.cs446w18.a16.imadog.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cs446w18.a16.imadog.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jean-Baptiste on 24/02/2018.
 */

public class VoteListView extends ListView {

    public interface Delegate {
        void playerWasSelected(String name);
    }

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    private List<String> mNames;
    private List<String> mAnswers;
    private List<Integer> mVotes;

    private Delegate delegate;

    private VoteListAdapter adapter;

    private boolean blackTheme = false;

    private int currentSelected = -1;

    public boolean isEnabled;


    /* ----------------------------- SETUP ----------------------------- */

    public VoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /// Setups the list view with the players' names and answers (Day vote)
    public void setup(Map<String, Integer> votes, Map<String, String> answers, Delegate delegate) {

        // Names
        mNames = new ArrayList<>(votes.keySet());
        java.util.Collections.sort(mNames);

        // Answers
        mAnswers = new ArrayList<>();
        for (String name: mNames) {
            mAnswers.add(answers.get(name));
        }

        // Votes
        mVotes = new ArrayList<>();
        for (String name: mNames) {
            mVotes.add(votes.get(name));
        }

        setup(delegate);
    }

    /// Setups the list view with only the players' names (Night vote)
    public void setup(Map<String, Integer> votes, Delegate delegate) {

        // Names
        mNames = new ArrayList<>(votes.keySet());
        java.util.Collections.sort(mNames);

        // Votes
        mVotes = new ArrayList<>();
        for (String name: mNames) {
            mVotes.add(votes.get(name));
        }

        setup(delegate);
    }

    /// Setups the list view with black text (Lobby)
    public void setupBlackTheme(List<String> names) {
        blackTheme = true;
        isEnabled = false;

        mNames = new ArrayList<>(names);

        setup(delegate);
    }

    private void setup(final Delegate delegate) {
        adapter = new VoteListAdapter(getContext());
        setAdapter(adapter);
        this.delegate = delegate;
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setSelector(R.drawable.row_selector);
        setDivider(null);
        isEnabled = true;

        // Set the row action
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelected = i;
                delegate.playerWasSelected(mNames.get(i));
            }
        });
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Locks the view to prevent the player from voting
    public void lock() {
        Log.d("Imadog", "Lock");
        isEnabled = false;
    }

    /// Updates the values when get a new message
    public void update(Map<String, Integer> votes) {

        // Names
        mNames = new ArrayList<>(votes.keySet());
        java.util.Collections.sort(mNames);

        // Votes
        mVotes = new ArrayList<>();
        for (String name: mNames) {
            mVotes.add(votes.get(name));
        }

        adapter.notifyDataSetChanged();
        setSelection(currentSelected);
    }


    /* ----------------------------- LIST ADAPTER ----------------------------- */

    private class VoteListAdapter extends BaseAdapter {

        private Context mContext;

        public VoteListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mNames.size();
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
        public boolean isEnabled(int position) { return isEnabled; }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // Get the row
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View row = inflater.inflate(R.layout.row_vote, viewGroup, false);

            // Change the name
            TextView nameLabel = row.findViewById(R.id.nameLabel);
            nameLabel.setText(mNames.get(i));

            // Change the answer (if applicable)
            TextView answerLabel = row.findViewById(R.id.answerLabel);
            if (mAnswers != null) {
                answerLabel.setText(mAnswers.get(i));
            }
            else {
                answerLabel.setVisibility(View.INVISIBLE);
            }

            // Change votes (if applicable)
            TextView voteLabel = row.findViewById(R.id.voteLabel);
            if (mVotes != null) {
                voteLabel.setText(String.valueOf(mVotes.get(i)));
            }
            else {
                voteLabel.setVisibility(View.INVISIBLE);
            }

            // Black theme
            if (blackTheme) {
                int blackColor = getResources().getColor(R.color.black);
                nameLabel.setTextColor(blackColor);
                answerLabel.setTextColor(blackColor);
            }

            return row;
        }
    }

}
