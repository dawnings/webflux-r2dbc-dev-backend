package cn.dawnings.bookkeeping.domains.base;

import cn.dawnings.bookkeeping.domains.constant.StatusConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 返回包装类.
 *
 * @param <T> the type parameter
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Result<T> extends ResultEMPTY {
    private static <T> Mono<Result<T>> monoResultCreate(Mono<T> monoData, int code, String msg) {
        return monoData.map(data -> {
            final Result<T> result = new Result<>();
            result.setCode(code);
            result.setData(data);
            result.setMessage(msg);
            return result;
        });
    }

    private static <T> Mono<Result<List<T>>> fluxResultCreate(Flux<T> fluxbody, int code, String msg) {
        return monoResultCreate(fluxbody.buffer().single(), code, msg);
    }

    /**
     * 数据.
     */
    private T data;


    public static <T> Mono<Result<T>> ok(Mono<T> monoBody, int code, String msg) {
        return monoResultCreate(monoBody, code, msg);
    }

    public static <T> Mono<Result<T>> ok(Mono<T> monoBody, String msg) {
        return monoResultCreate(monoBody, StatusConstant.REQUEST_SUCCESS_CODE, msg);
    }

    public static <T> Mono<Result<T>> ok(Mono<T> monoBody) {
        return monoResultCreate(monoBody, StatusConstant.REQUEST_SUCCESS_CODE, "操作成功!");
    }

    public static <T> Mono<Result<List<T>>> ok(Flux<T> fluxBody, String msg) {
        return fluxResultCreate(fluxBody, StatusConstant.REQUEST_SUCCESS_CODE, msg);
    }

    public static <T> Mono<Result<List<T>>> ok(Flux<T> fluxBody) {
        return fluxResultCreate(fluxBody, StatusConstant.REQUEST_SUCCESS_CODE, "操作成功!");
    }


//    public static <T> Mono<Result<T>> error(Mono<T> monoBody, String msg) {
//        return monoResultCreate(monoBody, StatusConstant.REQUEST_ERROR_CODE, msg);
//    }
//
    public static <T> Mono<Result<T>> error(Mono<T> monoBody) {
        return monoResultCreate(monoBody, StatusConstant.REQUEST_ERROR_CODE, "操作失败!");
    }
}
