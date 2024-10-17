package clinic.cs213project3.model.util;
import java.util.Calendar;

/**
 * This is the object structure for an appointment's date.
 * Each date object stores the day, month, and year stored as integers.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class Date implements Comparable<Date> {

    private static final int MONTHS_FROM_TODAY = 6;
    private static final int FIRST_CALENDER_YEAR = 1900;
    private static final int NONLEAP_FEB_DAYS = 28;
    private static final int LEAP_FEB_DAYS = 29;
    private static final int THIRTY_MONTH_DAYS = 30;
    private static final int THIRTY_ONE_MONTH_DAYS = 31;
    private static final int JAN = 0;
    private static final int FEB = 1;
    private static final int MAR = 2;
    private static final int APR = 3;
    private static final int MAY = 4;
    private static final int JUNE = 5;
    private static final int JULY = 6;
    private static final int AUG = 7;
    private static final int SEP = 8;
    private static final int OCT = 9;
    private static final int NOV = 10;
    private static final int DEC = 11;
    private static final int LESS_THAN = -1;
    private static final int EQUAL = 0;
    private static final int GREATER_THAN = 1;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    private int year;
    private int month;
    private int day;

    /**
     * Constructor creates a new date object.
     * The year, day, and month are stored as integers.
     * @param year The date's year as an integer.
     * @param month The date's month ranging from 0 to 11.
     * @param day The date's day as an integer.
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Checks if a date's month contains exactly 31 days.
     * Only January, March, May, July, August, October, and December have 31 days.
     * @return True if the date's month has 31 days, else False.
     */
    private boolean isThirtyOneDayMonth() {
        return month == JAN || month == MAR ||
                month == MAY || month == JULY ||
                month == AUG || month == OCT ||
                month == DEC;
    }

    /**
     * Checks if a date's month contains exactly 30 days.
     * Only April, June, September, and November have 30 days.
     * @return True if date's month has 30 days, else False.
     */
    private boolean isThirtyDayMonth() {
        return month == APR || month == JUNE ||
                month == SEP || month == NOV;
    }

    /**
     * Checks if a date's year is a leap year.
     * @return True if date's year is a leap year, else False.
     */
    private boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUATERCENTENNIAL == 0;
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a date is a valid date on the calendar.
     * @return True if date exists on the calendar, else False.
     */
    public boolean isValid() {
        if (FIRST_CALENDER_YEAR <= year) {
            int lastDayOfMonth;
            if (month == FEB) {
                if (isLeapYear()) {
                    lastDayOfMonth = LEAP_FEB_DAYS;
                }
                else {
                    lastDayOfMonth = NONLEAP_FEB_DAYS;
                }
            }
            else if (isThirtyOneDayMonth()) {
                lastDayOfMonth = THIRTY_ONE_MONTH_DAYS;
            }
            else if (isThirtyDayMonth()) {
                lastDayOfMonth = THIRTY_MONTH_DAYS;
            }
            else {
                return false;
            }
            return (0 < day && day <= lastDayOfMonth);
        }
        return false;
    }

    /**
     * Checks if two date objects are the equivalent.
     * Dates match if they have the same year, month, and day.
     * @param obj The object being compared.
     * @return True if dates are equal, else False.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return year == date.year &&
                    month == date.month &&
                    day == date.day;
        }
        return false;
    }

    /**
     * Defines the ordering of two date objects.
     * @param date The date that is being compared against.
     * @return -1 if object is before date, 1 if after, and 0 if equal.
     */
    @Override
    public int compareTo(Date date) {
        if (year < date.year) {
            return LESS_THAN;
        }
        else if (year > date.year) {
            return GREATER_THAN;
        }
        else if (month < date.month) {
            return LESS_THAN;
        }
        else if (month > date.month) {
            return GREATER_THAN;
        }
        else if (day < date.day) {
            return LESS_THAN;
        }
        else if (day > date.day) {
            return GREATER_THAN;
        }
        else {
            return EQUAL;
        }
    }

    /**
     * Converts the date information into a string format.
     * String format sequence is the month, day, and year in that order.
     * @return The string representation of a date object.
     */
    @Override
    public String toString() {
        int actualMonth = month + 1; // Month integers range 0-11...
        return actualMonth + "/" + day + "/" + year;
    }

    /**
     * Checks if a given date is today or before today.
     * @return True if date is today or before, else False.
     */
    public boolean isTodayOrBefore() {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        return !date.after(today);
    }

    /**
     * Checks if a given date is today or after today.
     * @return True if date is today or after, else False.
     */
    public boolean isTodayOrAfter() {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        return !date.before(today);
    }

    /**
     * Checks if a date falls on the weekend.
     * @return True if date is on the weekend, else False.
     */
    public boolean isWeekend() {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        int dayOfTheWeek = date.get(Calendar.DAY_OF_WEEK);
        return (dayOfTheWeek == Calendar.SATURDAY ||
                dayOfTheWeek == Calendar.SUNDAY);
    }

    /**
     * Checks if a date is within 6 months from today.
     * @return True if date is within 6 months, else False.
     */
    public boolean isWithinSixMonths() {
        Calendar today = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        // Gets the date six months from today...
        Calendar sixFromToday = Calendar.getInstance();
        sixFromToday.add(Calendar.MONTH, MONTHS_FROM_TODAY);
        // Checks if date is between today and sixFromToday...
        return date.after(today) && !date.after(sixFromToday);
    }

}
