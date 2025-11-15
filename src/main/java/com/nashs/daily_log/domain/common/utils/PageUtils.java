package com.nashs.daily_log.domain.common.utils;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class PageUtils<T> {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DISPLAY_PAGE_COUNT = 3;
    private final Pageable pageable;

    private Page<T> page;
    private int pageCount;

    public PageUtils(int page) {
        this.pageable = PageRequest.of(Math.max(page - 1, 0), DEFAULT_PAGE_SIZE);
    }

    public void setupContent(Page<T> page) {
        this.page = page;
        this.pageCount = page.getTotalPages();
    }

    public int getPrevPage() {
        return Math.max(this.page.getNumber(), 0);
    }

    public int getNextPage() {
        return Math.min(getTotalPages(), getCurrentPage() + 1);
    }

    public int getFirstPage() {
        return getTotalPages() > DISPLAY_PAGE_COUNT
             ? getCurrentPage()
             : 1;
    }

    public int getLastPage() {
        return Math.min(getTotalPages(), getCurrentPage() + DISPLAY_PAGE_COUNT - 1);
    }

    public int getTotalPages() {
        return this.page.getTotalPages();
    }

    public int getCurrentPage() {
        return this.page.getNumber() + 1;
    }

    public int getDrawFirstPage() {
        return this.pageCount - getCurrentPage() < DISPLAY_PAGE_COUNT
             ? this.pageCount - DISPLAY_PAGE_COUNT + 1
             : getCurrentPage();
    }

    public int getDrawLastPage() {
        return Math.min(getCurrentPage() + DISPLAY_PAGE_COUNT - 1, getLastPage());
    }

    public List<T> getContent() {
        return this.page.getContent();
    }
}
