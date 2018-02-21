package com.cs446w18.a16.imadog.model;

/**
 * Created by lacie on 2018-02-17.
 */

public class GameConstants {
    public static final String[] dogQuestions = {
            "What did you have for breakfast today?",
            "What song would you play at your funeral?",
            "Name one item in your fridge.",
            "Who is this room is the best drinker?",
            "Name your favourite singer or band.",
            "Who in this room would have a 3-hour conversation about the meaning of life?",
            "Who in this room would win the lottery and blow it all in a week?",
            "What is the most expensive thing you bought?",
            "What is your life motto",
            "Who is your celebrity spirit animal?",
            "How many drinks does it take to get you drunk?",
            "How much cash do you have in your wallet?",
            "How long has it been since you have last been in a physical fight?",
            "Describe your current mood in one word",
            "Do you like dogs or cats?",
            "What time of the day are you most awake?",
            "Who in this room can become friends with anybody?",
            "Who in this room takes forever to reply to your texts?",
            "Who in this room is most likely to end up in jail?",
            "What song describes your life?"
    };

    public static final String[] catQuestions = {
            "What did you have for dinner last night?",
            "Name the last song you listened to.",
            "What is your least favourite food?",
            "Name a random person in this room.",
            "Name any singer or band.",
            "Name a random person in this room.",
            "Name a random person in this room.",
            "What is the last thing you bought?",
            "Name a famous saying.",
            "Name the last celebrity you saw on the internet.",
            "Pick a number from one to ten.",
            "Pick a number from $1 to $500.",
            "How long has it been since you last talked to your parents?",
            "Pick any word",
            "Which do you hate, dogs or cats?",
            "What time of the day are most likely asleep?",
            "Name the person in this room you know the least.",
            "Name a random person in this room.",
            "Name a random person in this room.",
            "What is your least favourite song?"
    };

    public static final int minPlayers = 4;

    // Duration of the vote during the day, displayed on a counter (in seconds)
    public static final int dayVoteDuration = 10;

    // Duration of the vote during the night, displayed on a counter (in seconds)
    public static final int nightVoteDuration = 5;


    // KAREN: I used this boolean to replace the call of the model to change the pages in the navigation.
    // When you start to link the model to the interface, set this to false.
    // This will disable all the dummy calls I do in different places.
    // If this is set to false and the model is not implemented, the app will just be stuck on one page, waiting...

    // TEST boolean used for navigating in the interface without the model (created by JB)
    public static final boolean INTERFACE_TEST = true;

}
