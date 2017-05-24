package com.fa.app.store.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Organization entity.
 */
public class OrganizationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String owner;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationDTO organizationDTO = (OrganizationDTO) o;
        if(organizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
