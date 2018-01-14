package android.support.v7.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.philio.preferencecompatextended.R;

import static android.text.format.DateFormat.getTimeFormat;

public class DatePickerPreference extends DialogPreference {

  public static final int DATE_FORMAT_INHERIT = 0;
  public static final int DATE_FORMAT_MONTH_DAY_YEAR = 1;
  public static final int DATE_FORMAT_DAY_MONTH_YEAR = 2;

  private boolean dateAsSummary;
  private int dateFormat;
  private int defaultDayOfMonth;
  private int defaultMonthOfYear;
  private int defaultYear;
  private int value;

  public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatePickerPreference, defStyleAttr, defStyleRes);
    dateAsSummary = typedArray.getBoolean(R.styleable.DatePickerPreference_dateAsSummary, true);
    defaultYear = typedArray.getInt(R.styleable.DatePickerPreference_defaultYear, -1);
    defaultMonthOfYear = typedArray.getInt(R.styleable.DatePickerPreference_defaultMonthOfYear, -1);
    defaultDayOfMonth = typedArray.getInt(R.styleable.DatePickerPreference_defaultDayOfMonth, -1);
    dateFormat = typedArray.getInt(R.styleable.DatePickerPreference_dateFormat, DATE_FORMAT_INHERIT);
  }

  public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public DatePickerPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DatePickerPreference(Context context) {
    super(context);
  }

  public static int calculateValue(int year, int monthOfYear, int dayOfMonth) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, monthOfYear, dayOfMonth);
    return (int) cal.getTimeInMillis() / 1000;
  }

  public int getYear() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date(value));
    return cal.get(Calendar.YEAR);
  }

  public int getMonthOfYear() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date(value));
    return cal.get(Calendar.MONTH);
  }

  public int getDayOfMonth() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date(value));
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public void setValue(int year, int monthOfYear, int dayOfMonth) {
    setValue(calculateValue(year, monthOfYear, dayOfMonth));
  }

  public void setValue(int value) {
    this.value = value;
    if (dateAsSummary) {
      DateFormat format;
      switch (dateFormat) {
        case DATE_FORMAT_MONTH_DAY_YEAR:
          format = new SimpleDateFormat("MMM d YYYY");
          break;
        case DATE_FORMAT_DAY_MONTH_YEAR:
          format = new SimpleDateFormat("d MMM YYYY");
          break;
        case DATE_FORMAT_INHERIT:
        default:
          format = getTimeFormat(getContext());
          break;
      }
      Date date = new Date(value * 1000);
      String time = format.format(date);
      setSummary(time);
    }
    persistInt(value);
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    int defaultValue = 0;
    if (defaultYear > -1 && defaultMonthOfYear > -1 && defaultDayOfMonth > -1) {
      Calendar cal = Calendar.getInstance();
      cal.set(defaultYear, defaultMonthOfYear, defaultDayOfMonth);
      defaultValue = (int) cal.getTimeInMillis() / 1000;
    }
    return a.getInt(index, defaultValue);
  }

  @Override
  protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    setValue(restorePersistedValue ? getPersistedInt(value) : (Integer) defaultValue);
  }

  @Override
  protected Parcelable onSaveInstanceState() {
    final Parcelable superState = super.onSaveInstanceState();
    if (isPersistent()) {
      return superState;
    }
    final SavedState savedState = new SavedState(superState);
    savedState.dateAsSummary = dateAsSummary;
    savedState.dateFormat = dateFormat;
    savedState.defaultDayOfMonth = defaultDayOfMonth;
    savedState.defaultMonthOfYear = defaultMonthOfYear;
    savedState.defaultYear = defaultYear;
    savedState.value = value;
    return savedState;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    if (state == null || !state.getClass().equals(SavedState.class)) {
      super.onRestoreInstanceState(state);
      return;
    }
    SavedState savedState = (SavedState) state;
    super.onRestoreInstanceState(savedState.getSuperState());
    dateAsSummary = savedState.dateAsSummary;
    dateFormat = savedState.dateFormat;
    defaultDayOfMonth = savedState.defaultDayOfMonth;
    defaultMonthOfYear = savedState.defaultMonthOfYear;
    defaultYear = savedState.defaultYear;
    value = savedState.value;
  }

  private static class SavedState extends BaseSavedState {

    private boolean dateAsSummary;
    private int dateFormat;
    private int defaultDayOfMonth;
    private int defaultMonthOfYear;
    private int defaultYear;
    private int value;

    @SuppressLint("ParcelClassLoader")
    SavedState(Parcel source) {
      super(source);
      dateAsSummary = (boolean) source.readValue(null);
      dateFormat = source.readInt();
      defaultDayOfMonth = source.readInt();
      defaultMonthOfYear = source.readInt();
      defaultYear = source.readInt();
      value = source.readInt();
    }

    SavedState(Parcelable superState) {
      super(superState);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
      dest.writeValue(dateAsSummary);
      dest.writeInt(dateFormat);
      dest.writeInt(defaultDayOfMonth);
      dest.writeInt(defaultMonthOfYear);
      dest.writeInt(defaultYear);
      dest.writeInt(value);
    }

    public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>() {
          @Override public SavedState createFromParcel(Parcel source) {
            return new SavedState(source);
          }

          @Override public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
  }
}
