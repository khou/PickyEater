package com.devkh.pickyeater;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Will handle user input and randomization algorithm
 */
public class EntriesManager {

    private HashSet<String> mUserInputs = new HashSet<>();
    private String mLocation = "San Fransisco"; // default location
    private ArrayList<String> mEntries = new ArrayList<>();

    /**
     * Add user entry to the UserInputs HashSet
     *
     * @param entry entry to be added
     */
    public void addEntry(String entry) {
        mUserInputs.add(entry.toLowerCase());
        Log.v("Input Entries", mUserInputs.toString());
    }

    /**
     * Retrieve user mEntries as an Iterator
     *
     * @return Iterator of the UserInputs HashSet
     */
    public String getSelectedEntry() {
        this.toArrayList();
        return this.selectEntryAlgorithm();
    }

    private String selectEntryAlgorithm() {
        if (!mEntries.isEmpty()) {
            long seed = System.nanoTime();
            Collections.shuffle(mEntries, new Random(seed));
            Collections.shuffle(mEntries, new Random(seed));
            return mEntries.remove(0); // first in list
        }
        return ""; // if list is empty, return ""
    }

    private void toArrayList() {
        Iterator<String> it = mUserInputs.iterator();
        while (it.hasNext()) {
            mEntries.add(it.next());
            it.remove();
        }
    }

    /**
     * Clear existing mEntries in the UserInputs HashSet
     */
    public void clearEntries() {
        mUserInputs.clear();
        Log.v("OnPause", "Entries are cleared " + mUserInputs.toString());
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        if (location != null) this.mLocation = location;
    }
}

