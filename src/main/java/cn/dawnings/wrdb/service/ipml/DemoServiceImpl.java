package cn.dawnings.wrdb.service.ipml;

import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.bo.demo.DemoAddBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoDeleteBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoPageBo;
import cn.dawnings.wrdb.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.domains.entity.DemoDo;
import cn.dawnings.wrdb.domains.map.demo.DemoMap;
import cn.dawnings.wrdb.r_repo.DemoRepo;
import cn.dawnings.wrdb.service.DemoService;
import cn.dawnings.wrdb.service.ipml.base.BaseServiceImpl;
import cn.dawnings.wrdb.utils.flux.LambdaCQ;
import cn.dawnings.wrdb.utils.flux.LambdaUpdate;
import cn.dawnings.wrdb.utils.flux.WebFluxUtils;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DemoServiceImpl extends BaseServiceImpl<DemoDo, DemoRepo> implements DemoService {

    @Override
    public Flux<DemoPageDto> findAll() {
        return repo.findAll().map(DemoMap.INSTANCE::to);
    }

    @Override
    public Flux<DemoPageDto> page(DemoPageBo bo) {
        val pageable = Pageable.ofSize(bo.getLength()).withPage(bo.getPage());
        System.out.println(pageable);
        return repo.findAllBy(bo, pageable).map(DemoMap.INSTANCE::to);
    }

    @Override
    public Mono<Integer> count(DemoPageBo bo) {
        return repo.countBy();
    }

    @Override
    public Mono<PageDto<DemoPageDto>> page2(DemoPageBo bo) {
        val name = Criteria.where("name").like("%" + bo.getNameLikeAll() + "%");
        val count = r2dbcEntityTemplate.select(getDoClass()).
                from("t_demo")
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = r2dbcEntityTemplate.select(getDoClass()).
                from("t_demo")
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(DemoMap.INSTANCE::to);

        return WebFluxUtils.page(count, all, bo);
    }

    @Override
    public Mono<PageDto<DemoPageDto>> page3(DemoPageBo bo) {
        val name = LambdaCQ.where(DemoDo::getName).likeAll(StrUtil.isNotBlank(bo.getNameLikeAll()),
                bo.getNameLikeAll());
        val count = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(DemoMap.INSTANCE::to);

        return WebFluxUtils.page(count, all, bo);
    }

    @Override
    public Mono<PageDto<DemoPageDto>> page4(DemoPageBo bo) {
        val name = LambdaCQ.where(DemoDo::getName).likeAll(StrUtil::isNotBlank,
                bo.getNameLikeAll()
//        ).and(LambdaCQ.where(DemoDo::getName).likeAll(StrUtil::isNotBlank,"2")
        );
        val count = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = getSelect()
                .matching(Query.query(name).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(DemoMap.INSTANCE::to);

        return WebFluxUtils.page(count, all, bo);
    }

    @Override
    public Mono<DemoPageDto> add(DemoAddBo bo) {
        val addto = DemoMap.INSTANCE.to(bo);
        addto.setId(getNextId());
        return getInsert().using(addto).map(DemoMap.INSTANCE::to);
    }

    @Override
    public Mono<Integer> delete(DemoDeleteBo bo) {
        val name = LambdaCQ.where(DemoDo::getId).is(bo.getId());
        return getDelete().matching(Query.query(name)).all();
    }

    @Override
    public Mono<Integer> update(DemoUpdateBo bo) {
        val name = LambdaCQ.where(DemoDo::getId).is(bo.getId());
        return getUpdate().matching(Query.query(name)).apply(Update.update("name", bo.getName()));
    }

    @Override
    public Mono<Integer> update2(DemoUpdateBo bo) {
        val name = LambdaCQ.where(DemoDo::getId).is(bo.getId());
        return getUpdate().matching(Query.query(name)).apply(LambdaUpdate.update(DemoDo::getName, bo.getName()).getUpdate());
    }

    @Override
    public Mono<Integer> update3(DemoUpdateBo bo) {
        val name = LambdaCQ.where(DemoDo::getId).is(bo.getId());
        return getUpdate().matching(Query.query(name)).apply(LambdaUpdate.update(DemoDo::getName, bo.getName()).getUpdate());
    }
}
