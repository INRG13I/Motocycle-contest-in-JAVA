package ro.mpp2024.Domain;

public class Organizer implements IEntity{
    private Integer id;
    private String username;
    private String password;

    public Organizer(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
