package cn.dawnings.wrdb.service;

import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.bo.demo.DemoAddBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoDeleteBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoPageBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.service.base.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DemoService extends BaseService {
    Flux<DemoPageDto> findAll();

    Flux<DemoPageDto> page(DemoPageBo bo);

    Mono<Integer> count(DemoPageBo bo);

    Mono<PageDto<DemoPageDto>> page2(DemoPageBo bo);


    Mono<PageDto<DemoPageDto>> page3(DemoPageBo bo);

    Mono<PageDto<DemoPageDto>> page4(DemoPageBo bo);

    Mono<DemoPageDto> add(DemoAddBo bo);

    Mono<Integer> delete(DemoDeleteBo bo);

    Mono<Integer> update(DemoUpdateBo bo);

    Mono<Integer> update2(DemoUpdateBo bo);

    Mono<Integer> update3(DemoUpdateBo bo);
}
