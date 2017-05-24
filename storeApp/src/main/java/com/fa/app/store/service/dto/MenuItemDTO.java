package com.fa.app.store.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fa.app.store.domain.enumeration.Diet;

/**
 * A DTO for the MenuItem entity.
 */
public class MenuItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Integer preparationTime;

    private String ingredient;

    private String imageUrl;

    private String description;

    private Diet diet;

    private Long categoryId;

    private String categoryName;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long menuCategoryId) {
        this.categoryId = menuCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String menuCategoryName) {
        this.categoryName = menuCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuItemDTO menuItemDTO = (MenuItemDTO) o;
        if(menuItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), menuItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MenuItemDTO{" +
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
