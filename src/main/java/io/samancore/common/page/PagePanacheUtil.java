package io.samancore.common.page;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

public class PagePanacheUtil {

    private PagePanacheUtil() {
    }

    public static Sort generateSort(PageRequest pageRequest) {
        String pageSort = pageRequest.getSort();
        String pageOrder = pageRequest.getOrder();

        Sort.Direction direction;
        if (pageOrder.equals("desc")) {
            direction = Sort.Direction.Descending;
        } else {
            direction = Sort.Direction.Ascending;
        }
        return Sort.by(pageSort).direction(direction);
    }

    public static Page generatePage(PageRequest pageRequest) {
        int pageNumber = pageRequest.getPage();
        int pageLimit = pageRequest.getLimit();

        return Page.of(pageNumber, pageLimit);
    }
}
