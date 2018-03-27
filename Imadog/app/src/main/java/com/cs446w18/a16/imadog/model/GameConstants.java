package com.cs446w18.a16.imadog.model;

public class GameConstants {
    public static final String[] dogQuestions = {
            "What did you have for breakfast today?",
            "How many drinks does it take to get you drunk?",
            "How much cash do you have in your wallet?",
            "Describe your current mood in one word",
            "Do you like dogs or cats?"
    };

    public static final String[] catQuestions = {
            "What did you have for dinner last night?",
            "Pick a number from one to ten",
            "Pick a number from $1 to $500",
            "Pick any word",
            "Which do you hate, dogs or cats?"
    };

    public static final int minPlayers = 4;

    // GamePresenter Timer duration in milliseconds
    public static final int questionPageDuration = 50000;
    public static final int dayPollPageDuration = 60000;
    public static final int nightPollPageDuration = 30000;
    public static final int victimPageDuration = 10000;
    public static final int introDuration = 20000;
    public static final int dayNightDuration = 15000;

    public static final String DAY_PAGE = "DAY_PAGE";
    public static final String DAY_VOTE_PAGE = "DAY_VOTE_PAGE";
    public static final String QUESTION_PAGE = "QUESTION_PAGE";
    public static final String NIGHT_PAGE = "NIGHT_PAGE";
    public static final String NIGHT_VOTE_PAGE = "NIGHT_VOTE_PAGE";
    public static final String VICTIM_PAGE = "VICTIM_PAGE";

}
