package io.samancore.common.transformer.impl;

import io.samancore.common.transformer.Masker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("showLast4")
@ApplicationScoped
public class MaskerShowLastFourImpl implements Masker {
    @Override
    public String apply(String value) {
        return value.replaceAll(".(?=....)", "*");
    }
}
