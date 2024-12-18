package pwr.isa.backend.Posters.UserPosters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class UserPosterServiceImpl implements UserPosterService {
    private final UserPosterRepository userPosterRepository;

    public UserPosterServiceImpl(UserPosterRepository userPosterRepository) {
        this.userPosterRepository = userPosterRepository;
    }

    @Override
    public Iterable<UserPoster> getAllUserPosters(int limit, int offset) {
        // Pobieramy plakaty posortowane po dacie z limitem i offsetem
        List<UserPoster> posters = userPosterRepository.findAllWithLimitAndOffsetSortedByDate(limit, offset);
        return posters;
    }

    @Override
    public UserPoster getUserPosterById(Long userId) {
        // Wyszukujemy plakat po userId
        return userPosterRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPoster not found for userId: " + userId));
    }

    @Override
    public UserPoster createUserPoster(UserPoster userPoster) {
        validateUserPoster(userPoster);

        Optional<UserPoster> existingPoster = userPosterRepository.findByUserId(userPoster.getUserId());
        if (existingPoster.isPresent()) {
            // Jeśli istnieje, aktualizujemy go zamiast tworzyć nowy
            return updateUserPoster(userPoster, existingPoster.get().getId());
        }

        return userPosterRepository.save(userPoster);
    }

    @Override
    public UserPoster updateUserPoster(UserPoster userPoster, Long userId) {
        UserPoster existing = userPosterRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPoster not found with id: " + userId));

        validateUserPoster(userPoster);

        existing.setDescription(userPoster.getDescription());
        existing.setCreatedAt(userPoster.getCreatedAt());
        existing.setDueDate(userPoster.getDueDate());
        existing.setUserId(userPoster.getUserId());

        return userPosterRepository.save(existing);
    }

    @Override
    public void deleteUserPoster(Long userId) {
        UserPoster existingPoster = userPosterRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPoster not found for userId: " + userId));

        userPosterRepository.deleteById(existingPoster.getId());
    }

    private void validateUserPoster(UserPoster userPoster) {
        if (userPoster.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userPoster.getDescription() == null || userPoster.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (userPoster.getCreatedAt() == null) {
            throw new IllegalArgumentException("CreatedAt date cannot be null");
        }
        if (userPoster.getDueDate() != null && userPoster.getDueDate().before(userPoster.getCreatedAt())) {
            throw new IllegalArgumentException("DueDate cannot be before CreatedAt");
        }
    }
}
