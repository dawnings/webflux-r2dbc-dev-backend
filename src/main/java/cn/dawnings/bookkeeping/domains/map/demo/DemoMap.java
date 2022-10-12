package cn.dawnings.bookkeeping.domains.map.demo;

import cn.dawnings.bookkeeping.domains.bo.demo.DemoInsBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DemoMap {
    DemoMap INSTANCE = Mappers.getMapper(DemoMap.class);
    DemoDo to(DemoPageBo bo);

    DemoDo to(DemoInsBo bo);
}
