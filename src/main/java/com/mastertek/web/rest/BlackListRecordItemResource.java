package com.mastertek.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.BlackListRecordItem;

import com.mastertek.repository.BlackListRecordItemRepository;
import com.mastertek.web.rest.errors.BadRequestAlertException;
import com.mastertek.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BlackListRecordItem.
 */
@RestController
@RequestMapping("/api")
public class BlackListRecordItemResource {

    private final Logger log = LoggerFactory.getLogger(BlackListRecordItemResource.class);

    private static final String ENTITY_NAME = "blackListRecordItem";

    private final BlackListRecordItemRepository blackListRecordItemRepository;

    public BlackListRecordItemResource(BlackListRecordItemRepository blackListRecordItemRepository) {
        this.blackListRecordItemRepository = blackListRecordItemRepository;
    }

    /**
     * POST  /black-list-record-items : Create a new blackListRecordItem.
     *
     * @param blackListRecordItem the blackListRecordItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blackListRecordItem, or with status 400 (Bad Request) if the blackListRecordItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/black-list-record-items")
    @Timed
    public ResponseEntity<BlackListRecordItem> createBlackListRecordItem(@RequestBody BlackListRecordItem blackListRecordItem) throws URISyntaxException {
        log.debug("REST request to save BlackListRecordItem : {}", blackListRecordItem);
        if (blackListRecordItem.getId() != null) {
            throw new BadRequestAlertException("A new blackListRecordItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlackListRecordItem result = blackListRecordItemRepository.save(blackListRecordItem);
        return ResponseEntity.created(new URI("/api/black-list-record-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /black-list-record-items : Updates an existing blackListRecordItem.
     *
     * @param blackListRecordItem the blackListRecordItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blackListRecordItem,
     * or with status 400 (Bad Request) if the blackListRecordItem is not valid,
     * or with status 500 (Internal Server Error) if the blackListRecordItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/black-list-record-items")
    @Timed
    public ResponseEntity<BlackListRecordItem> updateBlackListRecordItem(@RequestBody BlackListRecordItem blackListRecordItem) throws URISyntaxException {
        log.debug("REST request to update BlackListRecordItem : {}", blackListRecordItem);
        if (blackListRecordItem.getId() == null) {
            return createBlackListRecordItem(blackListRecordItem);
        }
        BlackListRecordItem result = blackListRecordItemRepository.save(blackListRecordItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blackListRecordItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /black-list-record-items : get all the blackListRecordItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blackListRecordItems in body
     */
    @GetMapping("/black-list-record-items")
    @Timed
    public List<BlackListRecordItem> getAllBlackListRecordItems() {
        log.debug("REST request to get all BlackListRecordItems");
        return blackListRecordItemRepository.findAll();
        }

    /**
     * GET  /black-list-record-items/:id : get the "id" blackListRecordItem.
     *
     * @param id the id of the blackListRecordItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blackListRecordItem, or with status 404 (Not Found)
     */
    @GetMapping("/black-list-record-items/{id}")
    @Timed
    public ResponseEntity<BlackListRecordItem> getBlackListRecordItem(@PathVariable Long id) {
        log.debug("REST request to get BlackListRecordItem : {}", id);
        BlackListRecordItem blackListRecordItem = blackListRecordItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blackListRecordItem));
    }

    /**
     * DELETE  /black-list-record-items/:id : delete the "id" blackListRecordItem.
     *
     * @param id the id of the blackListRecordItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/black-list-record-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlackListRecordItem(@PathVariable Long id) {
        log.debug("REST request to delete BlackListRecordItem : {}", id);
        blackListRecordItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
