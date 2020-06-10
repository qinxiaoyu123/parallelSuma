package com.tju.suma.index;

import com.tju.suma.test.Parallel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ThreeKeyMap {
    private static final ConcurrentHashMap<Integer, Map<Integer, Map<Integer, Integer>>> IndexThreeKey = new ConcurrentHashMap<>();

    //单线程用
    public static boolean checkDuplicate(int rs, int rp, int ro, int index) {

        if (IndexThreeKey.containsKey(rp)) {
            //rp rs ro index
            Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
            if (IndexTwoKey.containsKey(rs)) {
                Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                if (IndexOneKey.containsKey(ro)) {
                    return true;
                } else {
                    IndexOneKey.put(ro, index);
                    return false;
                }
            } else {
                IndexTwoKey.put(rs, new HashMap<Integer, Integer>() {
                    {
                        put(ro, index);
                    }
                });
                return false;

            }

        } else {
            IndexThreeKey.put(rp, new HashMap<Integer, Map<Integer, Integer>>() {{
                put(rs, new HashMap<Integer, Integer>() {{
                    put(ro, index);
                }});
            }});
            return false;
        }
//        return false;

    }

    public static boolean checkDuplicate(int rs, int rp, int ro) {
//        synchronized (IndexThreeKey) {
            if (IndexThreeKey.containsKey(rp)) {
                //rp rs ro index
                Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
                if (IndexTwoKey.containsKey(rs)) {
                    Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                    return IndexOneKey.containsKey(ro);
                }
            }
            return false;
//        }
    }

    public static boolean checkDuplicateBeforeIndex(int index, int rs, int rp, int ro) {
        synchronized (IndexThreeKey) {
            if (IndexThreeKey.containsKey(rp)) {
                //rp rs ro index
                Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
                if (IndexTwoKey.containsKey(rs)) {
                    Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                    if (IndexOneKey.containsKey(ro)) {
                        int indexTmp = IndexOneKey.get(ro);
                        return indexTmp < index;
                    } else return false;
                }
            }
            return false;
        }
    }

    //return 0 表示已经存在不添加，return 非0表示不存在，并且已经更新到索引中了
    //这个函数目的是将判断和添加锁在一起
    public static int checkDuplicateIfNotThenAdd(int rs, int rp, int ro) {
        int indexTmp = 0;
        synchronized (IndexThreeKey) {
            if (IndexThreeKey.containsKey(rp)) {
                //rp rs ro index
                Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
                if (IndexTwoKey.containsKey(rs)) {
                    Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                    if (IndexOneKey.containsKey(ro)) {
                        return indexTmp;
                    } else {
                        indexTmp = Parallel.index.getAndIncrement();
                        IndexOneKey.put(ro, indexTmp);
                        return indexTmp;
                    }
                } else {
                    indexTmp = Parallel.index.getAndIncrement();
                    int finalIndexTmp1 = indexTmp;
                    IndexTwoKey.put(rs, new HashMap<Integer, Integer>() {
                        {
                            put(ro, finalIndexTmp1);
                        }
                    });
                    return indexTmp;

                }

            } else {
                indexTmp = Parallel.index.getAndIncrement();
                int finalIndexTmp = indexTmp;
                IndexThreeKey.put(rp, new HashMap<Integer, Map<Integer, Integer>>() {{
                    put(rs, new HashMap<Integer, Integer>() {{
                        put(ro, finalIndexTmp);
                    }});
                }});
                return indexTmp;
            }
        }
    }

}
