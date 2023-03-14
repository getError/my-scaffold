package cn.geterror.serialization.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

public class DuplicateKeyUpdateMethod extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        //脚本模板
        String sql = "<script>\nINSERT INTO %s (%s)\nVALUES\n" +
                "<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n %s \n</foreach>\n" +
                "ON DUPLICATE KEY UPDATE\n%s\n</script>";
        //字段list（除了主键）
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        //全部字段
        List<String> allFieldName = fieldList.stream()
                .map(TableFieldInfo::getColumn)
                .collect(Collectors.toList());
        allFieldName.add(0, tableInfo.getKeyColumn());

        //生成所需的sql片段
        String fieldNames = String.join(", ", allFieldName);
        String insertFields = allFieldName.stream()
                .map(field -> "#{item." + field + "}")
                .collect(Collectors.joining(", "));
        insertFields = "(" + insertFields + ")";
        String updateFields = fieldList.stream()
                .map(field -> {
                    String column = field.getColumn();
                    return column + "=values(" + column + ")";
                })
                .collect(Collectors.joining(",\n"));

        //生成具体脚本
        String sqlResult = String.format(sql, tableInfo.getTableName(), fieldNames, insertFields, updateFields);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "duplicateKeyUpdate", sqlSource);
    }


}