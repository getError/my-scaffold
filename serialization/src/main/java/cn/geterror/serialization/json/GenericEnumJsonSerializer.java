package cn.geterror.serialization.json;

import cn.geterror.serialization.enums.IdEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.io.IOException;


public class GenericEnumJsonSerializer {

    @SuppressWarnings("unused")
    public static class GenericEnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {

        private final JsonDeserializer<?> integerDeserializer = NumberDeserializers.find(Integer.class, Integer.class.getName());

        private final Class<IdEnum<?>> type;

        public GenericEnumDeserializer() {
            this(null);
        }

        public GenericEnumDeserializer(Class<IdEnum<?>> type) {
            this.type = type;
        }

        @Override
        public Enum<?> deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {

            if (type == null) {
                throw new IOException("Use createContextual");
            }

            Integer intValue = (Integer) integerDeserializer.deserialize(p, context);

            if (intValue == null) {
                return null;
            }

            return (Enum<?>) IdEnum.getById(type, intValue);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) throws JsonMappingException {

            Class<?> type = property.getType().getRawClass();

            if (IdEnum.class.isAssignableFrom(type)) {
                //noinspection unchecked
                return new GenericEnumDeserializer((Class<IdEnum<?>>) type);
            }

            BeanDescription beanDesc = ctx.getConfig().introspect(property.getType());

            return BeanDeserializerFactory.instance.buildBeanDeserializer(ctx, property.getType(), beanDesc);

        }
    }

    @SuppressWarnings("unused")
    public static class IdEnumSerializer extends JsonSerializer<IdEnum<?>> {

        @Override
        public void serialize(IdEnum<?> idEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (idEnum == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(idEnum.getId());
            }
        }
    }
}
