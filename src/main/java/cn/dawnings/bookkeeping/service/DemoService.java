package cn.dawnings.bookkeeping.service;

import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import cn.dawnings.bookkeeping.service.base.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DemoService extends BaseService {
    Flux<DemoDo> findAll();

    Flux<DemoDo> page(DemoPageBo bo);

    Mono<Integer> count(DemoPageBo bo);

    Mono<PageDto<DemoDo>> page2(DemoPageBo bo);


    Mono<PageDto<DemoDo>> page3(DemoPageBo bo);
}
