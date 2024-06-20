package io.samancore.common.transformer.impl;

import io.samancore.common.transformer.Masker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("sensitiveDataMask")
@ApplicationScoped
public class MaskerSensitiveDataMaskImpl implements Masker {
    @Override
    public String apply(String value) {
        return value.replaceAll(".(?=....)", "*");
    }
}
