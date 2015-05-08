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

    private HashSet<String> mUserInputs = new HashSet<>(); // default size
    private String location = "San Fransisco";
    private ArrayList<String> entries = new ArrayList<>();

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
     * Retrieve user entries as an Iterator
     *
     * @return Iterator of the UserInputs HashSet
     */
    public String getSelectedEntry() {
        this.toArrayList();
        return this.selectEntryAlgorithm();
    }

    private String selectEntryAlgorithm() {
        if(!entries.isEmpty()){
            long seed = System.nanoTime();
            Collections.shuffle(entries, new Random(seed));
            Collections.shuffle(entries, new Random(seed));
            return entries.remove(0); // first in list
        }
        return ""; // if list is empty, return ""
    }

    private void toArrayList() {
        Iterator<String> it = mUserInputs.iterator();
        while (it.hasNext()) {
            entries.add(it.next());
            it.remove();
        }
    }

    /**
     * Clear existing entries in the UserInputs HashSet
     */
    public void clearEntries() {
        mUserInputs.clear();
        Log.v("OnPause", "Entries are cleared " + mUserInputs.toString());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location != null) this.location = location;
    }
}

