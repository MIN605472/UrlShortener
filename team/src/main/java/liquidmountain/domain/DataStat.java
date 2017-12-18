package liquidmountain.domain;

public class DataStat {
    private String data;
    private int users;

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
