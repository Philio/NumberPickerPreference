package android.support.v7.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import me.philio.preferencecompatextended.R;


public class DatePickerPreference extends DialogPreference {

  public static final int DATE_FORMAT_INHERIT = 0;
  public static final int DATE_FORMAT_MONTH_DAY_YEAR = 1;
  public static final int DATE_FORMAT_DAY_MONTH_YEAR = 2;

  private static LocalDate EPOCH = new LocalDate(1970, 1, 1);
  private static DateTimeFormatter MONTH_DAY_YEAR_FORAMT = DateTimeFormat.forPattern("MMM d YYYY");
  private static DateTimeFormatter DAY_MONTH_YEAR_FORAMT = DateTimeFormat.forPattern("d MMM YYYY");

  private boolean dateAsSummary;
  private int dateFormat;
  private int defaultDayOfMonth;
  private int defaultMonthOfYear;
  private int defaultYear;
  private int defaultYearsAgo;
  private int value;

  public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatePickerPreference, defStyleAttr, defStyleRes);
    dateAsSummary = typedArray.getBoolean(R.styleable.DatePickerPreference_dateAsSummary, true);
    defaultYearsAgo = typedArray.getInt(R.styleable.DatePickerPreference_defaultYearsAgo, -1);
    defaultYear = typedArray.getInt(R.styleable.DatePickerPreference_defaultYear, -1);
    defaultMonthOfYear = typedArray.getInt(R.styleable.DatePickerPreference_defaultMonthOfYear, -1);
    defaultDayOfMonth = typedArray.getInt(R.styleable.DatePickerPreference_defaultDayOfMonth, -1);
    dateFormat = typedArray.getInt(R.styleable.DatePickerPreference_dateFormat, DATE_FORMAT_INHERIT);
    typedArray.recycle();
  }

  public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public DatePickerPreference(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.datePickerPreferenceStyle);
  }

  public DatePickerPreference(Context context) {
    this(context, null);
  }

  public static int calculateValue(LocalDate date) {
    return Days.daysBetween(EPOCH, date).getDays();
  }

  private LocalDate getDate() {
    return EPOCH.plusDays(value);
  }

  public int getYear() {
    return getDate().getYear();
  }

  public int getMonthOfYear() {
    return getDate().getMonthOfYear();
  }

  public int getDayOfMonth() {
    return getDate().getDayOfMonth();
  }

  public void setValue(LocalDate date) {
    setValue(calculateValue(date));
  }

  public void setValue(int value) {
    this.value = value;
    if (dateAsSummary) {
      switch (dateFormat) {
        case DATE_FORMAT_MONTH_DAY_YEAR:
          setSummary(MONTH_DAY_YEAR_FORAMT.print(getDate()));
          break;
        case DATE_FORMAT_DAY_MONTH_YEAR:
          setSummary(DAY_MONTH_YEAR_FORAMT.print(getDate()));
          break;
        case DATE_FORMAT_INHERIT:
        default:
          Date date = getDate().toDateTimeAtStartOfDay().toDate();
          setSummary(DateFormat.getDateFormat(getContext()).format(date));
          break;
      }
    }
    persistInt(value);
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    if (defaultYear > -1 && defaultYearsAgo > -1) {
      throw new IllegalArgumentException("Cannot specify both defaultYear and defaultYearsAgo");
    }
    int defaultValue = 0;
    if (defaultYear > -1 && defaultMonthOfYear > -1 && defaultDayOfMonth > -1) {
      LocalDate defaultDate = new LocalDate(defaultYear, defaultMonthOfYear + 1, defaultDayOfMonth);
      defaultValue = calculateValue(defaultDate);
    } else if (defaultYearsAgo > -1) {
      LocalDate defaultDate = LocalDate.now().minusYears(defaultYearsAgo);
      defaultValue = calculateValue(defaultDate);
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
    savedState.defaultYearsAgo = defaultYearsAgo;
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
    defaultYearsAgo = savedState.defaultYearsAgo;
    value = savedState.value;
  }

  private static class SavedState extends BaseSavedState {

    private boolean dateAsSummary;
    private int dateFormat;
    private int defaultDayOfMonth;
    private int defaultMonthOfYear;
    private int defaultYear;
    private int defaultYearsAgo;
    private int value;

    @SuppressLint("ParcelClassLoader")
    SavedState(Parcel source) {
      super(source);
      dateAsSummary = (boolean) source.readValue(null);
      dateFormat = source.readInt();
      defaultDayOfMonth = source.readInt();
      defaultMonthOfYear = source.readInt();
      defaultYear = source.readInt();
      defaultYearsAgo = source.readInt();
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
      dest.writeInt(defaultYearsAgo);
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
