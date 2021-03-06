/*
 ******************************************************************************
 * Copyright (c) 2016 IBM Corp. and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   2009       - Mark Struberg
 *      Ordinal solution in Apache OpenWebBeans
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Contributed to Apache DeltaSpike fb0131106481f0b9a8fd
 *   2016-07-14 - Mark Struberg
 *      Extracted the Config part out of DeltaSpike and proposed as Microprofile-Config cf41cf130bcaf5447ff8
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      Methods renamed, JavaDoc and cleanup
 *
 *******************************************************************************/
package org.eclipse.microprofile.config.spi;

import java.util.Map;

/**
 * <p>Implement this interfaces to provide a ConfigSource.
 * A ConfigSource provides configuration values from a specific place, like JNDI configuration, a properties file, etc.
 * A ConfigSource is always read-only, any potential updates of the configured values must be handled directly inside each ConfigSource.
 *
 * <p>
 * The default config sources always available by default are:
 * <ol>
 * <li>System properties (ordinal=400)</li>
 * <li>Environment properties (ordinal=300)</li>
 * <li>/META-INF/microprofile-config.properties (ordinal=100)</li>
 * </ol>
 * 
 * <p>Custom ConfigSource will get picked up via the {@link java.util.ServiceLoader} mechanism and and can be registered by
 * providing a file
 * <pre>
 *     META-INF/services/javax.config.spi.ConfigSource
 * </pre>
 * which contains the fully qualified {@code ConfigSource} implementation class name as content.
 *
 * <p>Adding a dynamic amount of custom config sources can be done programmatically via
 * {@link org.eclipse.microprofile.config.spi.ConfigSourceProvider}.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface ConfigSource {
    /**
     * Return the properties in this config source
     * @return the map containing the properties in this config source
     */
    Map<String, String> getProperties();

    /**
     * Return the ordinal for this config source. If a property is specified in multiple config sources, the value
     * in the config source with the highest ordinal takes precedence.
     * For the config sources with the same ordinal value, the config source names will
     * be used for sorting according to string sorting criteria.
     * Note that this property only gets evaluated during ConfigSource discovery.
     *
     * The ordinal for the default config sources:
     * <ol>
     *  <li>System properties (ordinal=400)</li>
     *  <li>Environment properties (ordinal=300)</li>
     *  <li>/META-INF/microprofile-config.properties (ordinal=100)</li>
     * </ol>
     *
     *
     * Any ConfigSource part of an application will typically use an ordinal between 0 and 200.
     * ConfigSource provided by the container or 'environment' typically use an ordinal higher than 200.
     * A framework which intends have values overwritten by the application will use ordinals between 0 and 100.
     * The property "config_ordinal" can be specified to override the default value.
     * 
     * @return the ordinal value
     */
    default int getOrdinal() {
        return 100;
    }

    /**
     * Return the value for the specified property in this config source.
     * @param propertyName the property name
     * @return the property value
     */
    String getValue(String propertyName);

    /**
     * The name of the config might be used for logging or analysis of configured values.
     *
     * @return the 'name' of the configuration source, e.g. 'property-file mylocation/myproperty.properties'
     */
    String getName();
    
}