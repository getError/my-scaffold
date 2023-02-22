package cn.geterror.serialization.json;

import cn.geterror.serialization.enums.IdEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JacksonSerializeStrategy {


    public static class EmptyList extends JsonSerializer<Object>{

        @Override
        public void serialize(Object list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if(list==null){
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }else {
                jsonGenerator.writeObject(list);
            }
        }
    }
    public static class DoubleWithDecimal extends JsonSerializer<Double> {
        @Override
        public void serialize(Double value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (Objects.nonNull(value)) {
                //保留2位小数
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //四舍五入
                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                String result = decimalFormat.format(value);
                jsonGenerator.writeNumber(result);
            } else {
                jsonGenerator.writeNumber(Double.valueOf(0));
            }
        }
    }

    public static class GenericListEnumDeserializer extends JsonDeserializer <List<? extends IdEnum<?>>> implements ContextualDeserializer {

        private final Class<?> type;
        GenericListEnumDeserializer(){
            this(null);
        }
        GenericListEnumDeserializer(Class<?> type){
            this.type = type;
        }
        @Override
        public List<IdEnum<?>> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            List<Integer> list = ctxt.readValue(p,ctxt.getTypeFactory().constructType(new TypeReference<List<Integer>>(){}));
            return  list.stream().map(i->IdEnum.getById((Class<? extends IdEnum<?>>) type, i)).collect(Collectors.toList());
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            Class<?> outerType = property.getType().getRawClass();
            Class<?> innerType = property.getType().getContentType().getRawClass();
            Class<?> enumType = property.getType().getContentType().getRawClass();

            if (List.class.isAssignableFrom(outerType)&& IdEnum.class.isAssignableFrom(innerType)) {
                //noinspection unchecked
                return new GenericListEnumDeserializer(innerType);
            }

            BeanDescription beanDesc = ctxt.getConfig().introspect(property.getType());

            return BeanDeserializerFactory.instance.buildBeanDeserializer(ctxt, property.getType(), beanDesc);
        }
    }
}
