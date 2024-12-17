package pwr.isa.backend.Team;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team createTeam(Team team) {
        // Sprawdzenie, czy nazwa zespołu została podana
        if (team.getTeamName() == null || team.getTeamName().isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        // Sprawdzenie unikalności nazwy zespołu
        boolean exists = teamRepository.findByTeamName(team.getTeamName()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("A team with this name already exists");
        }

        // Opcjonalne dodatkowe walidacje (np. sprawdzenie poprawności kapitana)
        if (team.getTeamCaptain() == null) {
            throw new IllegalArgumentException("Team captain cannot be null");
        }

        // Zapis zespołu do bazy danych
        return teamRepository.save(team);
    }


    @Override
    public Team updateTeam(Long id, Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        // Pełna aktualizacja (PUT):

        // Aktualizacja teamName –bnazwa drużyny musi być jakas
        if (updatedTeam.getTeamName() != null && !updatedTeam.getTeamName().isBlank()) {
            existingTeam.setTeamName(updatedTeam.getTeamName());
        }
        // Jeśli jest null lub puste, nie zmieniamy istniejącej nazwy.

        // Aktualizacja description – opcjonalne pole, jeśli null, zachowujemy starą wartość
        if (updatedTeam.getDescription() != null) {
            existingTeam.setDescription(updatedTeam.getDescription());
        }
        // Jeśli null, opis pozostaje bez zmian.

        // Aktualizacja teamCaptain – jeśli null, zachowujemy poprzednią wartość
        if (updatedTeam.getTeamCaptain() != null) {
            existingTeam.setTeamCaptain(updatedTeam.getTeamCaptain());
        }
        // Jeśli null, zachowujemy poprzednią wartość kapitana.

        return teamRepository.save(existingTeam);
    }

    @Override
    public Team patchTeam(Long id, Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        // PATCH:
        // Aktualizujemy tylko pola, które zostały podane

        if (updatedTeam.getTeamName() != null && !updatedTeam.getTeamName().isBlank()) {
            existingTeam.setTeamName(updatedTeam.getTeamName());
        }

        if (updatedTeam.getDescription() != null) {
            existingTeam.setDescription(updatedTeam.getDescription());
        }

        if (updatedTeam.getTeamCaptain() != null) {
            existingTeam.setTeamCaptain(updatedTeam.getTeamCaptain());
        }

        return teamRepository.save(existingTeam);
    }

    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new EntityNotFoundException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    }

    @Override
    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }
}
