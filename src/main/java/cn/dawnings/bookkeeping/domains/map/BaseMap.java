package cn.dawnings.bookkeeping.domains.map;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMap {
    BaseMap INSTANCE = Mappers.getMapper(BaseMap.class);

}
