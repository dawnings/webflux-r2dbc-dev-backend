package cn.dawnings.wrdb.web.test;


import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.base.Result;
import cn.dawnings.wrdb.domains.bo.demo.DemoPageBo;
import cn.dawnings.wrdb.domains.bo.test.TestPageBo;
import cn.dawnings.wrdb.domains.dto.demo.DemoPageDto;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.service.DemoService;
import cn.dawnings.wrdb.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static cn.dawnings.wrdb.utils.flux.WebFluxUtils.pageByFunc;

/**
 * @author cyp
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestWeb {

    private final TestService testService;


    @GetMapping("/list")
    public Mono<Result<List<TestPageDto>>> list() {
        return Result.ok(testService.findAll());
    }

    @GetMapping("/page")
    public Mono<Result<PageDto<TestPageDto>>> page(@Valid @RequestBody TestPageBo bo) {
        return Result.ok(pageByFunc(testService, bo, TestService::count, TestService::page));
    }

    @GetMapping("/page2")
    public Mono<Result<PageDto<TestPageDto>>> page2(@Valid @RequestBody TestPageBo bo) {
        return Result.ok(testService.page2(bo));
    }

    @GetMapping("/page3")
    public Mono<Result<PageDto<TestPageDto>>> page3(@Valid @RequestBody TestPageBo bo) {
        return Result.ok(testService.page3(bo));
    }

    @GetMapping("/page4")
    public Mono<Result<PageDto<TestPageDto>>> page4(@Valid @RequestBody TestPageBo bo) {
        return Result.ok(testService.page4(bo));
    }


}
