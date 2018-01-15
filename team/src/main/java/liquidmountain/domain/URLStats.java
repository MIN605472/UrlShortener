package liquidmountain.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to give persistence url's stats
 */
public class URLStats {
    public List<DataStat> countries;
    public List<DataStat> browsers;
    public List<DataStat> platforms;
    public long numOfClickFromQr;

    /**
     * Constructor
     */
    public URLStats(){
        this.countries = new ArrayList<>();
        this.browsers = new ArrayList<>();
        this.platforms = new ArrayList<>();
    }

}
