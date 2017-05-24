package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.BusinessUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BusinessUser and its DTO BusinessUserDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreGroupMapper.class, StoreMapper.class, })
public interface BusinessUserMapper extends EntityMapper <BusinessUserDTO, BusinessUser> {
    @Mapping(source = "storeGroup.id", target = "storeGroupId")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    BusinessUserDTO toDto(BusinessUser businessUser); 
    @Mapping(source = "storeGroupId", target = "storeGroup")
    @Mapping(source = "storeId", target = "store")
    BusinessUser toEntity(BusinessUserDTO businessUserDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default BusinessUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUser businessUser = new BusinessUser();
        businessUser.setId(id);
        return businessUser;
    }
}
