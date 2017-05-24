package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.StoreGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreGroup and its DTO StoreGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, })
public interface StoreGroupMapper extends EntityMapper <StoreGroupDTO, StoreGroup> {
    @Mapping(source = "organization.id", target = "organizationId")
    StoreGroupDTO toDto(StoreGroup storeGroup); 
    @Mapping(target = "stores", ignore = true)
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(target = "user", ignore = true)
    StoreGroup toEntity(StoreGroupDTO storeGroupDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default StoreGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreGroup storeGroup = new StoreGroup();
        storeGroup.setId(id);
        return storeGroup;
    }
}
