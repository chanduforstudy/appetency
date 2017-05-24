package com.fa.app.store.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StoreGroup entity.
 */
public class StoreGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String siteUrl;

    private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoreGroupDTO storeGroupDTO = (StoreGroupDTO) o;
        if(storeGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", siteUrl='" + getSiteUrl() + "'" +
            "}";
    }
}
