package cn.dawnings.bookkeeping.service.ipml;

import cn.dawnings.bookkeeping.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.lang.reflect.ParameterizedType;

@EnableR2dbcRepositories
public abstract class BaseServiceImpl<Do, R extends ReactiveCrudRepository<Do, ?>> implements BaseService {
    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
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
