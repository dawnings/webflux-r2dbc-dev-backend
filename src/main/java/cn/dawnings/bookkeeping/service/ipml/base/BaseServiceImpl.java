package cn.dawnings.bookkeeping.service.ipml.base;

import cn.dawnings.bookkeeping.service.base.BaseService;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.*;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.lang.reflect.ParameterizedType;

@EnableR2dbcRepositories
public abstract class BaseServiceImpl<Do, R extends ReactiveCrudRepository<Do, ?>> implements BaseService {
    private final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    private String tableName;

    protected long getNextId() {
        return snowflake.nextId();
    }

    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    protected ReactiveSelectOperation.SelectWithProjection<Do> getSelect() {
        return r2dbcEntityTemplate.select(getDoClass()).from(getTableName());
    }

    protected ReactiveInsertOperation.TerminatingInsert<Do> getInsert() {
        return r2dbcEntityTemplate.insert(getDoClass()).into(getTableName());
    }

    protected ReactiveUpdateOperation.UpdateWithQuery getUpdate() {
        return r2dbcEntityTemplate.update(getDoClass()).inTable(getTableName());
    }

    protected ReactiveDeleteOperation.DeleteWithQuery getDelete() {
        return r2dbcEntityTemplate.delete(getDoClass()).from(getTableName());
    }

    private String getTableName() {
        if (tableName == null) {
            val table = AnnotationUtil.getAnnotation(getDoClass(), Table.class);
            return tableName = StrUtil.isBlankIfStr(table.value()) ? table.name() : table.value();
        }
        return tableName;
    }
    //    private TransactionalOperator operator;

    @Autowired
    @SuppressWarnings("all")
    public void setRepo(R repo) {
        this.repo = repo;
    }

    protected R repo;

    @SuppressWarnings("unchecked")
    public Class<Do> getDoClass() {
        return (Class<Do>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    public Do getDoInstance() {
        try {
            Class<Do> tClass = getDoClass();
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected R2dbcEntityTemplate r2dbcEntityTemplate;
}
