package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.DiningTableDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DiningTable and its DTO DiningTableDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreMapper.class, })
public interface DiningTableMapper extends EntityMapper <DiningTableDTO, DiningTable> {
    @Mapping(source = "store.id", target = "storeId")
    DiningTableDTO toDto(DiningTable diningTable); 
    @Mapping(source = "storeId", target = "store")
    DiningTable toEntity(DiningTableDTO diningTableDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default DiningTable fromId(Long id) {
        if (id == null) {
            return null;
        }
        DiningTable diningTable = new DiningTable();
        diningTable.setId(id);
        return diningTable;
    }
}
