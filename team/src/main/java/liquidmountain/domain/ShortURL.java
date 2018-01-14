package liquidmountain.domain;

import java.net.URI;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShortURL {

	private String hash;
	private String target;
	private URI uri;
	private String sponsor;
	private Date created;
	private String owner;
	private Integer mode;
	private Boolean safe;
	private String ip;
	private String country;
	private Date expirationDate;
	private Time expirationTime;

	public ShortURL(String hash, String target, URI uri, String sponsor,
			Date created, String owner, Integer mode, Boolean safe, String ip,
			String country, Date expirationDate, Time expirationTime) {
		this.hash = hash;
		this.target = target;
		this.uri = uri;
		this.sponsor = sponsor;
		this.created = created;
		this.owner = owner;
		this.mode = mode;
		this.safe = safe;
		this.ip = ip;
		this.country = country;
		if(expirationDate == null) { //if it's null, means it's forever
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(System.currentTimeMillis()));
			c.add(Calendar.YEAR, 999);  // add 999 years
			java.util.Date d = c.getTime();
			this.expirationDate = new Date(d.getTime());
		} else this.expirationDate = expirationDate;
		if(expirationTime == null) { // same as above
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(System.currentTimeMillis()));
			c.set(Calendar.HOUR_OF_DAY, 0);  // set as 00:00
			c.set(Calendar.MINUTE, 0); // set as 00:00
			c.set(Calendar.SECOND, 0); // jeez...
			java.util.Date d = c.getTime();
			this.expirationTime = new Time(d.getTime());
		} else this.expirationTime = expirationTime;
	}

	public ShortURL(String hash, String target, URI uri, String sponsor, Date created, String owner, Integer mode,
					Boolean safe, String ip, String country) {
		this.hash = hash;
		this.target = target;
		this.uri = uri;
		this.sponsor = sponsor;
		this.created = created;
		this.owner = owner;
		this.mode = mode;
		this.safe = safe;
		this.ip = ip;
		this.country = country;;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		c.add(Calendar.YEAR, 999);  // number of days to add
		java.util.Date d = c.getTime();
		this.expirationDate = new Date(d.getTime());
		this.expirationTime = new Time(System.currentTimeMillis());
	}

	public ShortURL() {
	}

	public String getHash() {
		return hash;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public Time getExpirationTime() {
		return expirationTime;
	}

	public String getTarget() {
		return target;
	}

	public URI getUri() {
		return uri;
	}

	public Date getCreated() {
		return created;
	}

	public String getOwner() {
		return owner;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getSponsor() {
		return sponsor;
	}

	public Boolean getSafe() {
		return safe;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	public String getIP() {
		return ip;
	}

	public String getCountry() {
		return country;
	}

}
