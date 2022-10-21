package cn.dawnings.wrdb.domains.map.test;

import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.domains.entity.TestDo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestMap {
    TestMap INSTANCE = Mappers.getMapper(TestMap.class);
    TestPageDto to(TestDo testDo);

}
