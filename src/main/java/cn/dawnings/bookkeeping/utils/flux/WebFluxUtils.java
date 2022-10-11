package cn.dawnings.bookkeeping.utils.flux;

import cn.dawnings.bookkeeping.domains.base.PageBo;
import cn.dawnings.bookkeeping.domains.base.PageDto;
import cn.dawnings.bookkeeping.domains.base.Result;
import cn.dawnings.bookkeeping.service.base.BaseService;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;

public class WebFluxUtils {
    public static <T> Mono<Result<PageDto<T>>> monoResultPageCreate(Mono<List<T>> monoData, int code, String msg) {
        return monoData.map(data -> {
            final Result<PageDto<T>> result = new Result<>();
            final PageDto<T> pageDto = new PageDto<>();
            pageDto.setData(data);
            result.setCode(code);
            result.setData(pageDto);
            result.setMessage(msg);
            return result;
        });
    }

    public static <DTO, B extends PageBo, Ser extends BaseService> Mono<PageDto<DTO>> pageByFunc(Ser service, B params
            , BiFunction<Ser, B, Mono<Integer>> countFunc, BiFunction<Ser, B, Flux<DTO>> listFunc) {
        val countRes = countFunc.apply(service, params);
        val listRes = WebFluxUtils.fluxPageCreate(listFunc.apply(service, params));
        return listRes.zipWith(countRes, (p, c) -> {
            p.setTotal(c);
            p.setRecords(p.getData().size());
            p.setPage(params.getPage());
            p.setLength(params.getLength());
            return p;
        });
    }

    public static <DTO, B extends PageBo> Mono<PageDto<DTO>> page(Mono<Long> countRes, Flux<DTO> listFlux, B params
    ) {
        val listRes = WebFluxUtils.fluxPageCreate(listFlux);
        return listRes.zipWith(countRes, (p, c) -> {
            p.setTotal(c.intValue());
            p.setRecords(p.getData().size());
            p.setPage(params.getPage());
            p.setLength(params.getLength());
            return p;
        });
    }

    public static <T> Mono<PageDto<T>> monoPageCreate(Mono<List<T>> monoData) {
        return monoData.map(data -> {
            final PageDto<T> pageDto = new PageDto<>();
            pageDto.setData(data);
            return pageDto;
        });
    }

    public static <DTO> Mono<PageDto<DTO>> fluxPageCreate(Flux<DTO> fluxbody) {
        return monoPageCreate(fluxbody.buffer().single());
    }

    public static <T> Mono<Result<PageDto<T>>> fluxResultPageCreate(Flux<T> fluxbody, int code, String msg) {
        return monoResultPageCreate(fluxbody.buffer().single(), code, msg);
    }


    public static <T> Mono<Result<T>> monoResultCreate(Mono<T> monoData, int code, String msg) {
        return monoData.map(data -> {
            final Result<T> result = new Result<>();
            result.setCode(code);
            result.setData(data);
            result.setMessage(msg);
            return result;
        });
    }

    public static <T> Mono<Result<List<T>>> fluxResultCreate(Flux<T> fluxbody, int code, String msg) {
        return monoResultCreate(fluxbody.buffer().single(), code, msg);
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(Pageable pageable, List<T> list, Long totalNumber, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(totalNumber));
        String link = "";
        int totalPages = Math.toIntExact(totalNumber / pageable.getPageSize() + 1);
        if ((pageable.getPageNumber() + 1) < totalPages) {
            link = "<" + generateUri(baseUrl, pageable.getPageNumber() + 1, list.size()) + ">; rel=\"next\",";
        }
        // prev link
        if ((pageable.getPageNumber()) > 0) {
            link += "<" + generateUri(baseUrl, pageable.getPageNumber() - 1, list.size()) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (totalPages > 0) {
            lastPage = totalPages - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, list.size()) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, list.size()) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
    }


}
