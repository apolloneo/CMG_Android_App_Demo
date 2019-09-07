package com.example.cmgandroidappdemo.ui.main;

import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.Tab1_Main;
import com.example.cmgandroidappdemo.Tab2_Item;
import com.example.cmgandroidappdemo.Tab3_TBD;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        switch (position){
            case 0:
                Tab1_Main tab1_main;

                tab1_main = new Tab1_Main();


                return tab1_main;
            case 1:
                Tab2_Item tab2_item = new Tab2_Item();
                return tab2_item;
            case 2:
                Tab3_TBD tab3_tbd = new Tab3_TBD();
                return tab3_tbd;

                default:
                    return null;


        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }


}