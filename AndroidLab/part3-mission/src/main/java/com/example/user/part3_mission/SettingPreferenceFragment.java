package com.example.user.part3_mission;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class SettingPreferenceFragment extends PreferenceFragment {

    SwitchPreference roamingPreference;
    ListPreference networkTypePreference;
    SwitchPreference lteModePreference;

    SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);

        roamingPreference=(SwitchPreference)findPreference("roaming");
        networkTypePreference=(ListPreference)findPreference("network_type");
        lteModePreference=(SwitchPreference)findPreference("lte_mode");

        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        if(!prefs.getString("network_type","").equals("")){
            networkTypePreference.setSummary(prefs.getString("network_type", "LTE(권장)"));
        }
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("network_type")){
                networkTypePreference.setSummary(prefs.getString("network_type", "LTE(권장)"));
            }
        }
    };
}

