package core.dto;

import java.sql.Time;

public class AttendanceDto {
	private int id;
	private String nik;
	private String attendanceDate;
	private Time checkin;
	private Time chekout;
	private String longitude;
	private String latitude;
	private String name;
	private String email;
	private String total;
	private String activity;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNik() {
		return nik;
	}
	
	public void setNik(String nik) {
		this.nik = nik;
	}
	
	public String getAttendanceDate() {
		return attendanceDate;
	}
	
	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	
	public Time getCheckin() {
		return checkin;
	}
	
	public void setCheckin(Time checkin) {
		this.checkin = checkin;
	}
	
	public Time getChekout() {
		return chekout;
	}
	
	public void setChekout(Time chekout) {
		this.chekout = chekout;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	
}
