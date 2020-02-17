package com.mastertek.repository;

import com.mastertek.domain.BlackListPerson;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BlackListPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlackListPersonRepository extends JpaRepository<BlackListPerson, Long> {

}
