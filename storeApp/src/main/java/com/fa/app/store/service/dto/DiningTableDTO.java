package com.fa.app.store.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the DiningTable entity.
 */
public class DiningTableDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer number;

    @NotNull
    private String code;

    private Long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiningTableDTO diningTableDTO = (DiningTableDTO) o;
        if(diningTableDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diningTableDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiningTableDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
