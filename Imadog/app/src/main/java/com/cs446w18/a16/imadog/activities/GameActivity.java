package com.cs446w18.a16.imadog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.cs446w18.a16.imadog.R;
import com.cs446w18.a16.imadog.fragments.DayFragment;
import com.cs446w18.a16.imadog.fragments.IntroFragment;
import com.cs446w18.a16.imadog.fragments.NavigationBarFragment;
import com.cs446w18.a16.imadog.fragments.NightFragment;
import com.cs446w18.a16.imadog.fragments.OutroFragment;
import com.cs446w18.a16.imadog.fragments.QuestionFragment;
import com.cs446w18.a16.imadog.fragments.VictimFragment;
import com.cs446w18.a16.imadog.fragments.VoteFragment;
import com.cs446w18.a16.imadog.model.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class GameActivity extends SuperActivity implements QuestionFragment.Delegate, VoteFragment.Delegate {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    // Stores the current fragment
    Fragment currentFragment;


    /* ----------------------------- SETUP ----------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        switchToFragment(new IntroFragment(), null);
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Main method to switch between the different game states
    public void switchToFragment(Fragment frag, Bundle arguments) {
        currentFragment = frag;
        frag.setArguments(arguments);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, frag);
        transaction.commit();

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

        switchToFragment(new QuestionFragment(), arguments);
    }

    /**
     * Shows the vote page, and start the timer.
     * The app will wait on this page till showVictimPage() is called, even after the countdown is over.
     *
     * @param question: the question to display (this question should always be the DOG question).
     * @param answers: a map of the answers on the form <playerName, answer>
     */
    public void showVotePage(String question, HashMap<String, String> answers) {
        Bundle arguments = new Bundle();
        arguments.putString("title", question);
        arguments.putBoolean("isNight", false);
        arguments.putSerializable("answers", answers);

        switchToFragment(new VoteFragment(), arguments);
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
     * @param players: a list of the remaining players' names
     */
    public void showNightVotePage(String title, ArrayList<String> players) {
        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putBoolean("isNight", true);
        arguments.putStringArrayList("players", players);

        switchToFragment(new VoteFragment(), arguments);
    }

    /**
     * Show the outro page.
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
     * Finish the game and go back to lobby.
     */
    public void finishGame() {
        finish();
    }


    /* ----------------------------- CONTROLLER METHODS ----------------------------- */

    // KAREN: These methods are called by a user input.
    // You should call methods in the model in these methods.

    /**
     * Called when the user entered an answer and pressed Enter.
     * This method might be called several times, if the user changes his answer.
     *
     * @param answer: the text the user entered
     */
    @Override
    public void answeredQuestion(String answer) {
        Log.d("Imadog", "Question answered: "+answer);

        // TEST: show next page with dummy values.
        if (GameConstants.INTERFACE_TEST) {
            HashMap<String, String> answers = new HashMap<String, String>();
            answers.put("Alice", "Answer from Alice");
            answers.put("Bob", "Answer from Bob");
            answers.put("Carol", "Answer from Carol");
            showVotePage(GameConstants.dogQuestions[0], answers);
        }

        // Do something here
    }

    /**
     * Called when the user voted for someone by selecting in the list.
     * This method might be called several times, every time the user tap on a name.
     *
     * NOTE: I did not implement the lists yet, so I created a dummy vote.
     * This method will be called after 2 seconds, with the first name of the given list in
     * showVotePage(...) or showNightVotePage(...)
     *
     * @param playerName: the name of the player voted for
     */
    @Override
    public void changedVoteFor(String playerName) {
        Log.d("Imadog", "Voted for: "+playerName);

        // Do something here
    }

    /**
     * Called when the left button of the navigation bar is pressed
     */
    public void leftBarButtonWasPressed(View view) {
        Intent helpIntent = new Intent(GameActivity.this, HelpActivity.class);
        startActivity(helpIntent);
    }

    /**
     * Called when the right button of the navigation bar is pressed
     */
    public void rightBarButtonWasPressed(View view) {
        Intent profileIntent = new Intent(GameActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }
}
