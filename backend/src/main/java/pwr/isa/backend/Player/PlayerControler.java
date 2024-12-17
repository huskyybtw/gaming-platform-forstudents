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

    @GetMapping(path= "/{id}")
    public ResponseEntity<Player> readPlayer(@PathVariable Long id) {
        return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.FOUND);
    }

    @PostMapping(path= "/")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.CREATED);
    }

    @PutMapping(path= "/{id}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long id,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(player,id), HttpStatus.OK);
    }

    @PatchMapping(path= "/{id}")
    public ResponseEntity<Player> patchPlayer(
            @PathVariable Long id,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.patchPlayer(player,id), HttpStatus.OK);
    }

    @DeleteMapping(path= "/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
