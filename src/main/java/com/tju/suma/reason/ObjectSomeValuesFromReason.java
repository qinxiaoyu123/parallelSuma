package com.tju.suma.reason;

import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.index.ThreeKeyMap;
import com.tju.suma.index.TwoKeyMap;
import com.tju.suma.test.Parallel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tju.suma.reason.DicSerialReason.*;

public class ObjectSomeValuesFromReason {
    //A subclassof R.C


    public static void reason(int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp);
        if (firstTripleIsp != -1) {
            if (class2 == 1) {
                return;
            }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do {
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                Objects.requireNonNull(dicDataBeanIterator, "dicDataBeanIterator at ObjectSomeValuesFromReason");
                indexNew = dicDataBeanIterator.getNsp();
                int ro = dicDataBeanIterator.getRo();
                if (ThreeKeyMap.checkDuplicate(ro, typeEncode, class2)) {
                    return;
                }
            } while (indexNew != -1);
        }
        addSomeValueFrom(rs, rp, class2);
    }

    private static void addSomeValueFrom(int rs, int rp, int class2) {
        int ro = Parallel.anonymous.getAndDecrement();

        addNewRdfDataBeanParallel(rs, rp, ro);
        addNewRdfDataBeanParallel(ro, typeEncode, class2);
    }


    public static void addNewRdfDataBeanParallel(int rs, int rp, int ro) {

        int index = ThreeKeyMap.checkDuplicateIfNotThenAdd(rs, rp, ro);
        //已经存在
        if (index == 0) {
            return;
        }
        DicRdfDataBean dicDataBean = new DicRdfDataBean();
        dicDataBean.setRs(rs);
        dicDataBean.setRp(rp);
        dicDataBean.setRo(ro);


        int nsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp, index);
        dicDataBean.setNsp(nsp, index);
        int nop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro, index);
        dicDataBean.setNop(nop, index);
        DicRdfDataMap.getDicDataMap().put(index, dicDataBean);


    }
}
