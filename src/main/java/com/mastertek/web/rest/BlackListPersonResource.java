package com.mastertek.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.BlackListPerson;

import com.mastertek.repository.BlackListPersonRepository;
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
 * REST controller for managing BlackListPerson.
 */
@RestController
@RequestMapping("/api")
public class BlackListPersonResource {

    private final Logger log = LoggerFactory.getLogger(BlackListPersonResource.class);

    private static final String ENTITY_NAME = "blackListPerson";

    private final BlackListPersonRepository blackListPersonRepository;

    public BlackListPersonResource(BlackListPersonRepository blackListPersonRepository) {
        this.blackListPersonRepository = blackListPersonRepository;
    }

    /**
     * POST  /black-list-people : Create a new blackListPerson.
     *
     * @param blackListPerson the blackListPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blackListPerson, or with status 400 (Bad Request) if the blackListPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/black-list-people")
    @Timed
    public ResponseEntity<BlackListPerson> createBlackListPerson(@Valid @RequestBody BlackListPerson blackListPerson) throws URISyntaxException {
        log.debug("REST request to save BlackListPerson : {}", blackListPerson);
        if (blackListPerson.getId() != null) {
            throw new BadRequestAlertException("A new blackListPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlackListPerson result = blackListPersonRepository.save(blackListPerson);
        return ResponseEntity.created(new URI("/api/black-list-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /black-list-people : Updates an existing blackListPerson.
     *
     * @param blackListPerson the blackListPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blackListPerson,
     * or with status 400 (Bad Request) if the blackListPerson is not valid,
     * or with status 500 (Internal Server Error) if the blackListPerson couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/black-list-people")
    @Timed
    public ResponseEntity<BlackListPerson> updateBlackListPerson(@Valid @RequestBody BlackListPerson blackListPerson) throws URISyntaxException {
        log.debug("REST request to update BlackListPerson : {}", blackListPerson);
        if (blackListPerson.getId() == null) {
            return createBlackListPerson(blackListPerson);
        }
        BlackListPerson result = blackListPersonRepository.save(blackListPerson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blackListPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /black-list-people : get all the blackListPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blackListPeople in body
     */
    @GetMapping("/black-list-people")
    @Timed
    public List<BlackListPerson> getAllBlackListPeople() {
        log.debug("REST request to get all BlackListPeople");
        return blackListPersonRepository.findAll();
        }

    /**
     * GET  /black-list-people/:id : get the "id" blackListPerson.
     *
     * @param id the id of the blackListPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blackListPerson, or with status 404 (Not Found)
     */
    @GetMapping("/black-list-people/{id}")
    @Timed
    public ResponseEntity<BlackListPerson> getBlackListPerson(@PathVariable Long id) {
        log.debug("REST request to get BlackListPerson : {}", id);
        BlackListPerson blackListPerson = blackListPersonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blackListPerson));
    }

    /**
     * DELETE  /black-list-people/:id : delete the "id" blackListPerson.
     *
     * @param id the id of the blackListPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/black-list-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlackListPerson(@PathVariable Long id) {
        log.debug("REST request to delete BlackListPerson : {}", id);
        blackListPersonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
