package liquidmountain.domain;

import java.sql.Date;


/**
 * Class to give persistence url's clicks
 */
public class Click {

	private Long id;
	private String hash;
	private Date created;
	private String referrer;
	private String browser;
	private String platform;
	private String ip;
	private String country;

	/**
	 * Constructor
	 * @param id: Long identificador del Click
	 * @param hash: String identificador hash del click
	 * @param created: Date fecha de creacion del click
	 * @param referrer: String
	 * @param browser: String navegador donde se genero el click
	 * @param platform: String plataforma donde se genero el click
	 * @param ip: String direccion ipi donde se genero el click
	 * @param country: String ciudad donde se genero el click
	 */
	public Click(Long id, String hash, Date created, String referrer,
			String browser, String platform, String ip, String country) {
		this.id = id;
		this.hash = hash;
		this.created = created;
		this.referrer = referrer;
		this.browser = browser;
		this.platform = platform;
		this.ip = ip;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public String getHash() {
		return hash;
	}

	public Date getCreated() {
		return created;
	}

	public String getReferrer() {
		return referrer;
	}

	public String getBrowser() {
		return browser;
	}

	public String getPlatform() {
		return platform;
	}

	public String getIp() {
		return ip;
	}

	public String getCountry() {
		return country;
	}
}
