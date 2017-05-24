package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.MenuCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MenuCategory and its DTO MenuCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {MenuMapper.class, })
public interface MenuCategoryMapper extends EntityMapper <MenuCategoryDTO, MenuCategory> {
    @Mapping(source = "menu.id", target = "menuId")
    @Mapping(source = "menu.name", target = "menuName")
    MenuCategoryDTO toDto(MenuCategory menuCategory); 
    @Mapping(target = "items", ignore = true)
    @Mapping(source = "menuId", target = "menu")
    MenuCategory toEntity(MenuCategoryDTO menuCategoryDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default MenuCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setId(id);
        return menuCategory;
    }
}
