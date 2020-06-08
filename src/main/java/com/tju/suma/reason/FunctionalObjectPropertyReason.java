package com.tju.suma.reason;

import com.tju.suma.dictionary.Dictionary;

import java.util.HashSet;

import static com.tju.suma.bean.DicRdfDataMap.mutex;
import static com.tju.suma.reason.SameAsReason.findEquivPoolIndex;

public class FunctionalObjectPropertyReason {
    public static final Object object = new Object();
    public static void reason(int rs, int rp, int ro) {
//        System.out.println(Thread.currentThread().getName()+" "+Dictionary.getDecode()[rs]+" "+Dictionary.getDecode()[rp]+" "+Dictionary.getDecode()[ro]);

                int rsEquiv = findEquivPoolIndex(rs);
                //没有相等individual
                if (rsEquiv == 0) {
                    SameAsReason.loopRsRpFindRo(rs, rp, ro);
                } else {
                    HashSet<Integer> set = SameAsReason.equiPool.get(rsEquiv - 1);
                    for (Integer tmp : set) {
                        SameAsReason.loopRsRpFindRo(tmp, rp, ro);
                    }
                }


    }

    public static void inverseReason(int rs, int rp, int ro) {
//        System.out.println(Thread.currentThread().getName()+" "+Dictionary.getDecode()[rs]+" "+Dictionary.getDecode()[rp]+" "+Dictionary.getDecode()[ro]);

                int roEquiv = findEquivPoolIndex(ro);
                //没有相等individual
                if (roEquiv == 0) {
                    SameAsReason.loopRpRoFindRs(rs, rp, ro);
                } else {
                    HashSet<Integer> set = SameAsReason.equiPool.get(roEquiv - 1);
                    for (Integer tmp : set) {
                        SameAsReason.loopRpRoFindRs(rs, rp, tmp);
                    }
                }



    }
}
