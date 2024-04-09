package Repository.ParticipantRepository;

import Domain.Participant;
import Repository.IRepository;

public interface IParticipantRepository extends IRepository<Participant,Integer> {
    public Participant findParticipantByNameCapacityAndTeam(String name, Integer capacity, String team);
}
