package cn.geterror.serialization.mybatis;

import cn.geterror.serialization.json.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectListTypeHandler extends BaseTypeHandler<List<Object>> {

    StringListTypeHandler stringListTypeHandler = new StringListTypeHandler();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE);
        } else if (!parameter.isEmpty() && parameter.get(0) instanceof String) {
            stringListTypeHandler.setNonNullParameter(ps, i, parameter.stream().map(Object::toString).collect(Collectors.toList()), jdbcType);
        } else if (!parameter.isEmpty() && parameter.get(0) instanceof Integer){
            Map<String,Object> map = new HashMap<>();
            map.put("class",Integer.class.getName());
            map.put("data",parameter);
            ps.setString(i, JsonUtils.toJSONString(map));
        } else if (!parameter.isEmpty() && parameter.get(0) instanceof DataBaseSerializable) {
            String s = "[" + parameter.stream()
                    .map(obj -> (DataBaseSerializable) obj)
                    .map(DataBaseSerializable::serialize)
                    .collect(Collectors.joining(",")) + "]";
            ps.setString(i, s);
        } else if (parameter.isEmpty()) {
            ps.setString(i, "[]");
        }

    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {

        String dbs = rs.getString(columnName);
        if (dbs == null) {
            return null;
        }
        try {
            return DataBaseSerializable.listDeserialization(dbs);
        }catch (Exception ignore){}

        try {
            List<Object> list = JsonUtils.fromJSON(dbs, new TypeReference<List<Object>>() {
            });
            return list.stream()
                    .map(i -> DataBaseSerializable.deserialization(i.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return stringListTypeHandler.getNullableResult(rs, columnName).stream().map(s -> (Object) s).collect(Collectors.toList());
        }

    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        String dbs = rs.getString(columnIndex);
        if (dbs == null) {
            return null;
        }
        try {
            return DataBaseSerializable.listDeserialization(dbs);
        }catch (Exception ignore){}
        try {
            List<Object> list = JsonUtils.fromJSON(dbs, new TypeReference<List<Object>>() {
            });
            return list.stream()
                    .map(i -> DataBaseSerializable.deserialization(i.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return stringListTypeHandler.getNullableResult(rs, columnIndex).stream().map(s -> (Object) s).collect(Collectors.toList());
        }
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        String dbs = cs.getString(columnIndex);
        if (dbs == null) {
            return null;
        }
        try {
            return DataBaseSerializable.listDeserialization(dbs);
        }catch (Exception ignore){}
        try {
            List<Object> list = JsonUtils.fromJSON(dbs, new TypeReference<List<Object>>() {
            });
            return list.stream()
                    .map(i -> DataBaseSerializable.deserialization(i.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return stringListTypeHandler.getNullableResult(cs, columnIndex).stream().map(s -> (Object) s).collect(Collectors.toList());
        }
    }
}
