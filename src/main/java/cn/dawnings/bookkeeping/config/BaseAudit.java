package cn.dawnings.bookkeeping.config;

import cn.dawnings.bookkeeping.domains.base.BaseDo;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.auditing.ReactiveIsNewAwareAuditingHandler;
import org.springframework.data.r2dbc.mapping.event.ReactiveAuditingEntityCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class BaseAudit {

    @Component
    public static class BaseReactiveAuditingEntityCallback extends ReactiveAuditingEntityCallback {

        public BaseReactiveAuditingEntityCallback(ObjectFactory<ReactiveIsNewAwareAuditingHandler> auditingHandlerFactory) {
            super(auditingHandlerFactory);
        }

        @Override
        public Publisher<Object> onBeforeConvert(Object entity, SqlIdentifier table) {
            val d = (BaseDo) entity;
            val now = LocalDateTime.now();
            d.setOrgId(1L);
            d.setCreatedBy(0L);
            d.setModifiedBy(0L);
            d.setCreatedDate(now);
            d.setModifiedDate(now);
            return super.onBeforeConvert(d, table);
        }
    }
}
