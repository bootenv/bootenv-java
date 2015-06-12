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

package org.bootenv;

import com.google.common.base.Optional;

/**
 * <p>The <code>Environment</code> class contains several useful methods
 * to make using the environment properties more pleasant!
 * </p>
 * <p>Among the facilities provided by the <code>Environment</code> class
 * are has key, support a environment property, get environment property
 * and get all environment properties.
 * </p>
 *
 * @author Andrés Amado
 * @since 2015-06-07
 */
public interface Environment {

    /**
     * Checks if a key is present
     *
     * @return <tt>true</tt> only if the environment has a property
     */
    boolean hasProperty(String key);

    /**
     * Checks if a key is equals to <tt>true</tt> if it's not present will return <tt>false</tt>
     *
     * @param feature The feature name
     * @return <tt>true</tt> only if the environment has a property and the value is <tt>true</tt>
     */
    boolean supports(String feature);

    /**
     * Checks if a key is equals to <tt>true</tt>
     * if it's not present will return the <tt>defaultValue</tt>
     *
     * @param feature      The feature name
     * @param defaultValue The  default value
     * @return <tt>true</tt> only if the environment has a property and the value is <tt>true</tt>,
     * otherwise: returns the <tt>defaultValue</tt>
     * @see Environment#supports(String)
     */
    boolean supportsOr(String feature, boolean defaultValue);

    /**
     * Get a Optional property value according to Environment
     *
     * @param key The property name
     * @return The Optional property value according to Environment
     */
    Optional<String> getOptionalProperty(String key);

    /**
     * Get the property value according to Environment,
     * use only if you are 100% sure that the environment has a key,
     * otherwise use {@link Environment#getOptionalProperty(String)}
     *
     * @param key The property name
     * @return The property value according to Environment, otherwise returns <tt>null<tt>
     */
    String getProperty(String key);

    /**
     * Get the property value according to Environment,
     * if it's not present will return the <tt>defaultValue</tt>
     *
     * @param key          The property name
     * @param defaultValue The default value
     * @return the property value according to Environment
     * @see Environment#getProperty(String)
     */
    String getPropertyOr(String key, String defaultValue);

    /**
     * @return an {@link Iterable} with all Environment keys
     */
    Iterable<String> getKeys();

}
