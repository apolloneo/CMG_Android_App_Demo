package com.example.cmgandroidappdemo.tab2.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;

public class Tab2_Sub_Sign_ID extends Fragment {
    private SharedViewModelUnderTab2 tab2SharedViewModel_sub3;
    private EditText editText_SignID;
    private Button button_Submit_SignID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab2_sub_sign_id, container,false);
        editText_SignID = rootView.findViewById(R.id.editText_Sign_ID);
        button_Submit_SignID = rootView.findViewById(R.id.button_SubmitSignID);

        button_Submit_SignID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_sign_id = editText_SignID.getText().toString();
                int id;
                try {
                    id = Integer.parseInt(str_sign_id, 10);
                    if (id >= 0){
                        tab2SharedViewModel_sub3.setSign_ID(str_sign_id);
                        editText_SignID.setText("");
                    }else {
                        Toast.makeText(getContext(),"Please input positive numbers!", Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(getContext(), "Please input valid numbers!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get the sharedViewModel registered
        tab2SharedViewModel_sub3 = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
        tab2SharedViewModel_sub3.getSign_ID().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                editText_SignID.setText(charSequence);
            }
        });
    }
}
