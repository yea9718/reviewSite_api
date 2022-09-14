package com.project.gansul.util;

import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

public class BaseSearch {
    @Data
    @Builder
    public static class Search {

        private Integer page;
        private Integer size;
        private boolean export;
        private List<Filter> filters;
        private List<Sort> sorts;
    }

    @Data
    @Builder
    @ApiModel("Search.Filter")
    public static class Filter {
        private String name;
        private Object value;
        private Ops ops;
    }

    @Data
    @Builder
    @ApiModel("Search.Sort")
    public static class Sort {
        private String name;
        private Direction direction;
        private int seq;
    }
}
