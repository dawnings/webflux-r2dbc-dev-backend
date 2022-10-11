package cn.dawnings.bookkeeping.service.ipml;

import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import cn.dawnings.bookkeeping.r_repo.DemoRepo;
import cn.dawnings.bookkeeping.service.DemoService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@EnableR2dbcRepositories
public class DemoServiceImpl implements DemoService {
    private final DemoRepo demoRepo;
//    private final TransactionalOperator operator;

    @Override
    public Flux<DemoDo> findAll() {
        return demoRepo.findAll();
    }

    @Override
    public Flux<DemoDo> page(DemoPageBo bo) {
        val pageable = Pageable.ofSize(bo.getLength()).withPage(bo.getPage());
        System.out.println(pageable);
        return demoRepo.findAllBy(pageable);
    }

    @Override
    public Mono<Integer> count(DemoPageBo bo) {
        return demoRepo.countBy();
    }
}
