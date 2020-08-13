package com.mastertek.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.Compare;
import com.mastertek.repository.CompareRepository;
import com.mastertek.service.DiviEngineService;
import com.mastertek.web.rest.errors.BadRequestAlertException;
import com.mastertek.web.rest.util.HeaderUtil;
import com.mastertek.web.rest.vm.SimilarityResult;
import com.vdt.face_recognition.sdk.Template;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Compare.
 */
@RestController
@RequestMapping("/api")
public class CompareResource {

    private final Logger log = LoggerFactory.getLogger(CompareResource.class);

    private static final String ENTITY_NAME = "compare";

    private final CompareRepository compareRepository;

    @Autowired
    DiviEngineService diviEngineService;
    
    public CompareResource(CompareRepository compareRepository) {
        this.compareRepository = compareRepository;
    }

    /**
     * POST  /compares : Create a new compare.
     *
     * @param compare the compare to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compare, or with status 400 (Bad Request) if the compare has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compares")
    @Timed
    public SimilarityResult createCompare(@RequestBody Compare compare) throws URISyntaxException {
        log.debug("REST request to save Compare : {}", compare);
        if (compare.getId() != null) {
            throw new BadRequestAlertException("A new compare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        byte[] image1 = compare.getImage1();
		byte[] image2 = compare.getImage2();
		
		
		Long start  = System.currentTimeMillis();
		Template template1 = diviEngineService.getTemplate(image1);
		Template template2 =  diviEngineService.getTemplate(image2);
		
		Float similar = diviEngineService.match(template1,template2);
		
		Long end  = System.currentTimeMillis();
		Long duration = end-start;
		
		SimilarityResult similarityResult = new SimilarityResult();;
		similarityResult.setDuration(duration);
		similarityResult.setSimilarity(similar.toString());
		
        return similarityResult;
    }

    /**
     * PUT  /compares : Updates an existing compare.
     *
     * @param compare the compare to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compare,
     * or with status 400 (Bad Request) if the compare is not valid,
     * or with status 500 (Internal Server Error) if the compare couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/compares")
    @Timed
    public ResponseEntity<Compare> updateCompare(@RequestBody Compare compare) throws URISyntaxException {
        log.debug("REST request to update Compare : {}", compare);
        
        Compare result = compareRepository.save(compare);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compare.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compares : get all the compares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compares in body
     */
    @GetMapping("/compares")
    @Timed
    public List<Compare> getAllCompares() {
        log.debug("REST request to get all Compares");
        return compareRepository.findAll();
        }

    /**
     * GET  /compares/:id : get the "id" compare.
     *
     * @param id the id of the compare to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compare, or with status 404 (Not Found)
     */
    @GetMapping("/compares/{id}")
    @Timed
    public ResponseEntity<Compare> getCompare(@PathVariable Long id) {
        log.debug("REST request to get Compare : {}", id);
        Compare compare = compareRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(compare));
    }

    /**
     * DELETE  /compares/:id : delete the "id" compare.
     *
     * @param id the id of the compare to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compares/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompare(@PathVariable Long id) {
        log.debug("REST request to delete Compare : {}", id);
        compareRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
