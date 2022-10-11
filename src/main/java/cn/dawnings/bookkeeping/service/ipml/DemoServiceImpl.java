package cn.dawnings.bookkeeping.service.ipml;

import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import cn.dawnings.bookkeeping.r_repo.DemoRepo;
import cn.dawnings.bookkeeping.service.DemoService;
import cn.dawnings.bookkeeping.service.ipml.base.BaseServiceImpl;
import cn.dawnings.bookkeeping.utils.flux.LambdaCQ;
import cn.dawnings.bookkeeping.utils.flux.WebFluxUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@EnableR2dbcRepositories
public class DemoServiceImpl extends BaseServiceImpl<DemoDo, DemoRepo> implements DemoService {

    @Override
    public Flux<DemoDo> findAll() {
        return repo.findAll();
    }

    @Override
    public Flux<DemoDo> page(DemoPageBo bo) {
        val pageable = Pageable.ofSize(bo.getLength()).withPage(bo.getPage());
        System.out.println(pageable);
        return repo.findAllBy(bo, pageable);
    }

    @Override
    public Mono<Integer> count(DemoPageBo bo) {
        return repo.countBy();
    }

    @Override
    public Mono<PageDto<DemoDo>> page2(DemoPageBo bo) {
        val name = Criteria.where("name").like("%" + bo.getNameLikeAll() + "%");
        val count = r2dbcEntityTemplate.select(getDoClass()).
                from("t_demo")
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = r2dbcEntityTemplate.select(getDoClass()).
                from("t_demo")
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all();

        return WebFluxUtils.page(count, all, bo);
    }

    @Override
    public Mono<PageDto<DemoDo>> page3(DemoPageBo bo) {
        val name = LambdaCQ.where(DemoDo::getName).likeAll(bo.getNameLikeAll());
        val count = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all();

        return WebFluxUtils.page(count, all, bo);
    }


}
