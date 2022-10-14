package cn.dawnings.bookkeeping.domains.map.demo;

import cn.dawnings.bookkeeping.domains.bo.demo.DemoAddBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.bookkeeping.domains.dto.demo.DemoPageDto;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DemoMap {
    DemoMap INSTANCE = Mappers.getMapper(DemoMap.class);
    DemoDo to(DemoPageBo bo);

    DemoDo to(DemoAddBo bo);

    DemoPageDto to(DemoDo d);

    DemoDo to(DemoUpdateBo bo);
}
