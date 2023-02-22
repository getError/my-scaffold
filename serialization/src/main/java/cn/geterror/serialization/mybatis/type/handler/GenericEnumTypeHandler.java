package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.enums.IdEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class GenericEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final BaseTypeHandler finalHandler;

    public GenericEnumTypeHandler(Class<E> type) {

        if (IdEnum.class.isAssignableFrom(type)) {
            finalHandler = new IdEnumTypeHandler(type);
        } else {
            finalHandler = new EnumTypeHandler<>(type);
        }
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        finalHandler.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (E) finalHandler.getNullableResult(rs, columnName);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (E) finalHandler.getNullableResult(rs, columnIndex);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (E) finalHandler.getNullableResult(cs, columnIndex);
    }
}
