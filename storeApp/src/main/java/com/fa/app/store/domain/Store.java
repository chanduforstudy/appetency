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
 * A Store.
 */
@Entity
@Table(name = "store")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "timings")
    private String timings;

    @Column(name = "website")
    private String website;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "fax_number")
    private String faxNumber;

    @Column(name = "site_url")
    private String siteUrl;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DiningTable> tables = new HashSet<>();

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private StoreGroup storeGroup;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Menu> menus = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Store code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Store name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimings() {
        return timings;
    }

    public Store timings(String timings) {
        this.timings = timings;
        return this;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getWebsite() {
        return website;
    }

    public Store website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public Store email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Store phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Store mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public Store faxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
        return this;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public Store siteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
        return this;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Location getLocation() {
        return location;
    }

    public Store location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<DiningTable> getTables() {
        return tables;
    }

    public Store tables(Set<DiningTable> diningTables) {
        this.tables = diningTables;
        return this;
    }

    public Store addTables(DiningTable diningTable) {
        this.tables.add(diningTable);
        diningTable.setStore(this);
        return this;
    }

    public Store removeTables(DiningTable diningTable) {
        this.tables.remove(diningTable);
        diningTable.setStore(null);
        return this;
    }

    public void setTables(Set<DiningTable> diningTables) {
        this.tables = diningTables;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Store organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public StoreGroup getStoreGroup() {
        return storeGroup;
    }

    public Store storeGroup(StoreGroup storeGroup) {
        this.storeGroup = storeGroup;
        return this;
    }

    public void setStoreGroup(StoreGroup storeGroup) {
        this.storeGroup = storeGroup;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public Store menus(Set<Menu> menus) {
        this.menus = menus;
        return this;
    }

    public Store addMenus(Menu menu) {
        this.menus.add(menu);
        menu.setStore(this);
        return this;
    }

    public Store removeMenus(Menu menu) {
        this.menus.remove(menu);
        menu.setStore(null);
        return this;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Store store = (Store) o;
        if (store.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", timings='" + getTimings() + "'" +
            ", website='" + getWebsite() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", faxNumber='" + getFaxNumber() + "'" +
            ", siteUrl='" + getSiteUrl() + "'" +
            "}";
    }
}
