package Domain;

import java.io.Serializable;

public class Organizer implements Identifier<Integer>, Serializable {
    private int id;
    private String username;
    private String password;

    public Organizer(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
