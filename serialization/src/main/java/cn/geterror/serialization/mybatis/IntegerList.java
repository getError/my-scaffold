package com.sankuai.blue.infra.serialize;


import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
public class IntegerList extends ArrayList<Integer> {
    public IntegerList(Collection<? extends Integer> c){
        super(c);
    }


}
