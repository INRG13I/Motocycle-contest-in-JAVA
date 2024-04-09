package Service;

import Domain.Organizer;
import Domain.Participant;
import Domain.Race;
import Domain.Registration;
import Repository.OrganizerRepository.IOrganiserRepository;
import Repository.ParticipantRepository.IParticipantRepository;
import Repository.RaceRepository.IRaceRepository;
import Repository.RegistrationRepository.IRegistrationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Service {

    IParticipantRepository participantRepository;
    IOrganiserRepository organiserRepository;
    IRaceRepository raceRepository;
    IRegistrationRepository registrationRepository;

    public Service(IParticipantRepository participantRepository, IOrganiserRepository organiserRepository, IRaceRepository raceRepository, IRegistrationRepository registrationRepository) {
        this.participantRepository = participantRepository;
        this.organiserRepository = organiserRepository;
        this.raceRepository = raceRepository;
        this.registrationRepository = registrationRepository;
    }

//----------------------------------------------------------------------------------------------------------------CRUD//
    public void addParticipant(String name, Integer capacity, String team){
        participantRepository.add(new Participant(name, capacity, team));
    }

    public boolean deleteParticipant(String name, Integer capacity, String team){
        Participant participant = participantRepository.findParticipantByNameCapacityAndTeam(name, capacity, team);
        if (participant != null) {
            participantRepository.delete(participant);
            return true;
        } else {
            System.out.println("Participant with name " + name + ", capacity " + capacity + " and team " + team + " not found.");
        }
        return false;
    }

    public boolean deleteParticipant(Integer id){
        Participant participant = participantRepository.findById(id);
        if (participant != null) {
            participantRepository.delete(participant);
            return true;
        } else {
            System.out.println("Participant with id " + id + " not found.");
        }
        return false;
    }

    public boolean updateParticipant(Integer id, String name, Integer capacity, String team){
        Participant participant = participantRepository.findById(id);
        if (participant != null) {
            participantRepository.update(new Participant(name, capacity, team), id);
            return true;
        } else {
            System.out.println("Participant with ID " + id + " not found.");
        }
        return false;
    }

    public Participant findParticipantById(Integer id){return participantRepository.findById(id);}

    public Iterable<Participant> findAllParticipant(){
        return participantRepository.findAll();
    }

    public Participant findParticipantByNameCapacityAndTeam(String name, Integer capacity, String team) {
        return participantRepository.findParticipantByNameCapacityAndTeam(name, capacity, team);
    }

    public void addRace(Integer capacity) {
        raceRepository.add(new Race(capacity));
    }

    public boolean deleteRace(Integer raceId) {
        Race race = raceRepository.findById(raceId);
        if (race != null) {
            raceRepository.delete(race);
            return true;
        } else {
            System.out.println("Race with ID " + raceId + " not found.");
        }
        return false;
    }

    public boolean updateRace(Integer raceId, Integer capacity) {
        Race race = raceRepository.findById(raceId);
        if (race != null) {
            raceRepository.update(new Race(capacity), raceId);
            return true;
        } else {
            System.out.println("Race with ID " + raceId + " not found.");
        }
        return false;
    }

    public Iterable<Race> findAllRace() {
        return raceRepository.findAll();
    }

    public Race findByCapacity(Integer capacity) {
        return raceRepository.findRaceByCapacity(capacity);
    }



    public void addRegistration(Integer raceId, Integer participantId) {
        Race race = raceRepository.findById(raceId);
        Participant participant = participantRepository.findById(participantId);
        if(race == null){
            System.out.println("Race with ID " + raceId + " not found.");
        }else if(participant == null){
            System.out.println("Participant with ID " + participantId + " not found.");
        }else
            registrationRepository.add(new Registration(race, participant));
    }

    public boolean deleteRegistration(Integer registrationId) {
        Registration registration = registrationRepository.findById(registrationId);
        if (registration != null) {
            registrationRepository.delete(registration);
            return true;
        } else {
            System.out.println("Registration with ID " + registrationId + " not found.");
        }
        return false;
    }

    public boolean updateRegistration(Integer registrationId, Integer raceId, Integer participantId) {
        Registration registration = registrationRepository.findById(registrationId);
        Race race = raceRepository.findById(raceId);
        Participant participant = participantRepository.findById(participantId);
        if(race == null){
            System.out.println("Race with ID " + raceId + " not found.");
            return false;
        }else if(participant == null){
            System.out.println("Participant with ID " + participantId + " not found.");
            return false;
        }else if (registration == null) {
            System.out.println("Registration with ID " + registrationId + " not found.");
            return false;
        } else {
            registrationRepository.update(new Registration(race, participant), registrationId);
            return true;
        }

    }

    public Iterable<Registration> findAllRegistration() {
        return registrationRepository.findAll();
    }

    public void addOrganizer(String name, String password) {
        organiserRepository.add(new Organizer(name, password));
    }

    public void deleteOrganizer(Integer organizerId) {
        Organizer organizer = organiserRepository.findById(organizerId);
        if (organizer != null) {
            organiserRepository.delete(organizer);
        } else {
            System.out.println("Organizer with ID " + organizerId + " not found.");
        }
    }

    public void deleteOrganizer(String username, String password) {
        Organizer organizer = organiserRepository.findOrganizerByUsernameAndPassword(username, password);
        if (organizer != null) {
            organiserRepository.delete(organizer);
        } else {
            System.out.println("Organizer with username " + username + " not found.");
        }
    }

    public void update(Integer organizerId, String name, String password) {
        Organizer organizer = organiserRepository.findById(organizerId);
        if (organizer != null) {
            organiserRepository.update(new Organizer(name, password), organizerId);
        } else {
            System.out.println("Organizer with ID " + organizerId + " not found.");
        }
    }

    public Iterable<Organizer> findAll() {
        return organiserRepository.findAll();
    }



//--------------------------------------------------------------------------------------------------------------------//
    public boolean validateLogin(String username, String password) {
        return (organiserRepository.findOrganizerByUsernameAndPassword(username, password) != null);
    }

    public Map<String, Integer> getNumberOfParticipantsByRace() {
        Map<String, Integer> participantsByRace = new HashMap<>();

        Iterable<Registration> registrations = registrationRepository.findAll();
        for (Registration registration : registrations) {
            String raceCapacity = registration.getRace().getCapacity().toString();
            participantsByRace.put(raceCapacity, participantsByRace.getOrDefault(raceCapacity, 0) + 1);
        }
        return participantsByRace;
    }

    public StringBuilder getTeamParticipants(String team) {
        StringBuilder result = new StringBuilder();
        ArrayList<Participant> participants = (ArrayList<Participant>) participantRepository.findAll();
        for (Participant participant : participants) {
            if (participant.getTeam().equals(team)) {
                result.append("Name: ").append(participant.getName()).append(", Capacity: ").append(participant.getCapacity()).append("\n");
            }
        }

        return result;
    }


}
