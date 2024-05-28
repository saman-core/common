package io.samancore.common.transformer.impl;

import io.samancore.common.transformer.Masker;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaskerImpl implements Masker {
    @Override
    public String apply(String value) {
        return value;
    }
}
