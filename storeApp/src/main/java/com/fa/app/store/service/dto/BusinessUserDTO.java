package com.fa.app.store.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the BusinessUser entity.
 */
public class BusinessUserDTO implements Serializable {

    private Long id;

    private Long storeGroupId;

    private Long storeId;

    private String storeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreGroupId() {
        return storeGroupId;
    }

    public void setStoreGroupId(Long storeGroupId) {
        this.storeGroupId = storeGroupId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BusinessUserDTO businessUserDTO = (BusinessUserDTO) o;
        if(businessUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), businessUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusinessUserDTO{" +
            "id=" + getId() +
            "}";
    }
}
