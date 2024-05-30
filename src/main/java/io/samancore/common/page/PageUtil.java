package io.samancore.common.page;

import io.samancore.common.model.PageData;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PageUtil {

    private static final Logger log = Logger.getLogger(PageUtil.class);

    private PageUtil() {
    }

    public static PageRequest getPage(MultivaluedMap<String, String> queryParameters) {
        try {
            String pageSort = queryParameters.getFirst(PageParamConstant.SORT);
            Objects.requireNonNull(pageSort);
            String pageOrder = queryParameters.getFirst(PageParamConstant.ORDER);
            Objects.requireNonNull(pageOrder);
            var page = queryParameters.getFirst(PageParamConstant.PAGE);
            Objects.requireNonNull(page);
            var limit = queryParameters.getFirst(PageParamConstant.LIMIT);
            Objects.requireNonNull(limit);
            var pageNumber = Integer.parseInt(page);
            var pageLimit = Integer.parseInt(limit);
            return PageRequest.newBuilder()
                    .setPage(pageNumber)
                    .setLimit(pageLimit)
                    .setSort(pageSort)
                    .setOrder(pageOrder)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("Any pagination parameter is null", e);
        }
    }

    public static <ENTITY> PageData<ENTITY> toPageEntity(List<ENTITY> list, long total) {
        return PageData.<ENTITY>newBuilder()
                .setData(list)
                .setCount(total)
                .build();
    }

    public static <ENTITY, MODEL> PageData<MODEL> toPageModel(PageData<ENTITY> model, Function<? super ENTITY, MODEL> mapper) {
        var modelList = model.getData().stream()
                .map(mapper)
                .toList();
        return PageData.<MODEL>newBuilder()
                .setData(modelList)
                .setCount(model.getCount())
                .build();
    }

    public static <ENTITY> Uni<PageData<ENTITY>> combineToPageData(Uni<List<ENTITY>> list, Uni<Long> total) {
        return list.onItem()
                .transformToUni(entityList ->
                        total.onItem()
                            .transform(count ->
                                    PageData.<ENTITY>newBuilder()
                                        .setData(entityList)
                                        .setCount(count)
                                        .build()));
    }
}
