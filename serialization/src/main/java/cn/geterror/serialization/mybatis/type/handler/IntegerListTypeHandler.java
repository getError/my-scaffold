package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.mybatis.IntegerList;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@MappedTypes(IntegerList.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class IntegerListTypeHandler extends BaseTypeHandler<IntegerList> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IntegerList parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    @Override
    public IntegerList getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        if(string==null){
            return new IntegerList();
        }
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Override
    public IntegerList getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        if(string==null){
            return new IntegerList();
        }
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Override
    public IntegerList getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        if(string==null){
            return new IntegerList();
        }
        return new IntegerList(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }
}
