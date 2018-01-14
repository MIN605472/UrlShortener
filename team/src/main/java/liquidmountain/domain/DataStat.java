package liquidmountain.domain;


/**
 * Class to give persistence url's data
 */
public class DataStat {
    private String data;
    private int users;

    /**
     * Constructor
     * @param data: String
     * @param users: Integer usuarios que han usado la url
     */
    public DataStat(String data, int users) {
        this.data = data;
        this.users = users;
    }

    public String getData() {

        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }
}
