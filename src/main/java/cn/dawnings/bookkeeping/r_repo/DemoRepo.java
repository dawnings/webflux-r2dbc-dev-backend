package cn.dawnings.bookkeeping.r_repo;

import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepo extends BaseRepo<DemoDo, Long> {

//    Flux<DemoDo> findAllBy(DemoPageBo bo, Pageable pageable);

//    @Override
//    Mono<Integer> countBy();
}
