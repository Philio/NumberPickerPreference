package me.philio.preferencecompatextended;

import android.support.v4.app.DialogFragment;
import android.support.v7.preference.DatePickerPreference;
import android.support.v7.preference.DatePickerPreferenceDialogFragmentCompat;
import android.support.v7.preference.NumberPickerPreference;
import android.support.v7.preference.NumberPickerPreferenceDialogFragmentCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.TimePickerPreference;
import android.support.v7.preference.TimePickerPreferenceDialogFragmentCompat;

public abstract class PreferenceFragmentCompat extends android.support.v7.preference.PreferenceFragmentCompat {

    private static final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";

    @Override public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof NumberPickerPreference) {
            // Inherit the same behaviour as parent
            if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }
            final DialogFragment fragment = NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else if (preference instanceof TimePickerPreference) {
            // Inherit the same behaviour as parent
            if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }
            final DialogFragment fragment = TimePickerPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else if (preference instanceof DatePickerPreference) {
            // Inherit the same behaviour as parent
            if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                return;
            }
            final DialogFragment fragment = DatePickerPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

}
