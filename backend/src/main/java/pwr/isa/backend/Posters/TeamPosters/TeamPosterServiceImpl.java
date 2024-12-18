package pwr.isa.backend.Posters.TeamPosters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
//TODO Do przetestowania
public class TeamPosterServiceImpl implements TeamPosterService {
    private final TeamPosterRepository teamPosterRepository;

    public TeamPosterServiceImpl(TeamPosterRepository teamPosterRepository) {
        this.teamPosterRepository = teamPosterRepository;
    }

    @Override
    public Iterable<TeamPoster> getAllTeamPosters(int limit, int offset, boolean sortByRating) {
        //  Jeśli sortByRating = true...
        // Domyślnie sortujemy po dacie (created_at).

        return teamPosterRepository.findAllWithLimitAndOffsetSortedByDate(limit, offset);//??
    }

    @Override
    public TeamPoster getTeamPosterById(Long teamId) {
        return teamPosterRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found with id: " + teamId));
    }

    @Override
    public TeamPoster createTeamPoster(TeamPoster teamPoster) {
        //Wlidacj
        validateTeamPoster(teamPoster);
        // Sprawdzmy, czy teamPoster dla danego teamId już istnieje
        Optional<TeamPoster> existingPoster = teamPosterRepository.findByTeamId(teamPoster.getTeamId());
        if (existingPoster.isPresent()) {
            // Jeśli istnieje, to zmiast tworzyć nowy, aktualizujemy go
            return updateTeamPoster(teamPoster, existingPoster.get().getId());
        }
        return teamPosterRepository.save(teamPoster);
    }


    @Override
    public TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId) {
        TeamPoster existing = teamPosterRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found with id: " + teamId));

        // Walidacja
        validateTeamPoster(teamPoster);

        // Aktualizacja danych plakatu
        existing.setDescription(teamPoster.getDescription());
        existing.setCreatedAt(teamPoster.getCreatedAt());
        existing.setDueDate(teamPoster.getDueDate());
        existing.setTeamId(teamPoster.getTeamId());

        return teamPosterRepository.save(existing);
    }

    @Override
    public void deleteTeamPoster(Long teamId) {
        // Sprawdzamy, czy plakat istnieje z takim teamId
        TeamPoster existingPoster = teamPosterRepository.findByTeamId(teamId)
                .orElseThrow(() -> new EntityNotFoundException("TeamPoster not found for team with id: " + teamId));

        // Jeśli plakat istnieje, usuwamy go na podstawie ID
        teamPosterRepository.deleteById(existingPoster.getId());
    }


    private void validateTeamPoster(TeamPoster teamPoster) {
        if (teamPoster.getTeamId() == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        if (teamPoster.getDescription() == null || teamPoster.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (teamPoster.getCreatedAt() == null) {
            throw new IllegalArgumentException("CreatedAt date cannot be null");
        }
        if (teamPoster.getDueDate() != null && teamPoster.getDueDate().before(teamPoster.getCreatedAt())) {
            throw new IllegalArgumentException("DueDate cannot be before CreatedAt");
        }
    }
}
