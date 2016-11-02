package com.neozant.request;

public class TimerData {

	private int year;
	private int month;
	private int date;
	
	//12 hours
	private int hour;
	private int minutes;
	
	private String amPmMarker;
	private String repeatOn;
	
	
	public TimerData() {
		// TODO Auto-generated constructor stub
	}
	
	public TimerData(int year,int month,int date,int hour,int minutes,String amPmMarker) {
		
		this.year=year;
		this.month=month;
		this.date=date;
		this.hour=hour;
		this.minutes=minutes;
		this.amPmMarker=amPmMarker;
	}
	
	
	public int getYear() {
		return year;
	}



	public void setYear(int year) {
		this.year = year;
	}



	public int getMonth() {
		return month;
	}



	public void setMonth(int month) {
		this.month = month;
	}



	public int getDate() {
		return date;
	}



	public void setDate(int date) {
		this.date = date;
	}



	public int getHour() {
		return hour;
	}



	public void setHour(int hour) {
		this.hour = hour;
	}



	public int getMinutes() {
		return minutes;
	}



	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	
	
	public String getAmPmMarker() {
		return amPmMarker;
	}

	
	
	public void setAmPmMarker(String amPmMarker) {
		this.amPmMarker = amPmMarker;
	}

	public String getRepeatOn() {
		return repeatOn;
	}

	public void setRepeatOn(String repeatOn) {
		this.repeatOn = repeatOn;
	}


	public String toString(){
		String dateInString = this.getDate() + ":"
				+ this.getMonth() + ":" 
				+ this.getYear()  + "," 
				+ this.getHour() + ":"
				+ this.getMinutes() + ":00"
				+ ":"+this.getAmPmMarker();
		
		return dateInString;
	}
	

}
