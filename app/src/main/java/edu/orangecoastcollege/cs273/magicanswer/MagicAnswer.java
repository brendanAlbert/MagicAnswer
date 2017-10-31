package edu.orangecoastcollege.cs273.magicanswer;

import android.content.Context;

import java.util.Random;

/**
 * MagicAnswer randomly (well, pseudo-randomly) generates an answer to a question.
 * Answers are drawn from a string-array (magic_answers_list) in the strings.xml file.
 *
 * @author Michael Paulding
 */
public class MagicAnswer {
    private String[] mAllAnswers;

    /**
     * Gathers all the possible answers from an array in strings.xml
     * Needs a context to perform a lookup of these strings.
     * @param context The context to associate with.
     */
    public MagicAnswer(Context context) {
        //COLLECT ALL THE POSSIBLE ANSWERS FROM THE ARRAY IN STRINGS.XML
        mAllAnswers = context.getResources().getStringArray(R.array.magic_answer_list);
    }

    /**
     * Gets a random answer to a question.
     * @return A random answer
     */
    public String getRandomAnswer() {
        Random rng = new Random();
        int randomPosition = rng.nextInt(mAllAnswers.length);
        return mAllAnswers[randomPosition];
    }

}
