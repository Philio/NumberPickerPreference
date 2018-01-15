package android.support.v7.preference;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

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
    LocalDate date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
    if (preference.callChangeListener(DatePickerPreference.calculateValue(date))) {
      preference.setValue(date);
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
    return new DatePickerDialog(getActivity(), this, preference.getYear(), preference.getMonthOfYear() - 1, preference.getDayOfMonth());
  }
}
