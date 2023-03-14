package cn.geterror.serialization.mybatis.mapper;

import java.util.List;

public interface DuplicateKeyUpdateMapper<T> {

    /**
     * 批量插入 主键相同则update
     *
     * @param list 数据集合
     * @return 更新数量
     */
    int duplicateKeyUpdate(List<T> list);
}
