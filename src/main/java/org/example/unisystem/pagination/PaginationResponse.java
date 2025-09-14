package org.example.unisystem.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter

public class PaginationResponse<T> {
    private List<T> content;
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public PaginationResponse(Page<T> page) {
        this.pageNum = page.getNumber();
        this.content = page.getContent();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }

    private boolean last;

}
