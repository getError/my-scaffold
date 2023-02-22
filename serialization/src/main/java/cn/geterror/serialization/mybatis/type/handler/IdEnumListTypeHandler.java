package cn.geterror.serialization.mybatis.type.handler;

import cn.geterror.serialization.enums.IdEnum;
import cn.geterror.serialization.enums.IdEnumList;
import cn.geterror.serialization.mybatis.IntegerList;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class IdEnumListTypeHandler<E extends Enum<E> & IdEnum<E>> extends BaseTypeHandler<IdEnumList<E>> {

    private final Class<E> type;
    private final IntegerListTypeHandler integerListTypeHandler = new IntegerListTypeHandler();

    public IdEnumListTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IdEnumList<E> parameter, JdbcType jdbcType) throws SQLException {
        IntegerList integers = new IntegerList(parameter.stream().map(IdEnum::getId).collect(Collectors.toList()));
        integerListTypeHandler.setNonNullParameter(ps, i, integers, jdbcType);
    }

    @Override
    public IdEnumList<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        IntegerList result = integerListTypeHandler.getNullableResult(rs, columnName);
        List<IdEnum<E>> collect = result.stream().map(i -> IdEnum.getById((Class<? extends IdEnum<E>>)type, i)).collect(Collectors.toList());
        return new IdEnumList<>(collect);
    }

    @Override
    public IdEnumList<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        IntegerList result = integerListTypeHandler.getNullableResult(rs, columnIndex);
        List<IdEnum<E>> collect = result.stream().map(i -> IdEnum.getById((Class<? extends IdEnum<E>>) type, i)).collect(Collectors.toList());
        return new IdEnumList<>(collect);
    }

    @Override
    public IdEnumList<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        IntegerList result = integerListTypeHandler.getNullableResult(cs, columnIndex);
        List<IdEnum<E>> collect = result.stream().map(i -> IdEnum.getById((Class<? extends IdEnum<E>>)type, i)).collect(Collectors.toList());
        return new IdEnumList<>(collect);
    }
}
