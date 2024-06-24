package io.samancore.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import io.samancore.common.bean.jackson.CustomObjectMapper;
import io.samancore.common.error.exception.BusinessException;
import io.samancore.common.error.message.BusinessExceptionsEnum;
import io.samancore.common.model.condition.Condition;
import io.samancore.common.model.condition.ConditionRequest;
import io.samancore.common.model.condition.ConditionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConditionsUtil {

    private ConditionsUtil() {
    }

    public static ObjectMapper getObjectMapper() {
        return CustomObjectMapper.changeFeature(new ObjectMapper());
    }

    public static <MODEL> Map<String, Object> getModelMap(MODEL model, ObjectMapper objectMapper) {
        return objectMapper.convertValue(model, new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, Object> getModelMapUpdatedWithConditions(Map<String, Object> modelMap, List<Condition> conditions) {
        var validateError = conditions.stream().filter(condition -> condition.getConditionType().equals(ConditionType.VALIDATE) && condition.getValue() != null && !StringUtil.isNullOrEmpty(condition.getValue().toString())).toList();
        if (!validateError.isEmpty()) {
            var allProperties = validateError.stream().map(condition -> condition.getValue().toString()).collect(Collectors.joining(","));
            throw new BusinessException(BusinessExceptionsEnum.CONDITION_VALIDATE_ERROR, List.of(allProperties));
        }
        conditions.stream().filter(condition -> condition.getConditionType().equals(ConditionType.VALUE)).forEach(condition -> modelMap.put(condition.getProperty(), condition.getValue()));
        return modelMap;
    }

    public static ConditionRequest getConditionRequest(Map<String, Object> modelMap) {
        return ConditionRequest.newBuilder()
                .setEvalAll(true)
                .setVariables(modelMap)
                .setWithoutPresentation(true)
                .build();
    }


}
