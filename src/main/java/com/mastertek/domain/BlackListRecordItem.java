package com.mastertek.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A BlackListRecordItem.
 */
@Entity
@Table(name = "black_list_record_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BlackListRecordItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Record record;

    @ManyToOne
    private BlackListPerson person;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Record getRecord() {
        return record;
    }

    public BlackListRecordItem record(Record record) {
        this.record = record;
        return this;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public BlackListPerson getPerson() {
        return person;
    }

    public BlackListRecordItem person(BlackListPerson blackListPerson) {
        this.person = blackListPerson;
        return this;
    }

    public void setPerson(BlackListPerson blackListPerson) {
        this.person = blackListPerson;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlackListRecordItem blackListRecordItem = (BlackListRecordItem) o;
        if (blackListRecordItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blackListRecordItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlackListRecordItem{" +
            "id=" + getId() +
            "}";
    }
}
