package com.cs446w18.a16.imadog.views;

import android.content.Context;
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

    private List<String> mNames, mAnswers;
    private RadioButton radioButton;

    private Delegate delegate;

    private boolean mIsEnabled;


    /* ----------------------------- SETUP ----------------------------- */

    public VoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /// Setups the list view with the players' names and answers (Day vote)
    public void setup(Map<String, String> namesAndAnswers, Delegate delegate) {
        mNames = new ArrayList<String>(namesAndAnswers.keySet());
        mAnswers = new ArrayList<String>(namesAndAnswers.values());
        setup(delegate);
    }

    /// Setups the list view with only the players' names (Night vote)
    public void setup(List<String> names, Delegate delegate) {
        mNames = names;
        setup(delegate);
    }

    private void setup(final Delegate delegate) {
        setAdapter(new VoteListAdapter(getContext()));
        this.delegate = delegate;
        setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setSelector(R.drawable.row_selector);
        mIsEnabled = true;

        // Set the row action
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                delegate.playerWasSelected(mNames.get(i));
            }
        });
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Locks the view to prevent the player from voting
    public void lock() {
        Log.d("Imadog", "Lock");
        mIsEnabled = false;
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
        public boolean isEnabled(int position) { return mIsEnabled; }

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
                answerLabel.setAlpha(0);
            }

            // Setup radio button
//            radioButton = row.findViewById(R.id.radioButton);
//            radioButton.setClickable(false);
//            radioButton.setChecked(false);

            return row;
        }
    }

}
