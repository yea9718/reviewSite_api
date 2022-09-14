package com.project.gansul.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.querydsl.core.types.Ops;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Order;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PARAM_SEP = "&";
    private static final String VALUE_SEP = "=";
    private static final String COUNT_PARAM_NAME = "size";
    private static final String PAGE_PARAM_NAME = "pageNo";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String EXCEL = "excel";


    public static BaseSearch.Search buildSearch(final String queryString, final List<String> equals,
                                                final BaseSearch.Sort... defaultSort) {
        Integer size = 25;
        Integer page = 0;
        boolean export = Boolean.FALSE;
        final List<BaseSearch.Filter> filters = new ArrayList<>();
        final List<BaseSearch.Sort> sorts = new ArrayList<>();
        int sortSeq = 0;
        for (final String keyValue : Splitter.on(PARAM_SEP).omitEmptyStrings()
                .split(Strings.nullToEmpty(queryString))) {
            final Iterator<String> it =
                    Splitter.on(VALUE_SEP).omitEmptyStrings().split(keyValue).iterator();
            if (it.hasNext()) {
                final String key = it.next();
                String value = null;
                Boolean value2 = null;
                Long value3 = null;

                if (it.hasNext()) {
                    value = it.next();
                    if (value.equalsIgnoreCase("" + null)) {
                        value = null;
                    } else {
                        try {
                            value = URLDecoder
                                    .decode(value.replaceAll("\\+", "%2B"), StandardCharsets.UTF_8.name())
                                    .replaceAll("%2B", "+");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (ASC.equals(key) || DESC.equals(key)) {
                    if (value != null) {
                        final Sort.Direction direction = ASC.equals(key) ? Sort.Direction.ASC : Sort.Direction.DESC;
                        sorts
                                .add(BaseSearch.Sort.builder().seq(sortSeq++).name(value).direction(direction)
                                        .build());
                    }
                } else if (EXCEL.equals(key)) {
                    export = Boolean.TRUE;
                } else {
                    if (value != null) {
                        if (COUNT_PARAM_NAME.equals(key)) {
                            size = Integer.valueOf(value);
                        } else if (PAGE_PARAM_NAME.equals(key)) {
                            page = Integer.valueOf(value) == 0 ? 0 : Integer.valueOf(value) - 1;
                        } else {
                            final Ops ops;
                            if (equals.contains(key)) {
                                ops = Ops.EQ;
                            } else if (key.contains("Dt") && key.contains("start")) {
                                ops = Ops.GOE; //Greater or equal
                            } else if (key.contains("Dt") && key.contains("end")) {
                                ops = Ops.LOE; //Less or equal  Ops.LT; //Less than
                            } else if (key.contains("Id")) {
                                ops = Ops.EQ;
                                value3 = Long.valueOf(value);
                                value = null;
                                value2 = null;
                            } else if (value.contains("true") || value.contains("false")) {
                                ops = Ops.EQ;
                                if ("false".equals(value)) {
                                    value2 = Boolean.valueOf(false);
                                } else {
                                    value2 = Boolean.valueOf(true);
                                }
                                value = null;
                                value3 = null;
                            } else {
                                ops = Ops.LIKE; //Like
                                value = "%" + value + "%";
                            }

                            if (value != null) {
                                filters.add(BaseSearch.Filter.builder().name(key).value(value).ops(ops).build());
                            } else if (value2 != null) {
                                filters.add(BaseSearch.Filter.builder().name(key).value(value2).ops(ops).build());
                            } else if (value3 != null) {
                                filters.add(BaseSearch.Filter.builder().name(key).value(value3).ops(ops).build());
                            }
                        }
                    }
                }
            }
        }
        if (sorts.isEmpty() && defaultSort.length == 0) {
            sorts.add(defaultSort[0]);
        }
        return BaseSearch.Search.builder().page(page).size(size).filters(filters).sorts(sorts)
                .export(export).build();
    }

    public static PageRequest pageRequest(
            int page, int size, Sort.Direction direction, String... properties) {
        page = page == 0 ? 0 : page - 1;
        size = size == 0 ? 25 : size;
        final PageRequest pageRequest = PageRequest.of(page, size, direction, properties);
        return pageRequest;
    }

    public static Pageable getPageable(final DataTablesInput input) {
        final List<Sort.Order> orders = new ArrayList<>();
        for (Order order : input.getOrder()) {
            logger.debug("order column: " + order.getColumn() + "");
            final Column column = input.getColumns().get(order.getColumn());
            if (column.getOrderable()) {
                final String sortColumn = column.getData();
                final Sort.Direction sortDirection = Sort.Direction.fromString(order.getDir());
                orders.add(new Sort.Order(sortDirection, sortColumn));
            }
        }
        final Sort sort = orders.isEmpty() ? null : Sort.by(orders);
        if (input.getLength() == -1) {
            input.setStart(0);
            input.setLength(Integer.MAX_VALUE);
        }
        return PageRequest.of(input.getDraw(), input.getLength(), sort);
    }

    public static PageRequest pageRequest(final String queryString, final List<String> likes,
                                          final BaseSearch.Sort... defaultSort) {
        final BaseSearch.Search search = buildSearch(queryString, likes, defaultSort);
        return pageRequest(search);
    }

    public static PageRequest pageRequest(final BaseSearch.Search search) {
        final List<Sort.Order> orders = new ArrayList<>();
        final List<BaseSearch.Sort> sorts = search.getSorts().stream()
                .sorted(Comparator.comparingInt(BaseSearch.Sort::getSeq)).collect(
                        Collectors.toList());

        for (BaseSearch.Sort sort : sorts) {
            logger.debug("order column: " + sort.getName());
            final Sort.Order order = new Sort.Order(sort.getDirection(), sort.getName());
            orders.add(order);
        }
        final Sort sort = orders.isEmpty() ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(orders);
        return PageRequest.of(search.getPage(), search.getSize(), sort);
    }
}
