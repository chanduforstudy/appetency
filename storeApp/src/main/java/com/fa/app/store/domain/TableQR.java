package com.fa.app.store.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TableQR.
 */
@Entity
@Table(name = "table_qr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tableqr")
public class TableQR implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @OneToOne
    @JoinColumn(unique = true)
    private DiningTable table;

    @OneToOne
    @JoinColumn(unique = true)
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public TableQR code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiningTable getTable() {
        return table;
    }

    public TableQR table(DiningTable diningTable) {
        this.table = diningTable;
        return this;
    }

    public void setTable(DiningTable diningTable) {
        this.table = diningTable;
    }

    public Store getStore() {
        return store;
    }

    public TableQR store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableQR tableQR = (TableQR) o;
        if (tableQR.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tableQR.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TableQR{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
