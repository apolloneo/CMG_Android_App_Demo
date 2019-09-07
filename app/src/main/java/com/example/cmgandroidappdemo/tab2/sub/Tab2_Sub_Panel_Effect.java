package com.example.cmgandroidappdemo.tab2.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;

public class Tab2_Sub_Panel_Effect extends Fragment {
    private SharedViewModelUnderTab2 tab2SharedViewModel_sub4;
    private Spinner spinner_pe_1, spinner_pe_2;
    private Button button_PE_submit;
    private ArrayAdapter<CharSequence> spinner_pe1_adapter, spinner_pe2_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView =  inflater.inflate(R.layout.tab2_sub_pannel_effect_code, container,false);

        button_PE_submit = rootView.findViewById(R.id.button_PE_Submit);

        spinner_pe_1 = rootView.findViewById(R.id.spinner_PE_1);
        spinner_pe1_adapter = ArrayAdapter.createFromResource(getContext(),R.array.panel_effect_code_1,android.R.layout.simple_spinner_item);
        spinner_pe1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pe_1.setAdapter(spinner_pe1_adapter);
        spinner_pe_1.setSelection(0);

        spinner_pe_2 = rootView.findViewById(R.id.spinner_PE_2);
        spinner_pe2_adapter = ArrayAdapter.createFromResource(getContext(),R.array.panel_effect_code_2,android.R.layout.simple_spinner_item);
        spinner_pe2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pe_2.setAdapter(spinner_pe2_adapter);
        spinner_pe_2.setSelection(0);

        button_PE_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_pe_1.getSelectedItemPosition() == 0 && spinner_pe_2.getSelectedItemPosition() == 0){
                    tab2SharedViewModel_sub4.setPanel_Effect("");
                }else{
                    String pe_code, pe1_code, pe2_code;
                    String pe1[] = spinner_pe_1.getSelectedItem().toString().split("-");
                    String pe2[] = spinner_pe_2.getSelectedItem().toString().split("-");
                    pe1_code = pe1[0];
                    pe2_code = pe2[0];
                    pe_code = pe1_code + pe2_code;
                    tab2SharedViewModel_sub4.setPanel_Effect(pe_code);

                }
            }
        });



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tab2SharedViewModel_sub4 = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
    }
}
