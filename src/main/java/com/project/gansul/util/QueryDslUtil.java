package com.project.gansul.util;

import com.project.gansul.exception.AppException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {

    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    public static int getPageSize(int requestValue) { return (requestValue == 0) ? 25 : requestValue; }

    public static int getPageNo(int requestValue) { return (requestValue == 0) ? 0 : requestValue - 1; }

    public static List<OrderSpecifier> getOrderSpecifiers(String reqOrderColumns, String reqOrderTypes, Path<?> qClass) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (reqOrderColumns != null) {
            String[] orderColumns = reqOrderColumns.split(",");

            if (reqOrderTypes == null) {
                throw new AppException(BaseResponseCode.COLUMN_LENGTH_DIFF, "error.column.length.difference");
            }

            String[] orderTypes = reqOrderTypes.split(",");

            if (orderColumns.length != orderTypes.length) {
                throw  new AppException(BaseResponseCode.COLUMN_LENGTH_DIFF, "error.column.length.difference");
            }

            for (int i = 0; i < orderColumns.length; i++) {
                final String orderColumn = orderColumns[i];
                final String orderType = orderTypes[i];

                Order direction = "asc".equalsIgnoreCase(orderType) ? Order.ASC : Order.DESC;
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier(direction, Expressions.path(Object.class, qClass, orderColumn));
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }
}
