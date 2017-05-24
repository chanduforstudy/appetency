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
 * A Menu.
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "menu")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuCategory> categories = new HashSet<>();

    @ManyToOne
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Menu name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MenuCategory> getCategories() {
        return categories;
    }

    public Menu categories(Set<MenuCategory> menuCategories) {
        this.categories = menuCategories;
        return this;
    }

    public Menu addCategories(MenuCategory menuCategory) {
        this.categories.add(menuCategory);
        menuCategory.setMenu(this);
        return this;
    }

    public Menu removeCategories(MenuCategory menuCategory) {
        this.categories.remove(menuCategory);
        menuCategory.setMenu(null);
        return this;
    }

    public void setCategories(Set<MenuCategory> menuCategories) {
        this.categories = menuCategories;
    }

    public Store getStore() {
        return store;
    }

    public Menu store(Store store) {
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
        Menu menu = (Menu) o;
        if (menu.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
