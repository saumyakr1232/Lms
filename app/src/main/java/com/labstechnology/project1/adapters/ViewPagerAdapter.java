package com.labstechnology.project1.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.labstechnology.project1.QuizFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "ViewPagerAdapter";

    private ArrayList<QuizFragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();


    public ViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<QuizFragment> fragments) {
        super(fm);
        this.fragments = fragments;

        for (int i = 0; i < fragments.size(); i++) {
            titles.add("Q " + String.valueOf(i + 1));

        }

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
