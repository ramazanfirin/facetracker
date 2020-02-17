package com.mastertek.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.WhiteListPerson;

import com.mastertek.repository.WhiteListPersonRepository;
import com.mastertek.web.rest.errors.BadRequestAlertException;
import com.mastertek.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WhiteListPerson.
 */
@RestController
@RequestMapping("/api")
public class WhiteListPersonResource {

    private final Logger log = LoggerFactory.getLogger(WhiteListPersonResource.class);

    private static final String ENTITY_NAME = "whiteListPerson";

    private final WhiteListPersonRepository whiteListPersonRepository;

    public WhiteListPersonResource(WhiteListPersonRepository whiteListPersonRepository) {
        this.whiteListPersonRepository = whiteListPersonRepository;
    }

    /**
     * POST  /white-list-people : Create a new whiteListPerson.
     *
     * @param whiteListPerson the whiteListPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new whiteListPerson, or with status 400 (Bad Request) if the whiteListPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/white-list-people")
    @Timed
    public ResponseEntity<WhiteListPerson> createWhiteListPerson(@Valid @RequestBody WhiteListPerson whiteListPerson) throws URISyntaxException {
        log.debug("REST request to save WhiteListPerson : {}", whiteListPerson);
        if (whiteListPerson.getId() != null) {
            throw new BadRequestAlertException("A new whiteListPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WhiteListPerson result = whiteListPersonRepository.save(whiteListPerson);
        return ResponseEntity.created(new URI("/api/white-list-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /white-list-people : Updates an existing whiteListPerson.
     *
     * @param whiteListPerson the whiteListPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated whiteListPerson,
     * or with status 400 (Bad Request) if the whiteListPerson is not valid,
     * or with status 500 (Internal Server Error) if the whiteListPerson couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/white-list-people")
    @Timed
    public ResponseEntity<WhiteListPerson> updateWhiteListPerson(@Valid @RequestBody WhiteListPerson whiteListPerson) throws URISyntaxException {
        log.debug("REST request to update WhiteListPerson : {}", whiteListPerson);
        if (whiteListPerson.getId() == null) {
            return createWhiteListPerson(whiteListPerson);
        }
        WhiteListPerson result = whiteListPersonRepository.save(whiteListPerson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, whiteListPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /white-list-people : get all the whiteListPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of whiteListPeople in body
     */
    @GetMapping("/white-list-people")
    @Timed
    public List<WhiteListPerson> getAllWhiteListPeople() {
        log.debug("REST request to get all WhiteListPeople");
        return whiteListPersonRepository.findAll();
        }

    /**
     * GET  /white-list-people/:id : get the "id" whiteListPerson.
     *
     * @param id the id of the whiteListPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the whiteListPerson, or with status 404 (Not Found)
     */
    @GetMapping("/white-list-people/{id}")
    @Timed
    public ResponseEntity<WhiteListPerson> getWhiteListPerson(@PathVariable Long id) {
        log.debug("REST request to get WhiteListPerson : {}", id);
        WhiteListPerson whiteListPerson = whiteListPersonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(whiteListPerson));
    }

    /**
     * DELETE  /white-list-people/:id : delete the "id" whiteListPerson.
     *
     * @param id the id of the whiteListPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/white-list-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteWhiteListPerson(@PathVariable Long id) {
        log.debug("REST request to delete WhiteListPerson : {}", id);
        whiteListPersonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
