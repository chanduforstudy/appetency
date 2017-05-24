package com.fa.app.store.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.fa.app.store.domain.enumeration.Diet;

/**
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "menuitem")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @Column(name = "ingredient")
    private String ingredient;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet")
    private Diet diet;

    @ManyToOne
    private MenuCategory category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MenuItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public MenuItem price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public MenuItem preparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
        return this;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getIngredient() {
        return ingredient;
    }

    public MenuItem ingredient(String ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MenuItem imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Diet getDiet() {
        return diet;
    }

    public MenuItem diet(Diet diet) {
        this.diet = diet;
        return this;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public MenuItem category(MenuCategory menuCategory) {
        this.category = menuCategory;
        return this;
    }

    public void setCategory(MenuCategory menuCategory) {
        this.category = menuCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItem menuItem = (MenuItem) o;
        if (menuItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), menuItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", preparationTime='" + getPreparationTime() + "'" +
            ", ingredient='" + getIngredient() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", diet='" + getDiet() + "'" +
            "}";
    }
}
