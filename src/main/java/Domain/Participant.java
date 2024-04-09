package Domain;

public class Participant extends Entity<Integer>{

    private String name;
    private Integer capacity;
    private String team;

    public Participant(String name, Integer capacity, String team) {
        this.name = name;
        this.capacity = capacity;
        this.team = team;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getCapacity() {return capacity;}
    public void setCapacity(Integer capacity) {this.capacity = capacity;}
    public String getTeam() {return team;}
    public void setTeam(String team) {this.team = team;}

    @Override
    public String toString() {
        return id +
                ". " + name +
                "- " + capacity +
                "- " + team ;
    }
}
