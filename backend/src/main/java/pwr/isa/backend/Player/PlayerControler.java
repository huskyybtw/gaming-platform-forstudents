package pwr.isa.backend.Player;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/players")
public class PlayerControler {

    private final PlayerService playerService;

    public PlayerControler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path= "/")
    public Iterable<Player> readPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping(path= "/{userId}")
    public ResponseEntity<Player> readPlayer(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerById(userId), HttpStatus.FOUND);
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
