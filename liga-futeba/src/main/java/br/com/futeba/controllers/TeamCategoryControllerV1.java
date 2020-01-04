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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.futeba.models.Category;
import br.com.futeba.services.CategoryService;
import br.com.futeba.utils.HeaderUtil;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/teamCategory")
public class TeamCategoryControllerV1 {

	private static final String ENTITY_NAME = "category";

	private static final Logger logger = LoggerFactory
			.getLogger(TeamCategoryControllerV1.class);

	@Autowired
	private CategoryService service;

	@GetMapping("/listAll")
	public ResponseEntity<List<Category>> listAll() {
		logger.info("Listing all team category");
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping("/list/{id}")
	public ResponseEntity<Category> listById(@PathVariable Long id) {
		logger.info("Find category id: {}", id);
		Optional<Category> category = service.findById(id);
		return category.map(
				response -> ResponseEntity.ok().headers(null).body(response))
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND));
	}

	@GetMapping("/list/name/{name}")
	public ResponseEntity<Category> getCategoryByName(
			@PathVariable("name") String name) {
		logger.info("Find category: {}", name);
		Optional<Category> category = service.findByName(name);
		return category.map(
				response -> ResponseEntity.ok().headers(null).body(response))
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND));

	}

	@PostMapping("/")
	public ResponseEntity<Category> save(@RequestBody Category teamCategory) {

		logger.debug("REST request to save Category: {}", teamCategory);
		Optional<Category> foundCategory = service
				.findByName(teamCategory.getName());
		if (foundCategory.isPresent() && teamCategory.getName()
				.equals(foundCategory.get().getName())) {
			return getHttpStatusBadRequest("categoryExists",
					"A new category cannot exist");
		}
		try {
			Category category = service.save(teamCategory);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(teamCategory.getId()).toUri();

			return ResponseEntity.created(uri)
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
							category.getId().toString()))
					.body(category);

		} catch (Exception e) {
			return ResponseEntity.badRequest().headers(HeaderUtil
					.createErrorAlert("Check if parameters are correct!"))
					.body(null);
		}
	}

	private ResponseEntity<Category> getHttpStatusBadRequest(String errorKey,
			String defaultMessage) {
		return ResponseEntity.badRequest().headers(HeaderUtil
				.createFailureAlert(ENTITY_NAME, errorKey, defaultMessage))
				.body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@PathVariable Long id,
			@RequestBody Category teamCategory) {

		Optional<Category> foundCategory = service
				.findByName(teamCategory.getName());

		if (foundCategory.isPresent() && teamCategory.getName()
				.equals(foundCategory.get().getName())) {
			return getHttpStatusBadRequest("categoryExists",
					"A new category cannot exist");
		}
		teamCategory.setId(id);
		service.update(Optional.of(teamCategory));
		return ResponseEntity.noContent().build();
	}

	// @PutMapping("/update/{id}")
	// public Category update(@PathVariable Long id,
	// @RequestBody Category teamCategory) {
	//
	// Optional<Category> currentTeamCategory = service.findById(id);
	//
	// if (currentTeamCategory.isPresent()) {
	// logger.info("Updating team category id {}", id);
	// currentTeamCategory.get().setName(teamCategory.getName());
	// return service.update(currentTeamCategory);
	// } else {
	// logger.error("Team category id {} not found.", id);
	// return currentTeamCategory.get();
	// }
	//
	// }

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") final long id) {
		Optional<Category> currentTeamCategory = service.findById(id);

		logger.info("Deleting team category id {}", id);
		if (currentTeamCategory.isPresent()) {
			service.delete(id);
		} else {
			logger.error("Team category id {} not found.", id);
		}
	}

	@DeleteMapping("/delete")
	public void delete() {

		logger.info("Deleting all team category");
		service.deleteAll();
	}
}
