package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.mybatis.StringListEncode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 自定义 List<String> 到 String 的编码方式
 * 规则:
 *  1. 不接受 null 元素
 *  2. 元素里所有的 ',' 字符，使用 '\,' 转义, '\' 字符 使用 '\\' 转义
 *  3. 元素支持控制字符转义、八进制转义 和 Unicode转义
 *  3. 元素之间用 ',' 分割
 *  4. 当最后的元素为空字符串，增加 ',' 后缀用于区分
 *
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {

        String listValue = StringListEncode.encodeStringList(parameter);

        if (jdbcType == null) {
            ps.setString(i, listValue);
        } else {
            ps.setObject(i, listValue, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonValue = rs.getString(columnName);
        return StringListEncode.decodeStringList(jsonValue);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonValue = rs.getString(columnIndex);
        return StringListEncode.decodeStringList(jsonValue);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonValue = cs.getString(columnIndex);
        return StringListEncode.decodeStringList(jsonValue);
    }

}
