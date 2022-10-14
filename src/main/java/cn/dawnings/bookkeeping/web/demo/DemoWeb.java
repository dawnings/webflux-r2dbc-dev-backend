package cn.dawnings.bookkeeping.web.demo;

import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.base.Result;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoAddBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoDeleteBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoPageBo;
import cn.dawnings.bookkeeping.domains.bo.demo.DemoUpdateBo;
import cn.dawnings.bookkeeping.domains.dto.demo.DemoPageDto;
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

import static cn.dawnings.bookkeeping.utils.flux.WebFluxUtils.pageByFunc;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoWeb implements BaseWeb {
    private final DemoService demoService;

    @GetMapping("/list")
    public Mono<Result<List<DemoPageDto>>> list() {
        return Result.ok(demoService.findAll());
    }

    @GetMapping("/page")
    public Mono<Result<PageDto<DemoPageDto>>> page(@Valid @RequestBody DemoPageBo bo) {
        return Result.ok(pageByFunc(demoService, bo, DemoService::count, DemoService::page));
    }

    @GetMapping("/page2")
    public Mono<Result<PageDto<DemoPageDto>>> page2(@Valid @RequestBody DemoPageBo bo) {
        return Result.ok(demoService.page2(bo));
    }

    @GetMapping("/page3")
    public Mono<Result<PageDto<DemoPageDto>>> page3(@Valid @RequestBody DemoPageBo bo) {
        return Result.ok(demoService.page3(bo));
    }

    @GetMapping("/page4")
    public Mono<Result<PageDto<DemoPageDto>>> page4(@Valid @RequestBody DemoPageBo bo) {
        return Result.ok(demoService.page4(bo));
    }

    @GetMapping("/add")
    public Mono<DemoPageDto> add(@Valid @RequestBody DemoAddBo bo) {
        return demoService.add(bo);
    }

    @GetMapping("/delete")
    public Mono<Integer> delete(@Valid @RequestBody DemoDeleteBo bo) {
        return demoService.delete(bo);
    }

    @GetMapping("/update")
    public Mono<Integer> update(@Valid @RequestBody DemoUpdateBo bo) {
        return demoService.update(bo);
    }
}
