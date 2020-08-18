package com.mastertek.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.Record;
import com.mastertek.domain.RecordSense;

import com.mastertek.repository.RecordSenseRepository;
import com.mastertek.web.rest.errors.BadRequestAlertException;
import com.mastertek.web.rest.util.HeaderUtil;
import com.mastertek.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RecordSense.
 */
@RestController
@RequestMapping("/api")
public class RecordSenseResource {

    private final Logger log = LoggerFactory.getLogger(RecordSenseResource.class);

    private static final String ENTITY_NAME = "recordSense";

    private final RecordSenseRepository recordSenseRepository;
    
    String pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    

    public RecordSenseResource(RecordSenseRepository recordSenseRepository) {
        this.recordSenseRepository = recordSenseRepository;
    }

    /**
     * POST  /record-senses : Create a new recordSense.
     *
     * @param recordSense the recordSense to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recordSense, or with status 400 (Bad Request) if the recordSense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/record-senses")
    @Timed
    public ResponseEntity<RecordSense> createRecordSense(@RequestBody RecordSense recordSense) throws URISyntaxException {
        log.debug("REST request to save RecordSense : {}", recordSense);
        if (recordSense.getId() != null) {
            throw new BadRequestAlertException("A new recordSense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecordSense result = recordSenseRepository.save(recordSense);
        return ResponseEntity.created(new URI("/api/record-senses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /record-senses : Updates an existing recordSense.
     *
     * @param recordSense the recordSense to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recordSense,
     * or with status 400 (Bad Request) if the recordSense is not valid,
     * or with status 500 (Internal Server Error) if the recordSense couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/record-senses")
    @Timed
    public ResponseEntity<RecordSense> updateRecordSense(@RequestBody RecordSense recordSense) throws URISyntaxException {
        log.debug("REST request to update RecordSense : {}", recordSense);
        if (recordSense.getId() == null) {
            return createRecordSense(recordSense);
        }
        RecordSense result = recordSenseRepository.save(recordSense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recordSense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /record-senses : get all the recordSenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recordSenses in body
     */
    @GetMapping("/record-senses")
    @Timed
    public ResponseEntity<List<RecordSense>> getAllRecordSenses(Pageable pageable) {
        log.debug("REST request to get a page of RecordSenses");
        Page<RecordSense> page = recordSenseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/record-senses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /record-senses/:id : get the "id" recordSense.
     *
     * @param id the id of the recordSense to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recordSense, or with status 404 (Not Found)
     */
    @GetMapping("/record-senses/{id}")
    @Timed
    public ResponseEntity<RecordSense> getRecordSense(@PathVariable Long id) {
        log.debug("REST request to get RecordSense : {}", id);
        RecordSense recordSense = recordSenseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recordSense));
    }

    /**
     * DELETE  /record-senses/:id : delete the "id" recordSense.
     *
     * @param id the id of the recordSense to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/record-senses/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecordSense(@PathVariable Long id) {
        log.debug("REST request to delete RecordSense : {}", id);
        recordSenseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/record-senses/getRecordsForReport")
    public List<RecordSense> getRecordsForReport(@RequestParam("personId") Long personId,@RequestParam("startDate") String startDateValue,@RequestParam("endDate") String endDateValue) throws IOException, ParseException {
        //InputStream in =;
    	
    	Date startDate = getDate(startDateValue);
        Date endDate= getDate(endDateValue);
        
        List<RecordSense> list = recordSenseRepository.findRecords(personId,startDate.toInstant(), endDate.toInstant());
        System.out.println("asd");
        return list;
}
    
    private Date getDate(String startDateValue) throws ParseException {
    	Date startDate = simpleDateFormat.parse(startDateValue);
        if(startDate.getHours()==0)
        	startDate =addHoursToDate(startDate, 15);
        else
        	startDate =addHoursToDate(startDate, 3);
        return startDate;
    }
    
    private Date addHoursToDate(Date date, int count) {
    	Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, count);
        return c.getTime();
    }
}
