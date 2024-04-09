package Repository.OrganizerRepository;

import Domain.Organizer;
import Repository.IRepository;

public interface IOrganiserRepository extends IRepository<Organizer, Integer> {
    public Organizer findOrganizerByUsernameAndPassword(String username, String password);
}
