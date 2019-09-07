package com.example.cmgandroidappdemo.tab2.sub;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmgandroidappdemo.R;
import com.example.cmgandroidappdemo.ui.main.SharedViewModelUnderTab2;

import java.util.ArrayList;

public class Tab2_Sub_Jackpot_Settings extends Fragment {
    private SharedViewModelUnderTab2 tab2SharedViewModel_sub5;
    private Spinner spinner_window_qty;
    private ArrayAdapter<CharSequence> spinner_window_qty_adapter;
    private Button button_ok, button_reset;
    private ArrayList<String> window_effect_code_set_child = new ArrayList<>();
    private ArrayList<String> jp_value_set_child = new ArrayList<>();
    private final int spinner_window_effect_code_id = 10000, editText_jp_value_id = 20000;
    private int window_qty = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView =  inflater.inflate(R.layout.tab2_sub_jackpot_settings, container,false);
        spinner_window_qty = rootView.findViewById(R.id.spinner_WindowQty);
        spinner_window_qty_adapter = ArrayAdapter.createFromResource(getContext(), R.array.jackpot_window_qty,android.R.layout.simple_spinner_item);
        spinner_window_qty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_window_qty.setAdapter(spinner_window_qty_adapter);
        spinner_window_qty.setSelection(0);
        window_qty = Integer.parseInt(spinner_window_qty.getSelectedItem().toString());

        spinner_window_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                window_qty = Integer.parseInt(spinner_window_qty.getSelectedItem().toString());
                LinearLayout ll_row_parent = rootView.findViewById(R.id.jp_setting_row_parent);
                ll_row_parent.removeAllViewsInLayout();
                window_effect_code_set_child.clear();
                jp_value_set_child.clear();


                for (int i = 0; i < pos+1; i++){
                    LinearLayout ll_row = new LinearLayout(getContext());
                    LinearLayout.LayoutParams ll_row_params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll_row.setGravity(Gravity.CENTER|Gravity.TOP);
                    ll_row.setOrientation(LinearLayout.HORIZONTAL);
                    ll_row.setLayoutParams(ll_row_params);

                    TextView textView_window = new TextView(getContext());
                    textView_window.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

                    textView_window.setText("Window" + Integer.toString((i+1)) + " Effect Code: ");
                    textView_window.setTextColor(getResources().getColor(R.color.colorBlack, getActivity().getTheme()));
                    textView_window.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    textView_window.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);


                    Spinner spinner_window_effect_cdoe = new Spinner(getContext());
                    spinner_window_effect_cdoe.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    ArrayAdapter<CharSequence> spinner_window_effect_cdoe_adapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.window_effect_code,
                                    android.R.layout.simple_spinner_item);
                    spinner_window_effect_cdoe_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_window_effect_cdoe.setAdapter(spinner_window_effect_cdoe_adapter);
                    spinner_window_effect_cdoe.setId(spinner_window_effect_code_id+i);


                    String temp_str1[] = spinner_window_effect_cdoe.getSelectedItem().toString().split("-");
                    window_effect_code_set_child.add(temp_str1[0]);

                    spinner_window_effect_cdoe.setSelection(0);

                    spinner_window_effect_cdoe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int spinnerSelectionPosition, long l) {
                            String window_effect_code_single_selection[] = spinner_window_effect_cdoe.getSelectedItem().toString().split("-");
                            int index = spinner_window_effect_cdoe.getId()-spinner_window_effect_code_id;
                            window_effect_code_set_child.set(index,window_effect_code_single_selection[0]);
                            //tab2SharedViewModel_sub5.setWindow_effect_set(window_effect_code_set_child);
                            //Toast.makeText(getContext(),window_effect_code_set_child.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    TextView textView_jp_value = new TextView(getContext());
                    textView_jp_value.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    textView_jp_value.setText("Window" + Integer.toString((i+1)) + " Jackpot Value: ");
                    textView_jp_value.setTextColor(getResources().getColor(R.color.colorBlack, getActivity().getTheme()));
                    textView_jp_value.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    textView_jp_value.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);


                    EditText editText_jp_value = new EditText(getContext());
                    editText_jp_value.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    editText_jp_value.setText("88.8");
                    editText_jp_value.setId(editText_jp_value_id + i);
                    //editText_jp_value.generateViewId()
                    editText_jp_value.setTextColor(getResources().getColor(R.color.colorBlack, getActivity().getTheme()));
                    editText_jp_value.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    editText_jp_value.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    InputFilter[] editText_jp_value_input_filter = new InputFilter[1];
                    editText_jp_value_input_filter[0] = new InputFilter.LengthFilter(4);
                    editText_jp_value.setFilters(editText_jp_value_input_filter);
                    jp_value_set_child.add(editText_jp_value.getText().toString());
                    editText_jp_value.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            int index = editText_jp_value.getId()-editText_jp_value_id;
                            jp_value_set_child.set(index, editText_jp_value.getText().toString());
                            Toast.makeText(getContext(),jp_value_set_child.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //editText_jp_value.setGravity(Gravity.CENTER|Gravity.TOP);

                    ll_row.addView(textView_window);
                    ll_row.addView(spinner_window_effect_cdoe);
                    ll_row.addView(textView_jp_value);
                    ll_row.addView(editText_jp_value);

                    ll_row_parent.addView(ll_row);
                }

                //tab2SharedViewModel_sub5.setWindow_effect_set(window_effect_code_set_child);


                //Toast.makeText(getContext(), "windowEffectSet Size: " + window_effect_code_set_child.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        button_ok = rootView.findViewById(R.id.button_jp_setting_ok);
        button_reset = rootView.findViewById(R.id.button_jp_setting_reset);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //int window_qty = Integer.getInteger(spinner_window_qty.getSelectedItem().toString());
                tab2SharedViewModel_sub5.setWindow_effect_set(window_effect_code_set_child);
                tab2SharedViewModel_sub5.setJp_value_set(jp_value_set_child);
                tab2SharedViewModel_sub5.setWindow_qty(spinner_window_qty.getSelectedItem().toString());
                //spinner_window_qty.setSelection(window_qty_pos);

                Toast.makeText(getContext(),"size: " + Integer.toString(jp_value_set_child.size()) +
                        jp_value_set_child.toString(),Toast.LENGTH_SHORT).show();
                //window_effect_code_set_child.clear();

            }
        });
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_window_qty.setSelection(0);
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tab2SharedViewModel_sub5 = ViewModelProviders.of(getActivity()).get(SharedViewModelUnderTab2.class);
    }
}
