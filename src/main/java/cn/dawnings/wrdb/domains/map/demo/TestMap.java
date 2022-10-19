package cn.dawnings.wrdb.domains.map.demo;

import cn.dawnings.wrdb.domains.bo.TestPageBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoAddBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoPageBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.domains.entity.DemoDo;
import cn.dawnings.wrdb.domains.entity.TestDo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestMap {
    TestMap INSTANCE = Mappers.getMapper(TestMap.class);
    TestPageDto to(TestDo testDo);

}
