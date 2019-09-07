package com.example.cmgandroidappdemo.tab2.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;

public class Tab2_Sub_State_Code extends Fragment{

    private SharedViewModelUnderTab2 tab2SharedViewModel_sub2;
    private Spinner spinner_StateCode;
    private ArrayAdapter<CharSequence> stateCodeOptionsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab2_sub_state_code, container,false);

        spinner_StateCode = rootView.findViewById(R.id.spinner_StateCode);
        //stateCodeOptionsAdapter =  new ArrayAdapter<CharSequence>(getContext(), R.layout.support_simple_spinner_dropdown_item,);
        stateCodeOptionsAdapter = ArrayAdapter.createFromResource(getContext(),R.array.state_code,android.R.layout.simple_spinner_item);
        stateCodeOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_StateCode.setAdapter(stateCodeOptionsAdapter);
        spinner_StateCode.setSelection(0);

        spinner_StateCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterViewParent, View view, int position, long l) {
                if (!adapterViewParent.getItemAtPosition(position).equals(getResources().getStringArray(R.array.state_code)[0])){
                    String str_selectedItem = adapterViewParent.getItemAtPosition(position).toString();
                    String str_items[] = str_selectedItem.split("-");
                    String str_stateCode = str_items[1];
                    tab2SharedViewModel_sub2.setState_Code(str_stateCode);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tab2SharedViewModel_sub2 = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
    }
}
