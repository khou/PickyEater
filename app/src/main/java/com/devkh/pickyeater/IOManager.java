package com.devkh.pickyeater;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Will handle user input and randomization algorithm
 */
public class IOManager {

    private HashSet<String> mUserInputs = new HashSet<>(); // default size

    /**
     * Add user entry to the UserInputs HashSet
     *
     * @param entry entry to be added
     */
    public void addEntry(String entry) {
        mUserInputs.add(entry);
        Log.v("Input Entries", mUserInputs.toString());
    }

    /**
     * Retrieve user entries as an Iterator
     * @return Iterator of the UserInputs HashSet
     */
    public Iterator<String> getEntries() {
        return mUserInputs.iterator();
    }

    /**
     * Clear existing entries in the UserInputs HashSet
     */
    public void clearEntries() {
        mUserInputs.clear();
        Log.v("OnPause", "Entries are cleared " + mUserInputs.toString());
    }
}
