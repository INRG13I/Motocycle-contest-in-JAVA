package Repository.RaceRepository;

import Domain.Race;
import Repository.IRepository;

public interface IRaceRepository extends IRepository<Race,Integer> {
    public Race findRaceByCapacity(Integer capacity);
}
