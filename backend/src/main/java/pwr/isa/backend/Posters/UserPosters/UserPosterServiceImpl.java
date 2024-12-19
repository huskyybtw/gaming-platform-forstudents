package pwr.isa.backend.Posters.UserPosters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.User.UserService;

import java.util.Optional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserPosterServiceImpl implements UserPosterService {
    private final UserPosterRepository userPosterRepository;
    private final UserService userService; // walidacja id

    public UserPosterServiceImpl(UserPosterRepository userPosterRepository, UserService userService) {
        this.userPosterRepository = userPosterRepository;
        this.userService = userService;
    }

    @Override
    public Iterable<UserPoster> getAllUserPosters(int limit, int offset) {
        // Pobieramy plakaty posortowane po dacie z limitem i offsetem
        List<UserPoster> posters = userPosterRepository.findAllWithLimitAndOffsetSortedByDate(limit, offset);
        return posters;
    }

    @Override
    public UserPoster getUserPosterById(Long userId) {
        // Wyszukujemy po userId
        return userPosterRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPoster not found for userId: " + userId));
    }

    @Override
    public UserPoster createUserPoster(UserPoster userPoster) {
        // Ustawiamy createdAt i updatedAt na bieżącą datę przed walidacją
        userPoster.setCreatedAt(new Date());
        userPoster.setUpdatedAt(new Date());

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

        // Ustawiamy createdAt na oryginalną datę utworzenia
        userPoster.setCreatedAt(existing.getCreatedAt());
        // Aktualizujemy updatedAt na bieżącą datę
        userPoster.setUpdatedAt(new Date());

        validateUserPoster(userPoster);

        existing.setDescription(userPoster.getDescription());
        // Nie aktualizujemy createdAt, zachowujemy oryginalną datę utworzenia
        existing.setDueDate(userPoster.getDueDate());
        existing.setUserId(userPoster.getUserId());
        // Ustawiamy updatedAt na bieżącą datę
        existing.setUpdatedAt(userPoster.getUpdatedAt());

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

        // Sprawdzenie, czy użytkownik istnieje
        if (!userService.exists(userPoster.getUserId())) {
            throw new EntityNotFoundException("User not found with id: " + userPoster.getUserId());
        }

        // Dodatkowe walidacje (opcjonalnie)
        if (userPoster.getDescription().length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters");
        }

        Date today = new Date();
        if (userPoster.getDueDate() != null && userPoster.getDueDate().before(today)) {
            throw new IllegalArgumentException("DueDate cannot be in the past");
        }
    }
}
