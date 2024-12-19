package pwr.isa.backend.Player;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Consumer.DTO.LeagueDTO;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
public class PlayerControler {

    private final PlayerService playerService;

    public PlayerControler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path= "/")
    public Iterable<Player> readPlayers(
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false,defaultValue = "false") boolean sortByRating
    ) {
        return playerService.getAllPlayers(limit, offset, sortByRating);
    }

    @GetMapping(path= "/{userId}")
    public ResponseEntity<Player> readPlayer(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerById(userId), HttpStatus.FOUND);
    }

    @GetMapping(path= "/details/{userId}")
    public ResponseEntity<Player> refreshPlayer(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.refreshPlayer(userId), HttpStatus.OK);
    }

    @GetMapping(path= "/riot/rank/{userId}")
    public ResponseEntity<List<LeagueDTO>> getPlayerRank(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerRank(userId), HttpStatus.OK);
    }

    @PostMapping(path= "/")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.CREATED);
    }

    @PutMapping(path= "/{userId}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(player, userId), HttpStatus.OK);
    }

    @PatchMapping(path= "/{userId}")
    public ResponseEntity<Player> patchPlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.patchPlayer(player, userId), HttpStatus.OK);
    }

    @DeleteMapping(path= "/{userId}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long userId) {
        playerService.deletePlayer(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
