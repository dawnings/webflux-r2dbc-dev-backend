package cn.dawnings.wrdb.r_repo;

import cn.dawnings.wrdb.domains.entity.DemoDo;
import cn.dawnings.wrdb.domains.entity.TestDo;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends BaseRepo<TestDo, Long> {

//    Flux<DemoDo> findAllBy(DemoPageBo bo, Pageable pageable);

//    @Override
//    Mono<Integer> countBy();
}
