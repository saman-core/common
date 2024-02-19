package io.samancore.common.reflection;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(classNames = {
        "java.lang.String",
        "org.apache.commons.logging.impl.LogFactoryImpl",
        "org.apache.commons.logging.impl.SimpleLog",
        "org.apache.commons.logging.LogFactory",
        "org.geolatte.geom.codec.PostgisWkbEncoder",
        "org.geolatte.geom.codec.PostgisWkbDecoder",
        "org.geolatte.geom.codec.Wkb"
}, registerFullHierarchy = true)
public class ReflectionUtil {
}
