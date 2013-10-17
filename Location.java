import java.util.*;

public class Location {
	private double latitude;
	private double longitude;
	
	public Location() {
		latitude = 0;
		longitude = 0;
	}
	
	public Location(double _latitude, double _longitude) {
		this.latitude = _latitude;
		this.longitude = _longitude;
	}
	
	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}
	
	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
}