/**
 * Copyright (C) 2020 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jsonld.jackson.serialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import cz.cvut.kbss.jsonld.Configuration;
import cz.cvut.kbss.jsonld.common.BeanAnnotationProcessor;
import cz.cvut.kbss.jsonld.common.PropertyAccessResolver;
import cz.cvut.kbss.jsonld.serialization.ValueSerializer;

import java.util.Map;

/**
 * Main point of integration of the JSON-LD serialization implementation into Jackson.
 */
public class JsonLdSerializerModifier extends BeanSerializerModifier {

    private final Configuration configuration;

    private final Map<Class<?>, ValueSerializer<?>> commonSerializers;

    public JsonLdSerializerModifier(Configuration configuration, PropertyAccessResolver propertyAccessResolver, Map<Class<?>, ValueSerializer<?>> commonSerializers) {
        this.configuration = configuration;
        this.commonSerializers = commonSerializers;
        BeanAnnotationProcessor.setPropertyAccessResolver(propertyAccessResolver);
    }

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
                                              JsonSerializer<?> serializer) {
        if (BeanAnnotationProcessor.isOwlClassEntity(beanDesc.getBeanClass()) ||
                BeanAnnotationProcessor.hasTypesField(beanDesc.getBeanClass())) {
            return new JacksonJsonLdSerializer<>(configuration, commonSerializers);
        }
        return serializer;
    }
}
