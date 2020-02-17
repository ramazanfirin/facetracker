package com.mastertek.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.WhiteListRecordItem;

import com.mastertek.repository.WhiteListRecordItemRepository;
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
 * REST controller for managing WhiteListRecordItem.
 */
@RestController
@RequestMapping("/api")
public class WhiteListRecordItemResource {

    private final Logger log = LoggerFactory.getLogger(WhiteListRecordItemResource.class);

    private static final String ENTITY_NAME = "whiteListRecordItem";

    private final WhiteListRecordItemRepository whiteListRecordItemRepository;

    public WhiteListRecordItemResource(WhiteListRecordItemRepository whiteListRecordItemRepository) {
        this.whiteListRecordItemRepository = whiteListRecordItemRepository;
    }

    /**
     * POST  /white-list-record-items : Create a new whiteListRecordItem.
     *
     * @param whiteListRecordItem the whiteListRecordItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new whiteListRecordItem, or with status 400 (Bad Request) if the whiteListRecordItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/white-list-record-items")
    @Timed
    public ResponseEntity<WhiteListRecordItem> createWhiteListRecordItem(@RequestBody WhiteListRecordItem whiteListRecordItem) throws URISyntaxException {
        log.debug("REST request to save WhiteListRecordItem : {}", whiteListRecordItem);
        if (whiteListRecordItem.getId() != null) {
            throw new BadRequestAlertException("A new whiteListRecordItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WhiteListRecordItem result = whiteListRecordItemRepository.save(whiteListRecordItem);
        return ResponseEntity.created(new URI("/api/white-list-record-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /white-list-record-items : Updates an existing whiteListRecordItem.
     *
     * @param whiteListRecordItem the whiteListRecordItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated whiteListRecordItem,
     * or with status 400 (Bad Request) if the whiteListRecordItem is not valid,
     * or with status 500 (Internal Server Error) if the whiteListRecordItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/white-list-record-items")
    @Timed
    public ResponseEntity<WhiteListRecordItem> updateWhiteListRecordItem(@RequestBody WhiteListRecordItem whiteListRecordItem) throws URISyntaxException {
        log.debug("REST request to update WhiteListRecordItem : {}", whiteListRecordItem);
        if (whiteListRecordItem.getId() == null) {
            return createWhiteListRecordItem(whiteListRecordItem);
        }
        WhiteListRecordItem result = whiteListRecordItemRepository.save(whiteListRecordItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, whiteListRecordItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /white-list-record-items : get all the whiteListRecordItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of whiteListRecordItems in body
     */
    @GetMapping("/white-list-record-items")
    @Timed
    public List<WhiteListRecordItem> getAllWhiteListRecordItems() {
        log.debug("REST request to get all WhiteListRecordItems");
        return whiteListRecordItemRepository.findAll();
        }

    /**
     * GET  /white-list-record-items/:id : get the "id" whiteListRecordItem.
     *
     * @param id the id of the whiteListRecordItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the whiteListRecordItem, or with status 404 (Not Found)
     */
    @GetMapping("/white-list-record-items/{id}")
    @Timed
    public ResponseEntity<WhiteListRecordItem> getWhiteListRecordItem(@PathVariable Long id) {
        log.debug("REST request to get WhiteListRecordItem : {}", id);
        WhiteListRecordItem whiteListRecordItem = whiteListRecordItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(whiteListRecordItem));
    }

    /**
     * DELETE  /white-list-record-items/:id : delete the "id" whiteListRecordItem.
     *
     * @param id the id of the whiteListRecordItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/white-list-record-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteWhiteListRecordItem(@PathVariable Long id) {
        log.debug("REST request to delete WhiteListRecordItem : {}", id);
        whiteListRecordItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
