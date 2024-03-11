package ro.mpp2024.Domain;

import ro.mpp2024.Repository.AbstractRepository;

public class Race implements IEntity{
    private Integer id;
    private Integer capacity;
    private AbstractRepository<Participant> participants;

    public Race(Integer id, Integer capacity, AbstractRepository<Participant> participants) {
        this.id = id;
        this.capacity = capacity;
        this.participants = participants;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public AbstractRepository<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(AbstractRepository<Participant> participants) {
        this.participants = participants;
    }
}
