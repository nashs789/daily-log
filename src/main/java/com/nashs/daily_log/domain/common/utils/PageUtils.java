package com.nashs.daily_log.domain.common.utils;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class PageUtils<T> {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private Page<T> page;
    private final Pageable pageable;

    public PageUtils(int page) {
        pageable = PageRequest.of(Math.max(page - 1, 0), DEFAULT_PAGE_SIZE);
    }

    public void setupContent(Page<T> page) {
        this.page = page;
    }

    public int getPrevPage() {
        return Math.max(page.getNumber(), 0);
    }

    public int getNextPage() {
        return Math.min(getTotalPages(), getCurrentPage() + 1);
    }

    public int getMinPage() {
        return getTotalPages() > 3
             ? getCurrentPage()
             : 1;
    }

    public int getMaxPage() {
        return Math.min(getTotalPages(), getCurrentPage() + 2);
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public int getPageSize() {
        return page.getSize();
    }

    public int getCurrentPage() {
        return page.getNumber() + 1;
    }

    public List<T> getContent() {
        return page.getContent();
    }
}
