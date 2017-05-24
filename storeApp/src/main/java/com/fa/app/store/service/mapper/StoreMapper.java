package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.StoreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Store and its DTO StoreDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, OrganizationMapper.class, StoreGroupMapper.class, })
public interface StoreMapper extends EntityMapper <StoreDTO, Store> {
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "storeGroup.id", target = "storeGroupId")
    StoreDTO toDto(Store store); 
    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "tables", ignore = true)
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "storeGroupId", target = "storeGroup")
    @Mapping(target = "menus", ignore = true)
    Store toEntity(StoreDTO storeDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Store fromId(Long id) {
        if (id == null) {
            return null;
        }
        Store store = new Store();
        store.setId(id);
        return store;
    }
}
