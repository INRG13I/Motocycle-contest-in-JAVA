package Domain;

import java.io.Serializable;

public class Participant implements Identifier<Integer>, Serializable {
    private Integer id;
    private String name;
    private Integer capacity;
    private String team;

    public Participant(Integer id, String name, Integer capacity, String team) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.team = team;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id= id;
    }
}
