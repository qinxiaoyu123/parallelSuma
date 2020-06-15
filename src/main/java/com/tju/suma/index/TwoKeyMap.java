package com.tju.suma.index;

import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TwoKeyMap {
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> IspTwoKey = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> IopTwoKey = new ConcurrentHashMap<>();


    public static int getFirstIndexSpFromMap(int rs, int rp, int index) {
//        synchronized (IspTwoKey){
        if (IspTwoKey.containsKey(rs)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IspTwoKey.get(rs);
            if (IpTwoKey.containsKey(rp)) {
                return IpTwoKey.get(rp);
            } else {
                IpTwoKey.put(rp, index);
                return -1;
            }

        } else {
            IspTwoKey.put(rs, new ConcurrentHashMap<Integer, Integer>() {{
                put(rp, index);
            }});
            return -1;

        }
//        }
    }

    public static void updateIndexSpFromMap(int rs, int rp, int index) {
        if (IspTwoKey.containsKey(rs)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IspTwoKey.get(rs);
            IpTwoKey.put(rp, index);
        } else {
            IspTwoKey.put(rs, new ConcurrentHashMap<Integer, Integer>() {{
                put(rp, index);
            }});
        }
    }

    public static void updateIndexOpFromMap(int rp, int ro, int index) {
        if (IopTwoKey.containsKey(ro)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IopTwoKey.get(ro);
            IpTwoKey.put(rp, index);
        } else {
            IopTwoKey.put(ro, new ConcurrentHashMap<Integer, Integer>() {{
                put(rp, index);
            }});
        }
    }


    public static int getFirstIndexOpFromMap(int rp, int ro, int index) {
//        synchronized (IopTwoKey){
        if (IopTwoKey.containsKey(ro)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IopTwoKey.get(ro);
            if (IpTwoKey.containsKey(rp)) {
                return IpTwoKey.get(rp);
            } else {
                IpTwoKey.put(rp, index);
                return -1;
            }

        } else {
            IopTwoKey.put(ro, new ConcurrentHashMap<Integer, Integer>() {{
                put(rp, index);
            }});
            return -1;
        }
//        }

    }

    public static synchronized int getFirstIndexSpFromMap(int rs, int rp) {
//        synchronized (IspTwoKey){
        if (IspTwoKey.containsKey(rs)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IspTwoKey.get(rs);
            if (IpTwoKey.containsKey(rp)) {
                return IpTwoKey.get(rp);
            }
        }
        return -1;
//        }
    }


    public static int getFirstIndexOpFromMap(int rp, int ro) {
//        synchronized (IopTwoKey){
        if (IopTwoKey.containsKey(ro)) {
            ConcurrentHashMap<Integer, Integer> IpTwoKey = IopTwoKey.get(ro);
            if (IpTwoKey.containsKey(rp)) {
                return IpTwoKey.get(rp);
            }
        }
        return -1;
//        }
    }


    public static List<Integer> findAllTriplesFromRs(int tmp) {
        List<Integer> rpRoTriples = new ArrayList<>();
//        synchronized (IspTwoKey){
        if (IspTwoKey.containsKey(tmp)) {
            ConcurrentHashMap<Integer, Integer> indexBean = IspTwoKey.get(tmp);
            for (ConcurrentHashMap.Entry<Integer, Integer> tt : indexBean.entrySet()) {
                int firstTripleIsp = tt.getValue();
                int rp = tt.getKey();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIsp;
                do {
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    Objects.requireNonNull(dicDataBeanIterator, "dicDataBeanIterator is null");
                    indexNew = dicDataBeanIterator.getNsp();
                    int roTmp = dicDataBeanIterator.getRo();
                    rpRoTriples.add(rp);
                    rpRoTriples.add(roTmp);
                } while (indexNew != -1);
            }
        }
//        }
        return rpRoTriples;
    }

    public static List<Integer> findAllTriplesFromRo(int tmp) {
        List<Integer> rsRpTriples = new ArrayList<>();
//        synchronized (IopTwoKey){
        if (IopTwoKey.containsKey(tmp)) {
            ConcurrentHashMap<Integer, Integer> indexBean = IopTwoKey.get(tmp);
            for (ConcurrentHashMap.Entry<Integer, Integer> tt : indexBean.entrySet()) {
                int firstTripleIop = tt.getValue();
                int rp = tt.getKey();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIop;
                do {
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    Objects.requireNonNull(dicDataBeanIterator, "dicDataBeanIterator is null");
                    indexNew = dicDataBeanIterator.getNop();
                    int roTmp = dicDataBeanIterator.getRs();
                    rsRpTriples.add(roTmp);
                    rsRpTriples.add(rp);
                } while (indexNew != -1);
            }
        }
//        }
        return rsRpTriples;
    }

    public static void replaceWithMinIop(int ii, int minNew) {
        List<Integer> rsRpTriples = findAllTriplesFromRo(ii);
        Iterator<Integer> rsRpList = rsRpTriples.iterator();
        while (rsRpList.hasNext()) {
            int rs = rsRpList.next();
            int rp = rsRpList.next();
            DicRdfDataMap.addNewRdfDataBeanParallel(rs, rp, minNew);
        }
    }

    public static void replaceWithMinIsp(int ii, int minNew) {
        List<Integer> rpRoTriples = findAllTriplesFromRs(ii);
        Iterator<Integer> rpRoList = rpRoTriples.iterator();
        while (rpRoList.hasNext()) {
            int rp = rpRoList.next();
            int ro = rpRoList.next();
            DicRdfDataMap.addNewRdfDataBeanParallel(minNew, rp, ro);
        }
    }


//    public static boolean checkDuplicate(int firstTripleIsp, int ro) {
//        DicRdfDataBean dataBean;
//        int indexNew = firstTripleIsp;
//        if(indexNew == -1){
//            return false;
//        }
//        do{
//            dataBean = DicRdfDataMap.getDataBean(indexNew);
//            Objects.requireNonNull(dataBean,"dataBean is null");
//            indexNew = dataBean.getNsp();
//            int roIterator = dataBean.getRo();
//            if(ro == roIterator) return true;
//        }while(indexNew != -1);
//        return false;
//    }
//    //Isp = 1, Isp ; Isp = 0; Ipo
//    public static boolean checkDuplicate(int rs, int rp, int ro) {
//        int firstIndex = getFirstIndexSpFromMap(rs,rp);
//        if(firstIndex == -1){
//            return false;
//        }
//        return checkDuplicate(firstIndex, ro);
//    }
}
