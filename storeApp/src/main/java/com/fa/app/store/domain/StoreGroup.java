package com.fa.app.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StoreGroup.
 */
@Entity
@Table(name = "store_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "storegroup")
public class StoreGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "site_url")
    private String siteUrl;

    @OneToMany(mappedBy = "storeGroup")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Store> stores = new HashSet<>();

    @ManyToOne
    private Organization organization;

    @OneToOne(mappedBy = "storeGroup")
    @JsonIgnore
    private BusinessUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public StoreGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public StoreGroup siteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
        return this;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Set<Store> getStores() {
        return stores;
    }

    public StoreGroup stores(Set<Store> stores) {
        this.stores = stores;
        return this;
    }

    public StoreGroup addStores(Store store) {
        this.stores.add(store);
        store.setStoreGroup(this);
        return this;
    }

    public StoreGroup removeStores(Store store) {
        this.stores.remove(store);
        store.setStoreGroup(null);
        return this;
    }

    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }

    public Organization getOrganization() {
        return organization;
    }

    public StoreGroup organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public BusinessUser getUser() {
        return user;
    }

    public StoreGroup user(BusinessUser businessUser) {
        this.user = businessUser;
        return this;
    }

    public void setUser(BusinessUser businessUser) {
        this.user = businessUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreGroup storeGroup = (StoreGroup) o;
        if (storeGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", siteUrl='" + getSiteUrl() + "'" +
            "}";
    }
}
