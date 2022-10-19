package cn.dawnings.wrdb.service;

import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.bo.demo.*;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.service.base.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TestService extends BaseService {

    Flux<TestPageDto> findAll();
    Flux<TestPageDto> page(TestPageBo bo);
    Mono<Integer> count(TestPageBo bo);
    Mono<PageDto<TestPageDto>> page2(TestPageBo bo);

    Mono<PageDto<TestPageDto>> page3(TestPageBo bo);

    Mono<PageDto<TestPageDto>> page4(TestPageBo bo);
//

}
