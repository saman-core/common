package io.samancore.common.page;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder(
        setterPrefix = "set",
        toBuilder = true,
        builderMethodName = "newBuilder"
)
public class PageRequest {
    int page;
    int limit;
    String sort;
    String order;
}
