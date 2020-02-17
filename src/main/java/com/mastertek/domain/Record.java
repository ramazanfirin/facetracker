package com.mastertek.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.mastertek.domain.enumeration.RecordStatus;

/**
 * A Record.
 */
@Entity
@Table(name = "record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_insert")
    private Instant insert;

    @Column(name = "path")
    private String path;

    @Column(name = "file_sent_date")
    private Instant fileSentDate;

    @Column(name = "file_creation_date")
    private Instant fileCreationDate;

    @Column(name = "process_start_date")
    private Instant processStartDate;

    @Column(name = "process_finish_date")
    private Instant processFinishDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RecordStatus status;

    @Lob
    @Column(name = "afid")
    private byte[] afid;

    @Column(name = "afid_content_type")
    private String afidContentType;

    @ManyToOne
    private Device device;

    @ManyToOne
    private Image image;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInsert() {
        return insert;
    }

    public Record insert(Instant insert) {
        this.insert = insert;
        return this;
    }

    public void setInsert(Instant insert) {
        this.insert = insert;
    }

    public String getPath() {
        return path;
    }

    public Record path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getFileSentDate() {
        return fileSentDate;
    }

    public Record fileSentDate(Instant fileSentDate) {
        this.fileSentDate = fileSentDate;
        return this;
    }

    public void setFileSentDate(Instant fileSentDate) {
        this.fileSentDate = fileSentDate;
    }

    public Instant getFileCreationDate() {
        return fileCreationDate;
    }

    public Record fileCreationDate(Instant fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
        return this;
    }

    public void setFileCreationDate(Instant fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }

    public Instant getProcessStartDate() {
        return processStartDate;
    }

    public Record processStartDate(Instant processStartDate) {
        this.processStartDate = processStartDate;
        return this;
    }

    public void setProcessStartDate(Instant processStartDate) {
        this.processStartDate = processStartDate;
    }

    public Instant getProcessFinishDate() {
        return processFinishDate;
    }

    public Record processFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
        return this;
    }

    public void setProcessFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public Record status(RecordStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public byte[] getAfid() {
        return afid;
    }

    public Record afid(byte[] afid) {
        this.afid = afid;
        return this;
    }

    public void setAfid(byte[] afid) {
        this.afid = afid;
    }

    public String getAfidContentType() {
        return afidContentType;
    }

    public Record afidContentType(String afidContentType) {
        this.afidContentType = afidContentType;
        return this;
    }

    public void setAfidContentType(String afidContentType) {
        this.afidContentType = afidContentType;
    }

    public Device getDevice() {
        return device;
    }

    public Record device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Image getImage() {
        return image;
    }

    public Record image(Image image) {
        this.image = image;
        return this;
    }

    public void setImage(Image image) {
        this.image = image;
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
        Record record = (Record) o;
        if (record.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), record.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", insert='" + getInsert() + "'" +
            ", path='" + getPath() + "'" +
            ", fileSentDate='" + getFileSentDate() + "'" +
            ", fileCreationDate='" + getFileCreationDate() + "'" +
            ", processStartDate='" + getProcessStartDate() + "'" +
            ", processFinishDate='" + getProcessFinishDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", afid='" + getAfid() + "'" +
            ", afidContentType='" + getAfidContentType() + "'" +
            "}";
    }
}
