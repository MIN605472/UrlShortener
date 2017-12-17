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
		this.expirationDate = expirationDate;
		this.expirationTime = expirationTime;
	}

	public ShortURL(String hash, String target, URI uri, String sponsor, Date created, String owner, Integer mode, Boolean safe, String ip, String country) {
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
		c.add(Calendar.DATE, 1);  // number of days to add
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

	public String getSponsor() {
		return sponsor;
	}

	public Boolean getSafe() {
		return safe;
	}

	public String getIP() {
		return ip;
	}

	public String getCountry() {
		return country;
	}

}
