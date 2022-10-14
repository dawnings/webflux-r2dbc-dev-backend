package cn.dawnings.wrdb.domains.map.demo;

import cn.dawnings.wrdb.domains.bo.demo.DemoAddBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoPageBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.domains.entity.DemoDo;
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
