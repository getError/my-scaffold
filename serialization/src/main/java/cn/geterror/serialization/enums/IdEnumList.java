package cn.geterror.serialization.enums;

import java.util.ArrayList;
import java.util.Collection;

public class IdEnumList<E extends  Enum<E> & IdEnum<E>> extends ArrayList<IdEnum<E>> {
    private Class<E> type;
    public IdEnumList(Collection<? extends IdEnum<E>> c){
        super(c);
    }
}
