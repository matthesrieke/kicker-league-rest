package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;
import org.springframework.data.domain.Page;

import java.util.List;


public final class PageableResponse<T> {
    @JsonView({Basic.class})

    private Integer total;
    @JsonView({Basic.class})

    private Integer page;
    @JsonView({Basic.class})

    private Integer size;
    @JsonView({Basic.class})

    private List<T> data;

    public static <T> PageableResponse<T> from(Page<T> pageable) {
        PageableResponse<T> response = new PageableResponse<>();
        response.data = pageable.getContent();
        response.page = pageable.getNumber();
        response.size = pageable.getSize();
        response.total = pageable.getTotalPages() * pageable.getSize();
        return response;
    }

    public final Integer getTotal() {
        return this.total;
    }

    public final void setTotal(Integer var1) {
        this.total = var1;
    }

    public final Integer getPage() {
        return this.page;
    }

    public final void setPage(Integer var1) {
        this.page = var1;
    }

    public final Integer getSize() {
        return this.size;
    }

    public final void setSize(Integer var1) {
        this.size = var1;
    }

    public final List<T> getData() {
        return this.data;
    }

    public final void setData(List<T> var1) {
        this.data = var1;
    }
}
