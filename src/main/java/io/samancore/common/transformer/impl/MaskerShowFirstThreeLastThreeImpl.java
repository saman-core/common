package io.samancore.common.transformer.impl;

import io.samancore.common.transformer.Masker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("showFirst3Last3")
@ApplicationScoped
public class MaskerShowFirstThreeLastThreeImpl implements Masker {
    @Override
    public String apply(String value) {
        return value.replaceAll("(?<=...).(?=...)", "*");
    }
}
