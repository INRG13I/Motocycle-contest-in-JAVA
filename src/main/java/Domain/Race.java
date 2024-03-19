package Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Race implements Identifier<Integer>, Serializable {
    private Integer id;
    private Integer capacity;
    private ArrayList<Participant> participants;

    public Race(Integer id, Integer capacity, ArrayList<Participant> participants) {
        this.id = id;
        this.capacity = capacity;
        this.participants = participants;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id =id;
    }
}
