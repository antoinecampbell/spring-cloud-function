package com.antoinecampbell.cloud.springcloudfunction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Page<T> {

    private List<T> content;
    private int page;
    private int size;
    private int total;

    @SuppressWarnings("unused")
    public int getPageCount() {
        double pageSize = size == 0 ? 1 : size;
        return (int) Math.ceil(total / pageSize);
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Params {

        private static final int PAGE_SIZE_DEFAULT = 25;

        @Min(1)
        private int page = 1;

        @Min(0)
        @Max(1000)
        private int size = PAGE_SIZE_DEFAULT;
    }
}
