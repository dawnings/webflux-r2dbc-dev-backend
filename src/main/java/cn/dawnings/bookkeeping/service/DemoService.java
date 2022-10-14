package cn.dawnings.bookkeeping.service;

import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoAddBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoDeleteBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.bookkeeping.domains.dto.demo.DemoPageDto;
import cn.dawnings.bookkeeping.service.base.BaseService;
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
