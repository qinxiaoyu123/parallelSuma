package com.tju.suma.bean;

import com.tju.suma.index.ThreeKeyMap;
import com.tju.suma.index.TwoKeyMap;
import com.tju.suma.test.Parallel;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DicRdfDataMap {

    private static final Map<Integer, DicRdfDataBean> dicDataMap = new ConcurrentHashMap<>();
    private static final Map<Integer, DicRdfDataBean> dicStashMap = new ConcurrentHashMap<>();
    private static final Map<Integer, DicRdfDataBean> dicIteratorMap = new ConcurrentHashMap<>();

    public static final Object mutex = new Object();

    private DicRdfDataMap() {
    }

    public static Map<Integer, DicRdfDataBean> getDicDataMap() {
        return dicDataMap;
    }

    public static Map<Integer, DicRdfDataBean> getDicStashMap() {
        return dicStashMap;
    }

    public static Map<Integer, DicRdfDataBean> getDicIteratorMap() {
        return dicIteratorMap;
    }


    public static DicRdfDataBean getDataBean(int index) {
        DicRdfDataBean dataBean = dicDataMap.get(index);
        Objects.requireNonNull(dataBean, "dataBean at DicRdfDataMap is null");
        return dataBean;
    }


    public static void addSourceRdfDataBean(int index, int rs, int rp, int ro) {
        DicRdfDataBean dicDataBean = new DicRdfDataBean();
        dicDataBean.setRs(rs);
        dicDataBean.setRp(rp);
        dicDataBean.setRo(ro);
        int nsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp, index);
        dicDataBean.setNsp(nsp, index);
        int nop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro, index);
        dicDataBean.setNop(nop, index);
        dicDataMap.put(index, dicDataBean);
        ThreeKeyMap.checkDuplicate(rs, rp, ro, index);
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

        synchronized (mutex) {
            int nsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp, index);

            dicDataBean.setNsp(nsp, index);

            int nop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro, index);

            dicDataBean.setNop(nop, index);

            dicDataMap.put(index, dicDataBean);
        }
//        //todo:改代码去掉mutex
////        synchronized (mutex) {
//        int nsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp);
////            dicDataBean.setNsp(nsp, index);
//
//        int nop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro);
////            dicDataBean.setNop(nop, index);
//        dicDataMap.put(index, dicDataBean);
//
//        TwoKeyMap.updateIndexSpFromMap(rs, rp, index);
//        TwoKeyMap.updateIndexOpFromMap(rp, ro, index);
//        if (nsp != -1) {
//            dicDataMap.get(index).setNsp(nsp, index);
//        }
//
//        if (nop != -1) {
//            dicDataMap.get(index).setNop(nop, index);
//        }
//            else{
//                dicDataMap.get(index).setNop(nop, index);
//            }
//        }
    }

//    public static void addNewRdfDataBeanParallelWithOutSynchronized(int rs, int rp, int ro) {
//        int index = ThreeKeyMap.checkDuplicateIfNotThenAdd(rs, rp, ro);
//        //已经存在
//        if (index == 0) {
//            return;
//        }
//        DicRdfDataBean dicDataBean = new DicRdfDataBean();
//        dicDataBean.setRs(rs);
//        dicDataBean.setRp(rp);
//        dicDataBean.setRo(ro);
//
//
//        int nsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp, index);
//        dicDataBean.setNsp(nsp, index);
//        int nop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro, index);
//        dicDataBean.setNop(nop, index);
//        dicDataMap.put(index, dicDataBean);
//
//
//    }


}
