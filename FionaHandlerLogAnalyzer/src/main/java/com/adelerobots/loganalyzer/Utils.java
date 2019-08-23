package com.adelerobots.loganalyzer;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

public class Utils{

	static class Constantes {
		public static final Color MAGENTA = new Color(229, 51, 127);
		public static final Color GREEN = new Color(175, 199, 0);
		public static final String CONNECTIONS = "Usuarios atendidos";
		public static final String REJECTIONS = "Conexiones potenciales";
	}
	
	public static TimeSeries fillInMissingPeriods(TimeSeries timeSeries, Date startDate, Date endDate, Class<?> period) throws Exception {
		Date dateIndex = startDate;
			if (Day.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfDay(dateIndex);
					if (timeSeries.getDataItem(new Day(dateIndex)) == null) {
						timeSeries.add(new Day(dateIndex), 0.0d);
						//System.out.println("Day added: " + dateIndex);
					}
					else {
						//System.out.println("Day getDataItem: " + timeSeries.getDataItem(new Day(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addDays(dateIndex, 1);
					//System.out.println("Day: " + dateIndex);
				}
			} else if (Hour.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfHour(dateIndex);
					if (timeSeries.getDataItem(new Hour(dateIndex)) == null) {
						timeSeries.add(new Hour(dateIndex), 0.0d);
						//System.out.println("Hour added: " + dateIndex);
					}
					else {
						//System.out.println("Hour getDataItem: " + timeSeries.getDataItem(new Hour(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addHours(dateIndex, 1);
					//System.out.println("Hour: " + dateIndex);
				}
			} else if (Minute.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfMinute(dateIndex);
					if (timeSeries.getDataItem(new Minute(dateIndex)) == null) {
						timeSeries.add(new Minute(dateIndex), 0.0d);
						//System.out.println("Minute added: " + dateIndex);
					}
					else {
						//System.out.println("Minute getDataItem: " + timeSeries.getDataItem(new Minute(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addMinutes(dateIndex, 1);
					//System.out.println("Minute: " + dateIndex);
				}
			} else if (Second.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfSecond(dateIndex);
					if (timeSeries.getDataItem(new Second(dateIndex)) == null) {
						timeSeries.add(new Second(dateIndex), 0.0d);
						//System.out.println("Second added: " + dateIndex);
					}
					else {
						//System.out.println("Second getDataItem: " + timeSeries.getDataItem(new Second(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addSeconds(dateIndex, 1);
					//System.out.println("Second: " + dateIndex);
				}
			} else if (Week.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfWeek(dateIndex);
					if (timeSeries.getDataItem(new Week(dateIndex)) == null) {
						timeSeries.add(new Week(dateIndex), 0.0d);
						//System.out.println("Week added: " + dateIndex);
					}
					else {
						//System.out.println("Week getDataItem: " + timeSeries.getDataItem(new Week(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addDays(dateIndex, 7);
					//System.out.println("Week: " + dateIndex);
				}
			} else if (Month.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfMonth(dateIndex);
					if (timeSeries.getDataItem(new Month(dateIndex)) == null) {
						timeSeries.add(new Month(dateIndex), 0.0d);
						//System.out.println("Month added: " + dateIndex);
					}
					else {
						//System.out.println("Month getDataItem: " + timeSeries.getDataItem(new Month(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addMonths(dateIndex, 1);
					//System.out.println("Month: " + dateIndex);
				}
			} else if (Year.class.equals(period)) {
				while (dateIndex.before(endDate)) {
					dateIndex = startOfYear(dateIndex);
					if (timeSeries.getDataItem(new Year(dateIndex)) == null) {
						timeSeries.add(new Year(dateIndex), 0.0d);
						//System.out.println("Year added: " + dateIndex);
					}
					else {
						//System.out.println("Year getDataItem: " + timeSeries.getDataItem(new Year(dateIndex)).getValue());
					}
					dateIndex = DateUtils.addYears(dateIndex, 1);
					//System.out.println("Year: " + dateIndex);
				}
			} else {
				// un-expected time period
				throw new Exception("The supplied time period " + period.getName() + " is unsupported, use Second, Minute, Hour, Day, Week, Month, and Year.");
			}
		return timeSeries;
	}
	
	public static Date startOfYear(Date date){
		date = DateUtils.setMonths(date, 1);
		date = startOfMonth(date);
		return date;
	}
	
	public static Date startOfMonth(Date date){
		date = DateUtils.setDays(date, 1);
		date = startOfDay(date);
		return date;
	}
	
	//TODO : ponerlo a lunes
	public static Date startOfWeek(Date date){
		//date = DateUtils.setDays(date, 1);
		date = startOfDay(date);
		return date;
	}
	
	public static Date startOfDay(Date date){
		date = DateUtils.setHours(date, 0);
		date = startOfHour(date);
		return date;
	}
	
	public static Date startOfHour(Date date){
		date = DateUtils.setMinutes(date, 0);
		date = startOfMinute(date);
		return date;
	}
	
	public static Date startOfMinute(Date date){
		date = DateUtils.setSeconds(date, 0);
		date = startOfSecond(date);
		return date;
	}
	
	public static Date startOfSecond(Date date){
		date = DateUtils.setMilliseconds(date, 0);
		return date;
	}

	public static LegendItemCollection createAccesosLegendItems() {
		LegendItemCollection result = new LegendItemCollection();
		LegendItem item1 = new LegendItem("Attended users", "Attended users", "Attended users", "Attended users",
				new Rectangle(9, 9), Constantes.GREEN);
	    result.add(item1);
	   	return result;
	}

	public static LegendItemCollection createRechazosLegendItems() {
		LegendItemCollection result = new LegendItemCollection();
		LegendItem item1 = new LegendItem("Potential connections", "Potential connections", "Potential connections", "Potential connections",
				new Rectangle(9, 9), Constantes.MAGENTA);
	    result.add(item1);
	   	return result;
	}

	public static LegendItemCollection createConcurrentLegendItems() {
		LegendItemCollection result = new LegendItemCollection();
		LegendItem item1 = new LegendItem("Attended users", "Attended users", "Attended users", "Attended users",
				new Rectangle(9, 9), Constantes.GREEN);
		LegendItem item2 = new LegendItem("Demand (*)", "Demand (*)", "Demand (*)", "Demand (*)",
				new Rectangle(9, 9), Constantes.MAGENTA);
		LegendItem item3 = new LegendItem("Contracted concurrency", "Contracted concurrency", "Contracted concurrency", "Contracted concurrency",
				new Rectangle(9, 1), Color.BLACK);
	    result.add(item1);
      	result.add(item2);
      	result.add(item3);
	   	return result;
	}
	
	public static List<TimeSeries> orderSeries(List<TimeSeries> series) {
		List<TimeSeries> new_list = new ArrayList<TimeSeries>();
		Iterator<TimeSeries> it = series.iterator();
		if(it.hasNext()) {
			TimeSeries serie = it.next();
			if(it.hasNext()) {
				if(serie.getKey().equals(Constantes.CONNECTIONS))
					return series;
				new_list.add(it.next());
				new_list.add(serie);
				return new_list;
			}
		}
		return series;
	}
}