package cn.dawnings.bookkeeping.web.base;

import cn.dawnings.bookkeeping.domains.base.PageBo;
import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.service.base.BaseService;
import cn.dawnings.bookkeeping.utils.flux.WebFluxUtils;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public interface BaseWeb {
    default <DTO, B extends PageBo, Ser extends BaseService> Mono<PageDto<DTO>> pageByFunc(Ser service, B params
            , BiFunction<Ser, B, Mono<Integer>> countFunc, BiFunction<Ser, B, Flux<DTO>> listFunc) {
        val countRes = countFunc.apply(service, params);
        val listRes = WebFluxUtils.fluxPageCreate(listFunc.apply(service, params));
        return listRes.zipWith(countRes, (p, c) -> {
            p.setTotal(c);
            p.setPage(params.getPage());
            p.setLength(params.getLength());
            return p;
        });
    }

}
