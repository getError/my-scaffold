package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.enums.IdEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdEnumTypeHandler<E extends IdEnum<?>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public IdEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {

        if (jdbcType == null) {
            ps.setInt(i, parameter.getId());
        } else {
            ps.setObject(i, parameter.getId(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {

        Integer id = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return IdEnum.getById(type, id);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        Integer id = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return IdEnum.getById(type, id);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        Integer id = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return IdEnum.getById(type, id);
    }
}
