package com.springboot.project.common.specification;

import com.springboot.project.common.generated.model.PaginationRequestModel;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import java.util.function.Function;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

public final class SpecificationHelper {

    private SpecificationHelper() {
        throw new IllegalArgumentException("Static class can not be created");
    }

    public static <T> Specification<T> init(Example<T> example) {
        return (root, query, builder) -> {
            // If example is null or has all null attributes, return all items
            if (Objects.isNull(example) || allAttributesNull(example.getProbe())) {
                return builder.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            // Add predicates based on the example
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> boolean allAttributesNull(T probe) {
        // Check if all attributes in the probe object are null using reflection
        try {
            Map<String, String> map = BeanUtils.describe(probe);
            for (String value : map.values()) {
                if (value != null) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Pageable buildPageable(PaginationRequestModel paginationRequest) {
        Integer pageNum = paginationRequest.getPageNumber();
        Integer size = paginationRequest.getPageSize();
        int pageNumber = Objects.nonNull(pageNum) ? pageNum : 0;
        int pageSize = Objects.nonNull(size) ? size : 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (Objects.nonNull(paginationRequest.getSortBy())
                && Objects.nonNull(paginationRequest.getSortOrder())) {
            pageable = PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by(
                            Sort.Direction.valueOf(
                                    paginationRequest.getSortOrder().getValue()),
                            paginationRequest.getSortBy()));
        }
        return pageable;
    }

    public static Pageable buildPageableForCursor(PaginationRequestModel paginationRequest,
            String defaultCursorProperty) {
        Integer size = paginationRequest.getPageSize();
        int pageSize = Objects.nonNull(size) ? size : 50;

        Sort sort;
        if (paginationRequest.getSortBy() != null && paginationRequest.getSortOrder() != null) {
            sort = Sort.by(
                    Sort.Direction.valueOf(paginationRequest.getSortOrder().getValue()),
                    paginationRequest.getSortBy());
        } else {
            sort = Sort.by(Sort.Direction.ASC, defaultCursorProperty);
        }

        return PageRequest.of(0, pageSize, sort);
    }

    public static <T> Specification<T> cursorPagination(
            Sort sort, String idPropertyName, Long cursorValue, boolean isPrevious) {

        return (root, query, cb) -> {
            if (StringUtils.isBlank(idPropertyName) || cursorValue == null) {
                return cb.conjunction();
            }

            Sort.Order order = sort != null ? sort.getOrderFor(idPropertyName) : null;
            boolean ascending = order == null || order.isAscending();

            if (isPrevious) {
                ascending = !ascending;
            }

            return ascending
                    ? cb.greaterThan(root.get(idPropertyName), cursorValue)
                    : cb.lessThan(root.get(idPropertyName), cursorValue);
        };
    }

    public static ExampleMatcher containingIgnoreCaseMatcher() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

    /**
     * Builds the cursor-narrowed specification, sort and page size for a keyset (cursor) page.
     * When a previous-page token is present the sort is reversed so the page is fetched walking
     * backwards; callers are responsible for presenting rows in their natural order.
     */
    public static <T> CursorQuery<T> buildCursorQuery(
            Specification<T> baseSpecification,
            PaginationRequestModel pagination,
            String cursorProperty) {
        Pageable pageable = buildPageableForCursor(pagination, cursorProperty);
        Sort sort = pageable.getSort();
        Specification<T> specification = baseSpecification;

        Long nextPageToken = pagination.getNextPageToken();
        if (nextPageToken != null) {
            specification = specification.and(cursorPagination(sort, cursorProperty, nextPageToken, false));
        }

        Long previousPageToken = pagination.getPreviousPageToken();
        if (previousPageToken != null) {
            specification = specification.and(cursorPagination(sort, cursorProperty, previousPageToken, true));
            if (sort.isSorted()) {
                sort = sort.descending();
            }
        }
        return new CursorQuery<>(specification, sort, pageable.getPageSize());
    }

    public static <T> Function<FluentQuery.FetchableFluentQuery<T>, List<T>> limitedSortedQuery(
            Sort sort, int pageSize) {
        return query ->
                sort.isSorted()
                        ? query.sortBy(sort).limit(pageSize).all()
                        : query.limit(pageSize).all();
    }

    /** The specification, sort and page size that together fetch a single cursor page. */
    public record CursorQuery<T>(Specification<T> specification, Sort sort, int pageSize) {}
}
