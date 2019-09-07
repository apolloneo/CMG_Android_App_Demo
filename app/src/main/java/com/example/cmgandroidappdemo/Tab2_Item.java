package com.example.cmgandroidappdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;
import com.example.cmgandroidappdemo.ui.tab2.SectionsPagerAdapter_tab2;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Tab2_Item extends Fragment {
    private SharedViewModelUnderTab2 tab2SharedViewModel;
    private TextView textView_Protocol;
    private ArrayList<String> window_effect_code_set = new ArrayList<>();
    private ArrayList<String> jp_value_set = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_item, container,false);
        //register the view model here, notice the fragment life cycle.
        //tab2SharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
        textView_Protocol = rootView.findViewById(R.id.textView_BuildProtocol);

        //setContentView(R.layout.activity_main);
        SectionsPagerAdapter_tab2 sectionsPagerAdapter_tab2 = new SectionsPagerAdapter_tab2(getContext(), getChildFragmentManager());
        ViewPager viewPager_tab2 = rootView.findViewById(R.id.ViewPager_Tab2);
        viewPager_tab2.setAdapter(sectionsPagerAdapter_tab2);
        viewPager_tab2.setOffscreenPageLimit(5);
        TabLayout tabs = rootView.findViewById(R.id.TabLayout_Tab2Sub);
        tabs.setupWithViewPager(viewPager_tab2);

        //put initial value to the input text box
        //String str_sign_id = textView_Protocol.getText().toString().substring(11, 17);
        //tab2SharedViewModel.setSign_ID(str_sign_id);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tab2SharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
        tab2SharedViewModel.getMfc_Code().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                String str_Protocol = textView_Protocol.getText().toString();
                String str_TempMFC = str_Protocol.substring(1,3);
                String str_RestofProtocol = str_Protocol.substring(3);
                //if (!str_TempMFC.equals(charSequence.toString())){
                    if (charSequence.toString().equals("CM")) {
                        //str = str.replaceAll("<[A-Z](\\d)>", "<L$1>"); //keyword: capture group
                        //regex: [A-Z](?=\d) means find capital letter followed with a number.
                        str_RestofProtocol=str_RestofProtocol.replaceAll("<[A-Z](?=\\d)", "<W");
                    }else if (charSequence.toString().equals("PL")) {
                        str_RestofProtocol=str_RestofProtocol.replaceAll("<[A-Z](?=\\d)", "<L");
                    }
                    textView_Protocol.setText("<" + charSequence + str_RestofProtocol);
                //}
            }
        });

        tab2SharedViewModel.getState_Code().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                String str_Protocol = textView_Protocol.getText().toString();
                String str_tempStateCode = str_Protocol.substring(3,5);
                String s1,s2;
                s1 = str_Protocol.substring(0,3);
                s2 = str_Protocol.substring(5, str_Protocol.length());
                if (!str_tempStateCode.equals(charSequence.toString())){
                    textView_Protocol.setText(s1 + charSequence + s2);
                }

            }
        });

        tab2SharedViewModel.getSign_ID().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                String str_Protocol = textView_Protocol.getText().toString();
                String str_tempSignID = str_Protocol.substring(11, 17);
                String s1, s2;
                s1 = str_Protocol.substring(0, 11);
                s2 = str_Protocol.substring(17);
                if (!str_tempSignID.equals(charSequence.toString())){
                    textView_Protocol.setText(s1+ charSequence + s2);
                }
            }
        });

        tab2SharedViewModel.getPanel_Effect().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                String str_Protocol = textView_Protocol.getText().toString();
                String s1, s2;
                s1 = str_Protocol.substring(0,18);
                s2 = "";
                int pos1 = str_Protocol.indexOf("<L1>", 18);
                int pos2 = str_Protocol.indexOf("<W1>", 18);
                if (pos1 != -1 && pos2 == -1){
                    s2 = str_Protocol.substring(pos1);
                }else if (pos1 == -1 && pos2 != -1){
                    s2 = str_Protocol.substring(pos2);
                }else {
                    Toast.makeText(getContext(), "Something wrong with the protocol jackpot window", Toast.LENGTH_SHORT).show();
                }
                if (charSequence.toString() == "") {
                    textView_Protocol.setText(s1 + s2);
                }else {
                    textView_Protocol.setText(s1 + "<" + charSequence + ">" + s2);
                }
            }
        });

        tab2SharedViewModel.getWindow_qty().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                String str_Protocol = textView_Protocol.getText().toString();
                String mfc_Code = str_Protocol.substring(1,3);
                int window_qty = Integer.valueOf(charSequence.toString());
                String str1 = "", str2="";
                int pos1;
                if (mfc_Code.equals("CM")){
                    pos1 = str_Protocol.indexOf("<W1>");
                    str1 = str_Protocol.substring(0,pos1);
                    for (int i = 0; i < window_qty; i++){
                        if (!window_effect_code_set.isEmpty()) {
                            str2 = str2 + "<W" + Integer.toString(i + 1) + "><" + window_effect_code_set.toArray()[i] + ">";
                        }else{
                            str2 = str2 + "<W" + Integer.toString(i + 1) + "><FS>";
                        }
                    }
                    str2 = str2 + "<E>";
                }
                textView_Protocol.setText(str1+str2);

            }
        });

        tab2SharedViewModel.getWindow_effect_set().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                window_effect_code_set.clear();
                window_effect_code_set = strings;
            }
        });

        tab2SharedViewModel.getJp_value_set().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                jp_value_set.clear();
                jp_value_set = strings;
            }
        });

    }

}
