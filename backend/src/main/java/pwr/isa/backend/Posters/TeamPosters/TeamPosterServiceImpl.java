package pwr.isa.backend.Posters.TeamPosters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Team.TeamService;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TeamPosterServiceImpl implements TeamPosterService {
    private final TeamPosterRepository teamPosterRepository;
    private final TeamService teamService; // walidacja istnienia

    public TeamPosterServiceImpl(TeamPosterRepository teamPosterRepository, TeamService teamService) {
        this.teamPosterRepository = teamPosterRepository;
        this.teamService = teamService;
    }

    @Override
    public Iterable<TeamPoster> getAllTeamPosters(int limit, int offset, boolean sortByRating) {
        if (sortByRating) {
            return teamPosterRepository.findAllWithLimitAndOffsetSortedByDate(limit, offset);
        }
        return teamPosterRepository.findAllWithLimitAndOffsetSortedByDate(limit, offset);
    }

    @Override
    public TeamPoster getTeamPosterById(Long teamId) {
        return teamPosterRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found with id: " + teamId));
    }

    @Override
    public TeamPoster createTeamPoster(TeamPoster teamPoster) {
        // Sprawdzenie istnienia
        teamService.getTeamById(teamPoster.getTeamId());

        // Sprawdzamy, czy plakat dla teamId istnieje
        Optional<TeamPoster> existingPoster = teamPosterRepository.findByTeamId(teamPoster.getTeamId());
        if (existingPoster.isPresent()) {
            // Nadpisujemy istniejący
            return updateTeamPoster(teamPoster, existingPoster.get().getId());
        }

        // Ustawiamy createdAt i updatedAt na bieżącą
        teamPoster.setCreatedAt(new Date());
        teamPoster.setUpdatedAt(new Date());

        // Walidacja
        validateTeamPoster(teamPoster);

        return teamPosterRepository.save(teamPoster);
    }

    @Override
    public TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId) {
        TeamPoster existing = teamPosterRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found with id: " + teamId));

        // Sprawdzenie istnienia zespołu
        teamService.getTeamById(teamPoster.getTeamId());

        // Ustawiamy createdAt na oryginalną datę utworzenia
        teamPoster.setCreatedAt(existing.getCreatedAt());
        // Aktualizujemy updatedAt na bieżącą
        teamPoster.setUpdatedAt(new Date());

        // Walidacja
        validateTeamPoster(teamPoster);

        // Aktualizacja danych
        existing.setDescription(teamPoster.getDescription());
        existing.setDueDate(teamPoster.getDueDate());
        existing.setTeamId(teamPoster.getTeamId());
        existing.setUpdatedAt(teamPoster.getUpdatedAt());

        return teamPosterRepository.save(existing);
    }

    @Override
    public void deleteTeamPoster(Long teamId) {
        // Sprawdzamy, czy istnieje z takim teamId
        TeamPoster existingPoster = teamPosterRepository.findByTeamId(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found for team with id: " + teamId));

        // Usuwamy na podstawie ID
        teamPosterRepository.deleteById(existingPoster.getId());
    }

    private void validateTeamPoster(TeamPoster teamPoster) {
        // Sprawdzamy, czy teamId jest podane
        if (teamPoster.getTeamId() == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }

        // Sprawdzamy, czy opis jest podany
        if (teamPoster.getDescription() == null || teamPoster.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        // Walidacja dat
        if (teamPoster.getDueDate() == null) {
            teamPoster.setDueDate(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)); // Domyślnie 7 dni od teraz
        } else if (teamPoster.getDueDate().before(teamPoster.getCreatedAt())) {
            throw new IllegalArgumentException("DueDate cannot be before CreatedAt");
        }
    }
}
