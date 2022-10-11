package cn.dawnings.bookkeeping.r_repo;

import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DemoRepo extends ReactiveCrudRepository<DemoDo, Long> {
    Flux<DemoDo> findAllBy(Pageable pageable);
}
