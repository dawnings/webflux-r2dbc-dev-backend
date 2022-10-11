package cn.dawnings.bookkeeping.web.demo;

import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.base.Result;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import cn.dawnings.bookkeeping.service.DemoService;
import cn.dawnings.bookkeeping.web.base.BaseWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoWeb implements BaseWeb {
    private final DemoService demoService;

    @GetMapping("/list")
    public Mono<Result<List<DemoDo>>> list() {
        return Result.ok(demoService.findAll());
    }

    @GetMapping("/page")
    public Mono<Result<PageDto<DemoDo>>> page(@Valid @RequestBody DemoPageBo bo) {
        return Result.ok(pageByFunc(demoService, bo, DemoService::count, DemoService::page));


    }
//    @GetMapping("/add")
//    public Mono<DemoDo> add() {
//        return demoService.findAll();
//    }
}
