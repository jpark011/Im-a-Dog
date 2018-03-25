package com.cs446w18.a16.imadog.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.fragments.ChatFragment;
import com.cs446w18.a16.imadog.fragments.DayFragment;
import com.cs446w18.a16.imadog.fragments.HelpFragment;
import com.cs446w18.a16.imadog.fragments.IntroFragment;
import com.cs446w18.a16.imadog.fragments.NavigationBarFragment;
import com.cs446w18.a16.imadog.fragments.NightFragment;
import com.cs446w18.a16.imadog.fragments.OutroFragment;
import com.cs446w18.a16.imadog.fragments.ProfileFragment;
import com.cs446w18.a16.imadog.fragments.QuestionFragment;
import com.cs446w18.a16.imadog.fragments.SuperFragment;
import com.cs446w18.a16.imadog.fragments.VictimFragment;
import com.cs446w18.a16.imadog.fragments.VoteFragment;
import com.cs446w18.a16.imadog.model.GameConstants;
import com.cs446w18.a16.imadog.views.VoteListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class GameActivity extends SuperActivity {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    // Stores the current fragment
    Fragment currentFragment;

    // The view pager and page adapter for the tabs
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    // The different tabs
    private ArrayList<Fragment> tabs;

    // The chat fragment (special reference for update)
    private ChatFragment chat;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Global.user.setView(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // View pager
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

        chat = new ChatFragment();

        tabs = new ArrayList<>();
        tabs.add(new IntroFragment());
        tabs.add(chat);
        tabs.add(new ProfileFragment());
        tabs.add(new HelpFragment());

        switchToFragment(new IntroFragment(), null);
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Main method to switch between the different game states
    public void switchToFragment(final Fragment frag, final Bundle arguments) {

        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                hideSystemUI();


                currentFragment = frag;
                frag.setArguments(arguments);
                tabs.remove(0);
                tabs.add(0, frag);

                mPager.setAdapter(mPagerAdapter);
            }
        };
        mainHandler.post(myRunnable);

    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }


    /* ----------------------------- VIEW METHODS ----------------------------- */

    // KAREN: These methods are the methods you should call from the model to update the view.
    // These methods transition to different pages.
    // The pages correspond to the one in the picture I sent on the group.
    //
    // The model should have a property linked to this class (GameActivity).
    // This is the only class the model needs to know.

    /**
     * Shows the day page.
     * The app will wait on this page till showQuestionPage() is called.
     */
    public void showDayPage() {
        switchToFragment(new DayFragment(), null);
    }

    /**
     * Shows the question page.
     * The app will wait on this page till showVotePage() is called, even after the user answered.
     *
     * @param question: the question to display (depends on the player's role).
     */
    public void showQuestionPage(String question) {
        Bundle arguments = new Bundle();
        arguments.putString("question", question);

        Global.endTimer();
        switchToFragment(new QuestionFragment(), arguments);
    }

    /**
     * Shows the vote page, and start the timer.
     * The app will wait on this page till showVictimPage() is called, even after the countdown is over.
     *
     * @param question: the question to display (this question should always be the DOG question).
     * @param answers: a map of the answers on the form <playerName, answer>
     */
    public void showVotePage(String question, HashMap<String, Integer> votes, HashMap<String, String> answers) {
        Bundle arguments = new Bundle();
        arguments.putString("title", question);
        arguments.putBoolean("isNight", false);
        arguments.putSerializable("votes", votes);
        arguments.putSerializable("answers", answers);

        Global.endTimer();
        switchToFragment(new VoteFragment(), arguments);
    }

    /**
     *
     */
    public void updatePollVotes(HashMap<String, Integer> votes) {
        final HashMap<String, Integer> count = votes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                VoteFragment frag = (VoteFragment)tabs.get(0);
                frag.updatePollVotes(count);
            }
        });
    }

    /**
     * Shows the victim page.
     * The app will wait until either showNightPage() (if game continues) or showOutro() (if game over) is called.
     *
     * @param victimName: the name of the victim, e.g. Player1. Pass "" if no one was killed.
     * @param victimRole: the role of the victim, e.g. Cat. Pass "" if no one was killed.
     */
    public void showVictimPage(String victimName, String victimRole) {
        Bundle arguments = new Bundle();
        arguments.putString("victimName", victimName);
        arguments.putString("victimRole", victimRole);

        switchToFragment(new VictimFragment(), arguments);
    }

    /**
     * Shows the night page
     * The app will wait on this page till showNightVotePage() is called.
     */
    public void showNightPage() {
        switchToFragment(new NightFragment(), null);
    }

    /**
     * Shows the vote page (adapted for the night votes), and start the timer.
     * The app will wait on this page till showVictimPage() is called, even after the countdown is over.
     *
     * @param title: the title of the page. This might be for instance "Vote to kill a dog" for the cats,
     *             or "Vote for the best player" for the dogs
     * @param votes: a list of the remaining players' names
     */
    public void showNightVotePage(String title, HashMap<String, Integer> votes) {
        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putBoolean("isNight", true);
        arguments.putSerializable("votes", votes);

        Global.endTimer();
        switchToFragment(new VoteFragment(), arguments);
    }

    /**
     * Shows the outro page.
     *
     * @param winner: the team winning the game, e.g. "Dogs" or "Cats"
     *
     * NOTE: we should have an enum for Dog and Cat
     */
    public void showOutro(String winner) {
        Bundle arguments = new Bundle();
        arguments.putString("winner", winner);

        switchToFragment(new OutroFragment(), arguments);
    }

    /**
     * Finishes the game and go back to lobby.
     */
    public void finishGame() {
        finish();
    }

    /**
     * Updates the chat with the last messages
     * @param messages: a list of all the messages to display
     */
    public void updateChat(final ArrayList<Pair<String, String>> messages) {
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                chat.update(messages);
            }
        };
        mainHandler.post(myRunnable);
    }

    /* ----------------------------- PRESENTER METHODS ----------------------------- */

    // KAREN: These methods are called by a user input.
    // You should call methods in the model in these methods.

    /**
     * Called when the user entered an answer and pressed Enter.
     * This method might be called several times, if the user changes his answer.
     *
     * @param answer: the text the user entered
     */
    public void answeredQuestion(String answer) {
        Log.d("Imadog", "Question answered: "+answer);

        Global.user.submitAnswer(answer);
    }

    /**
     * Called when the user voted for someone by selecting in the list.
     * This method might be called several times, every time the user tap on a name.
     *
     * @param playerName: the name of the player voted for
     */
    public void changedVoteFor(String playerName) {
        Log.d("Imadog", "Voted for: "+playerName);

        Global.user.vote(playerName);
    }

    /**
     * Called when the user wrote a message in the chat
     *
     * @param message: the written message to send
     */
    public void wroteMessage(String message) {
        Log.d("Imadog", "Wrote message: "+message);

        Global.user.sendMessage(message);
    }

    /**
     * Called when the left button of the navigation bar is pressed
     */
    public void leftBarButtonWasPressed(View view) {
    }

    /**
     * Called when the right button of the navigation bar is pressed
     */
    public void rightBarButtonWasPressed(View view) {
    }
}
