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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

/**
 * Central class for creating queries. It follows a fluent API style so that you can easily chain together multiple
 * LambdaCQ. Static import of the {@code LambdaCQ.property(…)} method will improve readability as in
 * {@code where(property(…).is(…)}.
 * <p>
 * The LambdaCQ API supports composition with a {@link #empty() NULL object} and a {@link #from(List) static factory
 * method}. Example usage:
 * <pre class="code">
 * LambdaCQ.from(LambdaCQ.where("name").is("Foo"), LambdaCQ.from(LambdaCQ.where("age").greaterThan(42)));
 * </pre>
 * <p>
 * rendering:
 * <pre class="code">
 * WHERE name = 'Foo' AND age > 42
 * </pre>
 *
 * @author Mark Paluch
 * @author Oliver Drotbohm
 * @author Roman Chigvintsev
 * @author Jens Schauder
 * @author fanchen
 * @since 2.0
 */
@SuppressWarnings("unused")
public class LambdaCQ<Do> implements CriteriaDefinition {
    @SuppressWarnings("all")

    private static <Do> LambdaCQ<Do> EMPTY() {
        return new LambdaCQ<>(SqlIdentifier.EMPTY, Comparator.INITIAL, null);
    }

    private final @Nullable LambdaCQ<Do> previous;
    private final Combinator combinator;
    private final List<CriteriaDefinition> group;

    private final @Nullable SqlIdentifier column;
    private final @Nullable Comparator comparator;
    private final @Nullable Object value;
    private final boolean ignoreCase;

    private LambdaCQ(SqlIdentifier column, Comparator comparator, @Nullable Object value) {
        this(null, Combinator.INITIAL, Collections.emptyList(), column, comparator, value, false);
    }

    private LambdaCQ(@Nullable LambdaCQ<Do> previous, Combinator combinator, List<CriteriaDefinition> group,
                     @Nullable SqlIdentifier column, @Nullable Comparator comparator, @Nullable Object value) {
        this(previous, combinator, group, column, comparator, value, false);
    }

    private LambdaCQ(@Nullable LambdaCQ<Do> previous, Combinator combinator, List<CriteriaDefinition> group,
                     @Nullable SqlIdentifier column, @Nullable Comparator comparator, @Nullable Object value, boolean ignoreCase) {

        this.previous = previous;
        this.combinator = previous != null && previous.isEmpty() ? Combinator.INITIAL : combinator;
        this.group = group;
        this.column = column;
        this.comparator = comparator;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    private LambdaCQ(@Nullable LambdaCQ<Do> previous, Combinator combinator, List<CriteriaDefinition> group) {

        this.previous = previous;
        this.combinator = previous != null && previous.isEmpty() ? Combinator.INITIAL : combinator;
        this.group = group;
        this.column = null;
        this.comparator = null;
        this.value = null;
        this.ignoreCase = false;
    }

    /**
     * Static factory method to create an empty LambdaCQ.
     *
     * @return an empty {@link LambdaCQ}.
     */
    public static <Do> LambdaCQ<Do> empty() {
        return EMPTY();
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code AND} using the provided {@link List LambdaCQs}.
     *
     * @return new {@link LambdaCQ}.
     */
    @SafeVarargs
    public static <Do> LambdaCQ<Do> from(LambdaCQ<Do>... lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null");
        Assert.noNullElements(lambdaCQ, "LambdaCQ must not contain null elements");

        return from(Arrays.asList(lambdaCQ));
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code AND} using the provided {@link List LambdaCQs}.
     *
     * @return new {@link LambdaCQ}.
     */
    public static <Do> LambdaCQ<Do> from(List<LambdaCQ<Do>> lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null");
        Assert.noNullElements(lambdaCQ, "LambdaCQ must not contain null elements");

        if (lambdaCQ.isEmpty()) {
            return EMPTY();
        }

        if (lambdaCQ.size() == 1) {
            return lambdaCQ.get(0);
        }
        LambdaCQ<Do> empty = EMPTY();
        return empty.and(lambdaCQ);
    }

    /**
     * Static factory method to create a LambdaCQ using the provided {@code column} name.
     *
     * @param column Must not be {@literal null} or empty.
     * @return a new {@link LambdaCQStep} object to complete the first {@link LambdaCQ}.
     */
    public static <Do> LambdaCQStep<Do> where(FuncUtils.SFunction<Do, ?> column) {
        return new DefaultLambdaCQStep<>(SqlIdentifier.unquoted(FuncUtils.getFieldName(column)));
    }

    /**
     * Create a new {@link LambdaCQ} and combine it with {@code AND} using the provided {@code column} name.
     *
     * @param column Must not be {@literal null} or empty.
     * @return a new {@link LambdaCQStep} object to complete the next {@link LambdaCQ}.
     */
    public LambdaCQStep<Do> and(FuncUtils.SFunction<Do, ?> column) {

        SqlIdentifier identifier = SqlIdentifier.unquoted(FuncUtils.getFieldName(column));
        return new DefaultLambdaCQStep<>(identifier) {
            @Override
            protected LambdaCQ<Do> createLambdaCQ(Comparator comparator, @Nullable Object value) {
                return new LambdaCQ<>(LambdaCQ.this, Combinator.AND, Collections.emptyList(), identifier, comparator, value);
            }
        };
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code AND} using the provided {@link LambdaCQ} group.
     *
     * @param lambdaCQ LambdaCQ object.
     * @return a new {@link LambdaCQ} object.
     * @since 1.1
     */
    public LambdaCQ<Do> and(CriteriaDefinition lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null!");

        return and(Collections.singletonList(lambdaCQ));
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code AND} using the provided {@link LambdaCQ} group.
     *
     * @param lambdaCQ LambdaCQ objects.
     * @return a new {@link LambdaCQ} object.
     */
    @SuppressWarnings("unchecked")

    public LambdaCQ<Do> and(List<? extends CriteriaDefinition> lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null!");

        return new LambdaCQ<>(LambdaCQ.this, Combinator.AND, (List<CriteriaDefinition>) lambdaCQ);
    }

    /**
     * Create a new {@link LambdaCQ} and combine it with {@code OR} using the provided {@code column} name.
     *
     * @param column Must not be {@literal null} or empty.
     * @return a new {@link LambdaCQStep} object to complete the next {@link LambdaCQ}.
     */
    public LambdaCQStep<Do> or(FuncUtils.SFunction<Do, ?> column) {


        SqlIdentifier identifier = SqlIdentifier.unquoted(FuncUtils.getFieldName(column));
        return new DefaultLambdaCQStep<>(identifier) {
            @Override
            protected LambdaCQ<Do> createLambdaCQ(Comparator comparator, @Nullable Object value) {
                return new LambdaCQ<>(LambdaCQ.this, Combinator.OR, Collections.emptyList(), identifier, comparator, value);
            }
        };
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code OR} using the provided {@link LambdaCQ} group.
     *
     * @param lambdaCQ LambdaCQ object.
     * @return a new {@link LambdaCQ} object.
     * @since 1.1
     */
    public LambdaCQ<Do> or(CriteriaDefinition lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null!");

        return or(Collections.singletonList(lambdaCQ));
    }

    /**
     * Create a new {@link LambdaCQ} and combine it as group with {@code OR} using the provided {@link LambdaCQ} group.
     *
     * @param lambdaCQ LambdaCQ object.
     * @return a new {@link LambdaCQ} object.
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public LambdaCQ<Do> or(List<? extends CriteriaDefinition> lambdaCQ) {

        Assert.notNull(lambdaCQ, "LambdaCQ must not be null!");

        return new LambdaCQ<>(LambdaCQ.this, Combinator.OR, (List<CriteriaDefinition>) lambdaCQ);
    }

    /**
     * Creates a new {@link LambdaCQ} with the given "ignore case" flag.
     *
     * @param ignoreCase {@literal true} if comparison should be done in case-insensitive way
     * @return a new {@link LambdaCQ} object
     */
    public LambdaCQ<Do> ignoreCase(boolean ignoreCase) {
        if (this.ignoreCase != ignoreCase) {
            return new LambdaCQ<>(previous, combinator, group, column, comparator, value, ignoreCase);
        }
        return this;
    }

    /**
     * @return the previous {@link LambdaCQ} object. Can be {@literal null} if there is no previous {@link LambdaCQ}.
     * @see #hasPrevious()
     */
    @Nullable
    @Override
    public LambdaCQ<Do> getPrevious() {
        return previous;
    }

    /**
     * @return {@literal true} if this {@link LambdaCQ} has a previous one.
     */
    @Override
    public boolean hasPrevious() {
        return previous != null;
    }

    /**
     * @return {@literal true} if this {@link LambdaCQ} is empty.
     * @since 1.1
     */
    @Override
    public boolean isEmpty() {

        if (doIsEmpty()) {
            return false;
        }

        LambdaCQ<Do> parent = this.previous;

        while (parent != null) {

            if (parent.doIsEmpty()) {
                return false;
            }

            parent = parent.previous;
        }

        return true;
    }

    private boolean doIsEmpty() {

        if (this.comparator == Comparator.INITIAL) {
            return false;
        }

        if (this.column != null) {
            return true;
        }

        for (CriteriaDefinition LambdaCQ : group) {

            if (!LambdaCQ.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return {@literal true} if this {@link LambdaCQ} is empty.
     */
    @Override
    public boolean isGroup() {
        return !this.group.isEmpty();
    }

    /**
     * @return {@link Combinator} to combine this LambdaCQ with a previous one.
     */
    @Override
    public Combinator getCombinator() {
        return combinator;
    }

    @Override
    public List<CriteriaDefinition> getGroup() {
        return group;
    }

    /**
     * @return the column/property name.
     */
    @Override
    @Nullable
    public SqlIdentifier getColumn() {
        return column;
    }

    /**
     * @return {@link Comparator}.
     */
    @Override
    @Nullable
    public Comparator getComparator() {
        return comparator;
    }

    /**
     * @return the comparison value. Can be {@literal null}.
     */
    @Override
    @Nullable
    public Object getValue() {
        return value;
    }

    /**
     * Checks whether comparison should be done in case-insensitive way.
     *
     * @return {@literal true} if comparison should be done in case-insensitive way
     */
    @Override
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    @Override
    public String toString() {

        if (isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        unroll(this, builder);

        return builder.toString();
    }

    private void unroll(CriteriaDefinition lambdaCQ, StringBuilder stringBuilder) {

        CriteriaDefinition current = lambdaCQ;

        // reverse unroll LambdaCQ chain
        Map<CriteriaDefinition, CriteriaDefinition> forwardChain = new HashMap<>();

        while (true) {
            assert current != null;
            if (!current.hasPrevious()) {
                break;
            }
            forwardChain.put(current.getPrevious(), current);
            current = current.getPrevious();
        }

        // perform the actual mapping
        render(current, stringBuilder);
        while (forwardChain.containsKey(current)) {

            CriteriaDefinition criterion = forwardChain.get(current);

            if (criterion.getCombinator() != Combinator.INITIAL) {
                stringBuilder.append(' ').append(criterion.getCombinator().name()).append(' ');
            }

            render(criterion, stringBuilder);

            current = criterion;
        }
    }

    private void unrollGroup(List<? extends CriteriaDefinition> lambdaCQ, StringBuilder stringBuilder) {

        stringBuilder.append("(");

        boolean first = true;
        for (CriteriaDefinition criterion : lambdaCQ) {

            if (criterion.isEmpty()) {
                continue;
            }

            if (!first) {
                Combinator combinator = criterion.getCombinator() == Combinator.INITIAL ? Combinator.AND
                        : criterion.getCombinator();
                stringBuilder.append(' ').append(combinator.name()).append(' ');
            }

            unroll(criterion, stringBuilder);
            first = false;
        }

        stringBuilder.append(")");
    }

    private void render(CriteriaDefinition lambdaCQ, StringBuilder stringBuilder) {

        if (lambdaCQ.isEmpty()) {
            return;
        }

        if (lambdaCQ.isGroup()) {
            unrollGroup(lambdaCQ.getGroup(), stringBuilder);
            return;
        }

        stringBuilder.append(Objects.requireNonNull(lambdaCQ.getColumn()).toSql(IdentifierProcessing.NONE)).append(' ')
                .append(Objects.requireNonNull(lambdaCQ.getComparator()).getComparator());

        switch (lambdaCQ.getComparator()) {
            case BETWEEN:
            case NOT_BETWEEN:
                @SuppressWarnings("unchecked")
                Pair<Object, Object> pair = (Pair<Object, Object>) lambdaCQ.getValue();
                assert pair != null;
                stringBuilder.append(' ').append(pair.getFirst()).append(" AND ").append(pair.getSecond());
                break;

            case IS_NULL:
            case IS_NOT_NULL:
            case IS_TRUE:
            case IS_FALSE:
                break;

            case IN:
            case NOT_IN:
                stringBuilder.append(" (").append(renderValue(lambdaCQ.getValue())).append(')');
                break;

            default:
                stringBuilder.append(' ').append(renderValue(lambdaCQ.getValue()));
        }
    }

    private static String renderValue(@Nullable Object value) {

        if (value instanceof Number) {
            return value.toString();
        }

        if (value instanceof Collection) {

            StringJoiner joiner = new StringJoiner(", ");
            ((Collection<?>) value).forEach(o -> joiner.add(renderValue(o)));
            return joiner.toString();
        }

        if (value != null) {
            return String.format("'%s'", value);
        }

        return "null";
    }

    /**
     * Interface declaring terminal builder methods to build a {@link LambdaCQ}.
     */
    public interface LambdaCQStep<Do> {

        /**
         * Creates a {@link LambdaCQ} using equality.
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> is(Object value);

        /**
         * Creates a {@link LambdaCQ} using equality (is not).
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> not(Object value);

        /**
         * Creates a {@link LambdaCQ} using {@code IN}.
         *
         * @param values must not be {@literal null}.
         */
        LambdaCQ<Do> in(Object... values);

        /**
         * Creates a {@link LambdaCQ} using {@code IN}.
         *
         * @param values must not be {@literal null}.
         */
        LambdaCQ<Do> in(Collection<?> values);

        /**
         * Creates a {@link LambdaCQ} using {@code NOT IN}.
         *
         * @param values must not be {@literal null}.
         */
        LambdaCQ<Do> notIn(Object... values);

        /**
         * Creates a {@link LambdaCQ} using {@code NOT IN}.
         *
         * @param values must not be {@literal null}.
         */
        LambdaCQ<Do> notIn(Collection<?> values);

        /**
         * Creates a {@link LambdaCQ} using between ({@literal BETWEEN begin AND end}).
         *
         * @param begin must not be {@literal null}.
         * @param end   must not be {@literal null}.
         * @since 2.2
         */
        LambdaCQ<Do> between(Object begin, Object end);

        /**
         * Creates a {@link LambdaCQ} using not between ({@literal NOT BETWEEN begin AND end}).
         *
         * @param begin must not be {@literal null}.
         * @param end   must not be {@literal null}.
         * @since 2.2
         */
        LambdaCQ<Do> notBetween(Object begin, Object end);

        /**
         * Creates a {@link LambdaCQ} using less-than ({@literal <}).
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> lessThan(Object value);

        /**
         * Creates a {@link LambdaCQ} using less-than or equal to ({@literal <=}).
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> lessThanOrEquals(Object value);

        /**
         * Creates a {@link LambdaCQ} using greater-than({@literal >}).
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> greaterThan(Object value);

        /**
         * Creates a {@link LambdaCQ} using greater-than or equal to ({@literal >=}).
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> greaterThanOrEquals(Object value);

        /**
         * Creates a {@link LambdaCQ} using {@code LIKE}.
         *
         * @param value must not be {@literal null}.
         */
        LambdaCQ<Do> likeAll(String value);

        LambdaCQ<Do> likeAll(boolean condition, String value);

        LambdaCQ<Do> likeAll(Function<String, Boolean> condition, String value);

        LambdaCQ<Do> likeLeft(String value);

        LambdaCQ<Do> likeRight(String value);

        /**
         * Creates a {@link LambdaCQ} using {@code NOT LIKE}.
         *
         * @param value must not be {@literal null}
         * @return a new {@link LambdaCQ} object
         */
        LambdaCQ<Do> notLike(String value);

        /**
         * Creates a {@link LambdaCQ} using {@code IS NULL}.
         */
        LambdaCQ<Do> isNull();

        /**
         * Creates a {@link LambdaCQ} using {@code IS NOT NULL}.
         */
        LambdaCQ<Do> isNotNull();

        /**
         * Creates a {@link LambdaCQ} using {@code IS TRUE}.
         *
         * @return a new {@link LambdaCQ} object
         */
        LambdaCQ<Do> isTrue();

        /**
         * Creates a {@link LambdaCQ} using {@code IS FALSE}.
         *
         * @return a new {@link LambdaCQ} object
         */
        LambdaCQ<Do> isFalse();
    }

    /**
     * Default {@link LambdaCQStep} implementation.
     */
    static class DefaultLambdaCQStep<Do> implements LambdaCQStep<Do> {

        private final SqlIdentifier property;

        DefaultLambdaCQStep(SqlIdentifier property) {
            this.property = property;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#is(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> is(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.EQ, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#not(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> not(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.NEQ, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#in(java.lang.Object[])
         */
        @Override
        public LambdaCQ<Do> in(Object... values) {

            Assert.notNull(values, "Values must not be null!");
            Assert.noNullElements(values, "Values must not contain a null value!");

            if (values.length > 1 && values[1] instanceof Collection) {
                throw new InvalidDataAccessApiUsageException(
                        "You can only pass in one argument of type " + values[1].getClass().getName());
            }

            return createLambdaCQ(Comparator.IN, Arrays.asList(values));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#in(java.util.Collection)
         */
        @Override
        public LambdaCQ<Do> in(Collection<?> values) {

            Assert.notNull(values, "Values must not be null!");
            Assert.noNullElements(values.toArray(), "Values must not contain a null value!");

            return createLambdaCQ(Comparator.IN, values);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#notIn(java.lang.Object[])
         */
        @Override
        public LambdaCQ<Do> notIn(Object... values) {

            Assert.notNull(values, "Values must not be null!");
            Assert.noNullElements(values, "Values must not contain a null value!");

            if (values.length > 1 && values[1] instanceof Collection) {
                throw new InvalidDataAccessApiUsageException(
                        "You can only pass in one argument of type " + values[1].getClass().getName());
            }

            return createLambdaCQ(Comparator.NOT_IN, Arrays.asList(values));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#notIn(java.util.Collection)
         */
        @Override
        public LambdaCQ<Do> notIn(Collection<?> values) {

            Assert.notNull(values, "Values must not be null!");
            Assert.noNullElements(values.toArray(), "Values must not contain a null value!");

            return createLambdaCQ(Comparator.NOT_IN, values);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#between(java.lang.Object, java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> between(Object begin, Object end) {

            Assert.notNull(begin, "Begin value must not be null!");
            Assert.notNull(end, "End value must not be null!");

            return createLambdaCQ(Comparator.BETWEEN, Pair.of(begin, end));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#notBetween(java.lang.Object, java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> notBetween(Object begin, Object end) {

            Assert.notNull(begin, "Begin value must not be null!");
            Assert.notNull(end, "End value must not be null!");

            return createLambdaCQ(Comparator.NOT_BETWEEN, Pair.of(begin, end));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#lessThan(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> lessThan(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.LT, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#lessThanOrEquals(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> lessThanOrEquals(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.LTE, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#greaterThan(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> greaterThan(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.GT, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#greaterThanOrEquals(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> greaterThanOrEquals(Object value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.GTE, value);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#like(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> likeAll(String value) {

            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.LIKE, MessageFormat.format("%{0}%", value));
        }

        @Override
        public LambdaCQ<Do> likeAll(boolean condition, String value) {
            if (condition) {
                return likeAll(value);
            }
            return EMPTY();
        }

        @Override
        public LambdaCQ<Do> likeAll(Function<String, Boolean> condition, String value) {
            return likeAll(condition.apply(value), value);
        }

        @Override
        public LambdaCQ<Do> likeLeft(String value) {
            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.LIKE, MessageFormat.format("{0}%", value));
        }

        @Override
        public LambdaCQ<Do> likeRight(String value) {
            Assert.notNull(value, "Value must not be null!");

            return createLambdaCQ(Comparator.LIKE, MessageFormat.format("%{0}", value));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#notLike(java.lang.Object)
         */
        @Override
        public LambdaCQ<Do> notLike(String value) {
            Assert.notNull(value, "Value must not be null!");
            return createLambdaCQ(Comparator.NOT_LIKE, MessageFormat.format("%{0}%", value));
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#isNull()
         */
        @Override
        public LambdaCQ<Do> isNull() {
            return createLambdaCQ(Comparator.IS_NULL, null);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#isNotNull()
         */
        @Override
        public LambdaCQ<Do> isNotNull() {
            return createLambdaCQ(Comparator.IS_NOT_NULL, null);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#isTrue()
         */
        @Override
        public LambdaCQ<Do> isTrue() {
            return createLambdaCQ(Comparator.IS_TRUE, true);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.relational.query.LambdaCQ.LambdaCQStep#isFalse()
         */
        @Override
        public LambdaCQ<Do> isFalse() {
            return createLambdaCQ(Comparator.IS_FALSE, false);
        }

        protected LambdaCQ<Do> createLambdaCQ(Comparator comparator, @Nullable Object value) {
            return new LambdaCQ<>(this.property, comparator, value);
        }
    }
}
