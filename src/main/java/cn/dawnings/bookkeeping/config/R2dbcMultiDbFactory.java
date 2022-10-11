//package cn.dawnings.bookkeeping.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//public class R2dbcMultiDbFactory extends AbstractRoutingConnectionFactory {
//    private final String defaultDbName;
//
//    @Override
//    protected Mono<Object> determineCurrentLookupKey() {
//        return null;
//    }
//}
