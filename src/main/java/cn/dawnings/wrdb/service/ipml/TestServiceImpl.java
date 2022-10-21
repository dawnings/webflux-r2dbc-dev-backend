package cn.dawnings.wrdb.service.ipml;

import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.bo.test.TestPageBo;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.domains.entity.DemoDo;
import cn.dawnings.wrdb.domains.entity.TestDo;
import cn.dawnings.wrdb.domains.map.test.TestMap;
import cn.dawnings.wrdb.r_repo.TestRepo;
import cn.dawnings.wrdb.service.TestService;
import cn.dawnings.wrdb.service.ipml.base.BaseServiceImpl;
import cn.dawnings.wrdb.utils.flux.LambdaCQ;
import cn.dawnings.wrdb.utils.flux.WebFluxUtils;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class TestServiceImpl extends BaseServiceImpl<TestDo, TestRepo> implements TestService {
    @Override//Flux<DemoPageDto>
    public Flux<TestPageDto> findAll() {
        return repo.findAll().map(TestMap.INSTANCE::to);
    }

    @Override
    public Flux<TestPageDto> page(TestPageBo bo) {
        val pageable = Pageable.ofSize(bo.getLength()).withPage(bo.getPage());
        return repo.findAllBy(bo, pageable).map(TestMap.INSTANCE::to);
    }

    @Override
    public Mono<Integer> count(TestPageBo bo) {
        return repo.countBy();
    }

    @Override
    public Mono<PageDto<TestPageDto>> page2(TestPageBo bo) {
        //存在疑问：为什么写的这么长，有没有缩减的可能性，若固定查询怎么处理,后面人使用该框架怎么便捷
        //有没有专门的分页  会不会有 LIMIT 这样的写法 start(页码)，pageSize(每页显示的条数)
        // 我现在遇到的LIMIT 是没办法实现分页的  这类方法会不会出现
        val queryName = Criteria.where("name").like("%" + bo.getNameLikeAll() + "%");
        val queryCount = r2dbcEntityTemplate.select(getDoClass()).from("t_demo_test")
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val result = r2dbcEntityTemplate.select(getDoClass()).from("t_demo_test")
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(TestMap.INSTANCE::to);
        return WebFluxUtils.page(queryCount, result, bo);
    }

    @Override
    public Mono<PageDto<TestPageDto>> page3(TestPageBo bo) {
        val queryName = LambdaCQ.where(DemoDo::getName).likeAll(StrUtil.isNotBlank(bo.getNameLikeAll()),
                bo.getNameLikeAll());
        val queryCount = getSelect()
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val result = getSelect()
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(TestMap.INSTANCE::to);

        return WebFluxUtils.page(queryCount, result, bo);
    }

    @Override
    public Mono<PageDto<TestPageDto>> page4(TestPageBo bo) {
        val queryName = LambdaCQ.where(DemoDo::getName).likeAll(StrUtil::isNotBlank,bo.getNameLikeAll());
        val count = getSelect()
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).count();
        val all = getSelect()
                .matching(Query.query(queryName).limit(bo.getLength()).offset(bo.getLengthLong() * (bo.getPage()))).all().map(TestMap.INSTANCE::to);

        return WebFluxUtils.page(count, all, bo);
    }



}
