/*
 * Copyright (C) 2015 >bootenv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bootenv.utility;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Optional.*;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrés Amado
 * @since 2015-06-07
 */
public class EnvironmentImpl implements Environment {

    private static final Logger LOG = getLogger(Environment.class);

    public EnvironmentImpl() {
    }

    @Override
    public boolean has(final String key) {
        LOG.debug("Checking is Environment has the [{}] property...", key);
        Optional<String> property = getOptionalProperty(key);

        return property.isPresent();
    }

    @Override
    public boolean supports(final String feature) {
        LOG.debug("Checking is Environment supports the [{}] feature...", feature);
        Optional<String> property = getOptionalProperty(feature);

        return (property.isPresent() && safeSupports(property.get()));
    }

    @Override
    public boolean supports(final String feature, final boolean defaultValue) {
        LOG.debug("Checking is Environment supports the [{}] feature...", feature);
        Optional<String> property = getOptionalProperty(feature);
        if (property.isPresent()) {
            return safeSupports(property.get());
        }

        LOG.debug("Environment not supports the [{}] feature, returning default value [{}]", feature, defaultValue);
        return defaultValue;
    }

    @Override
    public Optional<String> getOptionalProperty(final String key) {
        LOG.debug("Getting [{}] property from Environment...", key);
        try {
            String value = System.getenv(key);
            if (!isNullOrEmpty(value)) {
                return of(value);
            }
        } catch (Exception ex) {
            LOG.error("Error getting Environment property for key [{}]!", key, ex);
        }

        return absent();
    }

    @Override
    public String getProperty(final String key) {
        return getProperty(key, null);
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        Optional<String> property = getOptionalProperty(key);
        if (property.isPresent()) {
            return property.get();
        }

        LOG.debug("Environment has no the [{}] property, returning default value [{}]", key, defaultValue);
        return defaultValue;
    }

    @Override
    public Iterable<String> getKeys() {
        LOG.debug("Getting Environment keys...");
        try {
            Optional<Map<String, String>> properties = fromNullable(System.getenv());
            if (properties.isPresent()) {
                Set<String> keys = properties.get().keySet();
                if (!keys.isEmpty()) {
                    return Iterables.unmodifiableIterable(keys);
                }
            }
        } catch (Exception ex) {
            LOG.error("Error getting ENV properties!", ex);
        }

        LOG.debug("Environment has no properties...");
        return Collections.emptyList();
    }

    private boolean safeSupports(final String value) {
        return Boolean.parseBoolean(value.trim().toLowerCase());
    }

}
