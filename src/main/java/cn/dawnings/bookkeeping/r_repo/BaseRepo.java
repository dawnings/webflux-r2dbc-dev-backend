package cn.dawnings.bookkeeping.r_repo;

import cn.dawnings.bookkeeping.domains.base.PageBo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseRepo<T, ID> extends ReactiveCrudRepository<T, ID> {

    <PB extends PageBo> Flux<T> findAllBy(PB bo, Pageable pageable);

    Mono<Integer> countBy();
}
