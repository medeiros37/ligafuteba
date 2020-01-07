package br.com.futeba.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.futeba.models.Game;
import br.com.futeba.services.GameService;
import br.com.futeba.utils.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class GameControllerV1 {

	private static final String ENTITY_NAME = "game";

	private static final Logger logger = LoggerFactory
			.getLogger(GameControllerV1.class);

	@Autowired
	private GameService service;

	@GetMapping("/games")
	public ResponseEntity<List<Game>> listAll() {
		logger.info("Listing all Games");
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping("/games/{id}")
	public ResponseEntity<Game> listById(@PathVariable Long id) {
		logger.info("Find game id: {}", id);
		Optional<Game> game = service.findById(id);
		return game.map(
				response -> ResponseEntity.ok().headers(null).body(response))
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/games/{year}", produces = "application/json")
	@ApiOperation(value = "Return a list of games by year")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return a list of games"),
			@ApiResponse(code = 403, message = "You do not have permission to access this feature"),
			@ApiResponse(code = 500, message = "An exception was thrown"),})
	public @ResponseBody List<Game> listByYear(
			@PathVariable("year") final Integer year) {
		return service.findByYear(year);
	}

	@PostMapping("/games")
	public ResponseEntity<Game> save(@RequestBody final Game game) {
		try {
			Game gameSaved = service.save(game);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(game.getId()).toUri();

			return ResponseEntity.created(uri)
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
							gameSaved.getId().toString()))
					.body(gameSaved);

		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil
					.createErrorAlert("Check if parameters are correct!"))
					.body(null);
		}
	}

	@PutMapping("/games/{id}")
	public ResponseEntity<Game> update(@PathVariable("id") final long id,
			@RequestBody final Game game) {

		game.setId(id);
		service.update(Optional.of(game));
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/games/{id}")
	public ResponseEntity<Game> deleteById(@PathVariable("id") final long id) {
		logger.info("Deleting gamr id {}", id);
		service.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil
				.createEntityDeletionAlert(ENTITY_NAME, String.valueOf(id)))
				.build();
	}

	@DeleteMapping("/games")
	public void deleteAllGames() {
		logger.info("Deleting all games");
		service.deleteAll();
	}
}
