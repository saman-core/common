package io.samancore.common.transformer.impl;

import io.samancore.common.transformer.Masker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("showFirst2Last2")
@ApplicationScoped
public class MaskerShowFirstTwoLastTwoImpl implements Masker {
    @Override
    public String apply(String value) {
        return value.replaceAll("(?<=..).(?=..)", "*");
    }
}
