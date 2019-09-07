package com.example.cmgandroidappdemo.ui.tab2;
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
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_Auto_Send;
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_Jackpot_Settings;
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_MFC_Code;
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_Panel_Effect;
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_Sign_ID;
import com.example.cmgandroidappdemo.tab2.sub.Tab2_Sub_State_Code;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter_tab2 extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab2_sub_1,
            R.string.tab2_sub_2,
            R.string.tab2_sub_3,
            R.string.tab2_sub_4,
            R.string.tab2_sub_5,
            R.string.tab2_sub_6
    };
    private final Context mContext;

    public SectionsPagerAdapter_tab2(Context context, FragmentManager fm) {
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
                Tab2_Sub_MFC_Code tab2_sub_mfc_code = new Tab2_Sub_MFC_Code();
                return tab2_sub_mfc_code;
            case 1:
                Tab2_Sub_State_Code tab2_sub_state_code = new Tab2_Sub_State_Code();
                return tab2_sub_state_code;
            case 2:
                Tab2_Sub_Sign_ID tab2_sub_sign_id = new Tab2_Sub_Sign_ID();
                return tab2_sub_sign_id;
            case 3:
                Tab2_Sub_Panel_Effect tab2_sub_panel_effect = new Tab2_Sub_Panel_Effect();
                return tab2_sub_panel_effect;
            case 4:
                Tab2_Sub_Jackpot_Settings tab2_sub_jackpot_settings = new Tab2_Sub_Jackpot_Settings();
                return tab2_sub_jackpot_settings;
            case 5:
                Tab2_Sub_Auto_Send tab2_sub_auto_send = new Tab2_Sub_Auto_Send();
                return tab2_sub_auto_send;

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
        //return 3;
        return TAB_TITLES.length;
    }


}