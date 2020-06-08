package com.tju.suma.reason;

import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.index.ThreeKeyMap;
import com.tju.suma.test.Parallel;

import java.util.List;
import java.util.Map;

public class SubPropertyReason {
    public static void reason(int rs, List<Integer> head, int ro) {
        int rp = head.get(0);
        //TODO 将重复检测内置到添加元组的函数中
        DicRdfDataMap.addNewRdfDataBeanParallel(rs, rp, ro);


    }
}
