package com.example.cmgandroidappdemo.tab2.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;

public class Tab2_Sub_MFC_Code extends Fragment {
    private SharedViewModelUnderTab2 tab2SharedViewModel_sub1;
    private Button button_cm, button_pl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab2_sub_mfc_code, container,false);
        button_cm = rootView.findViewById(R.id.button_mfc_1);
        button_pl = rootView.findViewById(R.id.button_mfc_2);

        button_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab2SharedViewModel_sub1.setMfc_Code(button_cm.getText().toString());
                tab2SharedViewModel_sub1.setWindow_letter("W");
            }
        });

        button_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab2SharedViewModel_sub1.setMfc_Code(button_pl.getText().toString());
                tab2SharedViewModel_sub1.setWindow_letter("L");
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get the sharedViewModel registered
        tab2SharedViewModel_sub1 = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
        /*
        tab2SharedViewModel_sub1.getMfc_Code().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {

            }
        });
        */
    }

}
