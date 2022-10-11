package cn.dawnings.bookkeeping.web;

import cn.dawnings.bookkeeping.domains.base.Result;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.entity.DemoDo;
import cn.dawnings.bookkeeping.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoWeb {
    private final DemoService demoService;

    @GetMapping("/list")
    public Mono<Result<List<DemoDo>>> list() {
        return Result.ok(demoService.findAll());
    }
    @GetMapping("/page")
    public Flux<DemoDo> page(@Valid @RequestBody DemoPageBo bo) {
        return demoService.page(bo);
    }
//    @GetMapping("/add")
//    public Mono<DemoDo> add() {
//        return demoService.findAll();
//    }
}
