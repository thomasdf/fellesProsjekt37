package utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import models.Activity;

public class Utilities {

	
	public final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	public final List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");

	public String getFormattedActivity(Activity act, boolean from) {
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
		return ret_str;
	}
	
	public int getFirstDayInMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public String getMonth(int i){
		return months.get(i);
	}
	
}
