package com.fa.app.store.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TableQR entity.
 */
public class TableQRDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private Long tableId;

    private String tableCode;

    private Long storeId;

    private String storeCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long diningTableId) {
        this.tableId = diningTableId;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String diningTableCode) {
        this.tableCode = diningTableCode;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableQRDTO tableQRDTO = (TableQRDTO) o;
        if(tableQRDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tableQRDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TableQRDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
