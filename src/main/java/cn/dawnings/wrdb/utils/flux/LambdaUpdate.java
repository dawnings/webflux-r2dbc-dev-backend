/*
 * Copyright 2019-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dawnings.wrdb.utils.flux;

import cn.dawnings.wrdb.utils.FuncUtils;
import lombok.Getter;
import org.springframework.data.relational.core.query.Update;
import org.springframework.lang.Nullable;

import java.util.function.Function;

public class LambdaUpdate<Do> {
    @Getter
    private Update update;

    private static <Do> LambdaUpdate<Do> EMPTY() {
        return new LambdaUpdate<>();
    }

    public static <Do> LambdaUpdate<Do> update(FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        LambdaUpdate<Do> empty = EMPTY();
        empty.update = Update.update(FuncUtils.getFieldName(column), value);
        return empty;
    }

    public static <Do> LambdaUpdate<Do> update(boolean condition, FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        if (condition) {
            return update(column, value);
        } else {
            return EMPTY();
        }
    }

    public static <Do> LambdaUpdate<Do> update(Function<Object, Boolean> condition, FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        return update(condition.apply(value), column, value);
    }

    public LambdaUpdate<Do> set(FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        if (null == this.update) {
            this.update = Update.update(FuncUtils.getFieldName(column), value);
        } else {
            this.update.set(FuncUtils.getFieldName(column), value);
        }

        return this;
    }

    public LambdaUpdate<Do> set(boolean condition, FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        if (condition) {
            return set(column, value);
        } else {
            return this;
        }
    }

    public LambdaUpdate<Do> set(Function<Object, Boolean> condition, FuncUtils.SFunction<Do, ?> column, @Nullable Object value) {
        return set(condition.apply(value), column, value);
    }
}
