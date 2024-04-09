package Domain;

public class Registration extends Entity<Integer>{
    private Race race;
    private Participant participant;

    public Registration(Race race, Participant participant) {
        this.race = race;
        this.participant = participant;
    }

    public Race getRace() {return race;}
    public void setRace(Race race) {this.race = race;}
    public Participant getParticipant() {return participant;}
    public void setParticipant(Participant participant) {this.participant = participant;}

    @Override
    public String toString() {
        return id +
                ". id_race=" + race.getId() +
                ", id_participant=" + participant.getId();
    }
}
