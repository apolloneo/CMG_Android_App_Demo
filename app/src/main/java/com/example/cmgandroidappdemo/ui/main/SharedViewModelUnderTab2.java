package com.example.cmgandroidappdemo.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class SharedViewModelUnderTab2 extends ViewModel {
    private final MutableLiveData<CharSequence> mfc_code = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> state_code = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> sign_ID = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> pennel_effect = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> window_letter = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> window_qty = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> window_effect_set = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> jp_value_set = new MutableLiveData<>();


    public void setMfc_Code(CharSequence input_mfc_code){
        mfc_code.setValue(input_mfc_code);
    }
    public LiveData<CharSequence> getMfc_Code() {
        return mfc_code;
    }

    public void setWindow_letter(CharSequence input_window_letter) {
        window_letter.setValue(input_window_letter);
    }
    public LiveData<CharSequence> getWindow_letter(){
        return  window_letter;
    }

    public void setState_Code(CharSequence input_state_code){
        state_code.setValue(input_state_code);
    }
    public LiveData<CharSequence> getState_Code(){
        return state_code;
    }

    public void setSign_ID(CharSequence input_sign_id) {
        sign_ID.setValue(input_sign_id);
    }
    public LiveData<CharSequence> getSign_ID() {
        return sign_ID;
    }

    public void setPanel_Effect(CharSequence input_panel_effect) {
        pennel_effect.setValue(input_panel_effect);
    }
    public LiveData<CharSequence> getPanel_Effect() {
        return pennel_effect;
    }

    public void setWindow_qty(CharSequence input_window_qty) {
        window_qty.setValue(input_window_qty);
    }
    public LiveData<CharSequence> getWindow_qty(){
        return window_qty;
    }

    public void setWindow_effect_set(ArrayList<String> input_window_effect_set){
        window_effect_set.setValue(input_window_effect_set);
    }
    public LiveData<ArrayList<String>> getWindow_effect_set() {
        return window_effect_set;
    }

    public void setJp_value_set(ArrayList<String> input_jp_value_set){
        jp_value_set.setValue(input_jp_value_set);
    }
    public LiveData<ArrayList<String>> getJp_value_set(){
        return jp_value_set;
    }
}
