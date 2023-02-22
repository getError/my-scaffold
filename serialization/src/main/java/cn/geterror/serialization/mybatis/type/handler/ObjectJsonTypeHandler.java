package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.mybatis.DataBaseSerializable;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(DataBaseSerializable.class)
public class ObjectJsonTypeHandler extends BaseTypeHandler<DataBaseSerializable> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DataBaseSerializable dataBaseSerializable, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, dataBaseSerializable.serialize());
    }

    @Override
    public DataBaseSerializable getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String dbs = resultSet.getString(s);
        return DataBaseSerializable.deserialization(dbs);
    }

    @Override
    public DataBaseSerializable getNullableResult(ResultSet resultSet, int i) throws SQLException {

        String dbs = resultSet.getString(i);
        return DataBaseSerializable.deserialization(dbs);
    }

    @Override
    public DataBaseSerializable getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String dbs = callableStatement.getString(i);
        return DataBaseSerializable.deserialization(dbs);
    }
}
