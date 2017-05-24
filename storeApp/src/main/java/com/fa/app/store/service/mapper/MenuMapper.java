package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.MenuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Menu and its DTO MenuDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class, })
public interface MenuMapper extends EntityMapper <MenuDTO, Menu> {
    @Mapping(source = "store.id", target = "storeId")
    MenuDTO toDto(Menu menu); 
    @Mapping(target = "categories", ignore = true)
    @Mapping(source = "storeId", target = "store")
    Menu toEntity(MenuDTO menuDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Menu fromId(Long id) {
        if (id == null) {
            return null;
        }
        Menu menu = new Menu();
        menu.setId(id);
        return menu;
    }
}
