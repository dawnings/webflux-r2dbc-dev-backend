package cn.dawnings.bookkeeping.r_repo;

import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DemoRepo extends ReactiveCrudRepository<DemoDo, Long> {

    Flux<DemoDo> findAllBy(DemoPageBo bo, Pageable pageable);

    Mono<Integer> countBy();
}