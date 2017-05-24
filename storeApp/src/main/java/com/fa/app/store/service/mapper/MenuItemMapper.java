package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.MenuItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MenuItem and its DTO MenuItemDTO.
 */
@Mapper(componentModel = "spring", uses = {MenuCategoryMapper.class, })
public interface MenuItemMapper extends EntityMapper <MenuItemDTO, MenuItem> {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    MenuItemDTO toDto(MenuItem menuItem); 
    @Mapping(source = "categoryId", target = "category")
    MenuItem toEntity(MenuItemDTO menuItemDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default MenuItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        return menuItem;
    }
}
