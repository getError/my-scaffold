package cn.geterror.serialization.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class IntegerListTypeHandler extends BaseTypeHandler<IntegerList> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IntegerList parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    @Override
    public IntegerList getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Override
    public IntegerList getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Override
    public IntegerList getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }
}
