package com.fa.app.store.service.mapper;

import com.fa.app.store.domain.*;
import com.fa.app.store.service.dto.TableQRDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TableQR and its DTO TableQRDTO.
 */
@Mapper(componentModel = "spring", uses = {DiningTableMapper.class, StoreMapper.class, })
public interface TableQRMapper extends EntityMapper <TableQRDTO, TableQR> {
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.code", target = "tableCode")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.code", target = "storeCode")
    TableQRDTO toDto(TableQR tableQR); 
    @Mapping(source = "tableId", target = "table")
    @Mapping(source = "storeId", target = "store")
    TableQR toEntity(TableQRDTO tableQRDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default TableQR fromId(Long id) {
        if (id == null) {
            return null;
        }
        TableQR tableQR = new TableQR();
        tableQR.setId(id);
        return tableQR;
    }
}
