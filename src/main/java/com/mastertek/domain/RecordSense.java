package com.mastertek.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.mastertek.domain.enumeration.RecordStatus;

/**
 * A RecordSense.
 */
@Entity
@Table(name = "record_sense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecordSense implements Serializable {

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

    @Column(name = "is_processed")
    private Boolean isProcessed;

    @Column(name = "similarity")
    private Float similarity;

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

    public RecordSense insert(Instant insert) {
        this.insert = insert;
        return this;
    }

    public void setInsert(Instant insert) {
        this.insert = insert;
    }

    public String getPath() {
        return path;
    }

    public RecordSense path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getFileSentDate() {
        return fileSentDate;
    }

    public RecordSense fileSentDate(Instant fileSentDate) {
        this.fileSentDate = fileSentDate;
        return this;
    }

    public void setFileSentDate(Instant fileSentDate) {
        this.fileSentDate = fileSentDate;
    }

    public Instant getFileCreationDate() {
        return fileCreationDate;
    }

    public RecordSense fileCreationDate(Instant fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
        return this;
    }

    public void setFileCreationDate(Instant fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }

    public Instant getProcessStartDate() {
        return processStartDate;
    }

    public RecordSense processStartDate(Instant processStartDate) {
        this.processStartDate = processStartDate;
        return this;
    }

    public void setProcessStartDate(Instant processStartDate) {
        this.processStartDate = processStartDate;
    }

    public Instant getProcessFinishDate() {
        return processFinishDate;
    }

    public RecordSense processFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
        return this;
    }

    public void setProcessFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public RecordSense status(RecordStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public byte[] getAfid() {
        return afid;
    }

    public RecordSense afid(byte[] afid) {
        this.afid = afid;
        return this;
    }

    public void setAfid(byte[] afid) {
        this.afid = afid;
    }

    public String getAfidContentType() {
        return afidContentType;
    }

    public RecordSense afidContentType(String afidContentType) {
        this.afidContentType = afidContentType;
        return this;
    }

    public void setAfidContentType(String afidContentType) {
        this.afidContentType = afidContentType;
    }

    public Boolean isIsProcessed() {
        return isProcessed;
    }

    public RecordSense isProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
        return this;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public RecordSense similarity(Float similarity) {
        this.similarity = similarity;
        return this;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public Device getDevice() {
        return device;
    }

    public RecordSense device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Image getImage() {
        return image;
    }

    public RecordSense image(Image image) {
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
        RecordSense recordSense = (RecordSense) o;
        if (recordSense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recordSense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecordSense{" +
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
            ", isProcessed='" + isIsProcessed() + "'" +
            ", similarity=" + getSimilarity() +
            "}";
    }
}
