package Domain;


public class Race extends Entity<Integer>{
    private Integer capacity;

    public Race(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return  id +
                ". " + capacity;
    }
}
