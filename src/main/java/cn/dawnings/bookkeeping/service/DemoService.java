package cn.dawnings.bookkeeping.service;

import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import reactor.core.publisher.Flux;

public interface DemoService {
    Flux<DemoDo> findAll();

    Flux<DemoDo> page(DemoPageBo bo);
}
