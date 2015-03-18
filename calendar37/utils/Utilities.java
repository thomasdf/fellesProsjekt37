package utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import models.Activity;

/**
 * This class contains several important utility-functions and attributes that
 * other classes depend on using. It's purpose is to avoid duplicate code.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Utilities {

	/**
	 * A list of all the months in one year, <em>in Norwegian</em>.
	 */
	public final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	/**
	 * A list of all the days in one week, <em>in Norwegian</em>.
	 */
	public final List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");
	
	
	/**
	 * Formats and {@link Activity} to so that we get the time, if it has a time,
	 * and we get its description. All well formatted as a string with a max length.
	 * Also pays attention to whether or not the {@link Activity} is the first instance at the
	 * start-date, or the last instance at the end-date, which should have different formatting.
	 * 
	 * @param act
	 * @param from
	 * @param max_size
	 * @return the formatted String representing the {@link Activity}
	 */
	public String getFormattedActivity(Activity act, boolean from, int max_size) {
		String ret_str = "";
		if (from) {
			if (act.getFrom() != null) {
				ret_str += act.getFrom() + ": ";
			}
		} else {
			if (act.getTo() != null) {
				ret_str += act.getTo() + ": ";
			}
		}
		if (act.getDescription() != null) {
			ret_str += act.getDescription();
		}
		if (ret_str.length() > max_size) {
			ret_str = ret_str.substring(0, max_size);
			ret_str += "..";
		}
		return ret_str;
	}
	
	/**
	 * Gives back the day of the week for the first day in a specified month in a specified year.
	 * 
	 * @param year
	 * @param month
	 * @return an int representing what day of the week the first day in a month is
	 */
	public int getFirstDayInMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
