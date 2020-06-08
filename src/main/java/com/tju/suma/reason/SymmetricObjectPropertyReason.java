package com.tju.suma.reason;

import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.index.ThreeKeyMap;

public class SymmetricObjectPropertyReason {
    public static void reason(int rs, int rp, int ro) {
        DicRdfDataMap.addNewRdfDataBeanParallel(ro, rp, rs);
    }
}
