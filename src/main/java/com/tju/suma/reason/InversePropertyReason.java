package com.tju.suma.reason;

import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.index.ThreeKeyMap;

import java.util.List;
import java.util.Map;

public class InversePropertyReason {
    public static void reason(int rs, List<Integer> head, int ro) {
        int rp = head.get(0);
        DicRdfDataMap.addNewRdfDataBeanParallel(ro, rp, rs);

    }
}
