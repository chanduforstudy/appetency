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
 * A MenuCategory.
 */
@Entity
@Table(name = "menu_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "menucategory")
public class MenuCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuItem> items = new HashSet<>();

    @ManyToOne
    private Menu menu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MenuCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MenuItem> getItems() {
        return items;
    }

    public MenuCategory items(Set<MenuItem> menuItems) {
        this.items = menuItems;
        return this;
    }

    public MenuCategory addItems(MenuItem menuItem) {
        this.items.add(menuItem);
        menuItem.setCategory(this);
        return this;
    }

    public MenuCategory removeItems(MenuItem menuItem) {
        this.items.remove(menuItem);
        menuItem.setCategory(null);
        return this;
    }

    public void setItems(Set<MenuItem> menuItems) {
        this.items = menuItems;
    }

    public Menu getMenu() {
        return menu;
    }

    public MenuCategory menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuCategory menuCategory = (MenuCategory) o;
        if (menuCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), menuCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MenuCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
