package android.support.v7.preference;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

/**
 * Created by parkeroth on 1/14/18.
 */

public class DatePickerPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements DatePickerDialog.OnDateSetListener {

  public static DatePickerPreferenceDialogFragmentCompat newInstance(String key) {
    DatePickerPreferenceDialogFragmentCompat fragment = new DatePickerPreferenceDialogFragmentCompat();
    Bundle args = new Bundle(1);
    args.putString(ARG_KEY, key);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
    DatePickerPreference preference = (DatePickerPreference) getPreference();
    if (preference.callChangeListener(DatePickerPreference.calculateValue(year, monthOfYear, dayOfMonth))) {
      preference.setValue(year, monthOfYear, dayOfMonth);
    }
  }

  @Override
  public void onDialogClosed(boolean positiveResult) {
    // Not needed, as handled by onTimeSet
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    DatePickerPreference preference = (DatePickerPreference) getPreference();
    return new DatePickerDialog(getActivity(), this, preference.getYear(), preference.getMonthOfYear(), preference.getDayOfMonth());
  }
}
