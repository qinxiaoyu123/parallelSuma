package com.tju.suma.test;

import com.tju.suma.bean.DicOwlBean;
import com.tju.suma.bean.DicOwlMap;
import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.dictionary.Dictionary;
import com.tju.suma.index.ThreeKeyMap;
import com.tju.suma.reason.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class MyNewTask implements Callable<Boolean> {
    //主线程控制，有新的元组就commit
//    DicRdfDataBean rdfData;
    int begin;
    Map<Integer, DicRdfDataBean> dicDataMap;
    Map<String, List<DicOwlBean>> dicRuleMap;
    private static Logger log = Logger.getLogger(MyNewTask.class);
    int end;
    public MyNewTask(int begin, int end) {
        this.begin = begin;
        this.dicDataMap = DicRdfDataMap.getDicDataMap();
        this.dicRuleMap = DicOwlMap.getRuleMap();
        this.end = end;
    }

    @Override
    public Boolean call() throws Exception {
       for(int i = begin;i<=end;i++){
           DicRdfDataBean rdfData = dicDataMap.get(i);
           //从大数据表中获取一个元组
           List<String> ruleKey = new ArrayList<>();

           int Rs = rdfData.getRs();
           int Rp = rdfData.getRp();
           int Ro = rdfData.getRo();
//           boolean rsBool = SameAsReason.boolSameAs(Rs);
//           boolean rpBool = SameAsReason.boolSameAs(Rp);
//           boolean roBool = SameAsReason.boolSameAs(Ro);
//           if (!(rsBool && rpBool && roBool)) {
//               return true;
//           }

           DicSerialReason.convertDataToRuleKey(ruleKey, Rp, Ro);
//           log.info(Dictionary.getDecode()[Rs]+" "+Dictionary.getDecode()[Rp]+" "+Dictionary.getDecode()[Ro]);
           ruleKey.forEach(str -> {
               if (dicRuleMap.containsKey(str)) {
                   List<DicOwlBean> OwlRule = dicRuleMap.get(str);
                   for (DicOwlBean typeHead : OwlRule) {
                       int type = typeHead.getType();
                       List<Integer> head = typeHead.getRuleHead();
                       simpleSwitchReasonType(Rs, Rp, Ro, type, head);
                   }
               }
           });
       }

        return true;
    }

    public static void simpleSwitchReasonType(int rs, int rp, int ro, int type, List<Integer> head) {
//        System.out.println(Thread.currentThread().getName());
        switch (type) {
            case 2013://SubObjectPropertyOf 2013
                SubPropertyReason.reason(rs, head, ro);
                break;
            case 2022://ObjectPropertyDomain 2022
            case 2002://SubClassOf 2002
                BasicReason.reason(rs, head);
                break;
            case 2023://ObjectPropertyRange 2023
                BasicReason.reason(ro, head);
                break;
            case 3005:
                ObjectSomeValuesFromReason.reason(rs, head);
                break;
            case 0:
                QueryReason.equivalentClass2Reason(rs, head);
                break;
            case 1:
                QueryReason.equivalentRoleReason(rs, ro, head);
                break;
            case 2:
                QueryReason.equivalentClass3Reason(rs, head);
                break;
            case 2019:
                TransitiveReason.reason(rs, rp, ro);
                break;
            case 2014://InverseProperty = 2014;
                InversePropertyReason.reason(rs, head, ro);
                break;
            case 2012://EquivalentProperty = 2012;
                EquivalentPropertyReason.reason(rs, head, ro);
                break;
            case 3006:
                ObjectAllValuesFromReason.reason(rs, head);
                break;
            case 3007:
                ObjectHasValueReason.reason(rs, head);
                break;
            case 3008:
                ObjectMinCardinalityReason.reason(rs, head);
                break;
            case -3006://ObjectAllValuesFrom
                ObjectAllValuesFromReason.inverseReason(rs, head);
                break;
            case 2017:
                SymmetricObjectPropertyReason.reason(rs, rp, ro);
                break;
            case 2016:
                FunctionalObjectPropertyReason.inverseReason(rs, rp, ro);
                break;
            case 2015:
                FunctionalObjectPropertyReason.reason(rs, rp, ro);
                break;
//
            case 10:
                QueryReason.type10Reason(rs, ro, head);
                break;
            case 11:
                QueryReason.type11Reason(ro, head);
                break;
            case 12:
                QueryReason.type12Reason(rs, rp, head);
                break;
            case 14:
                QueryReason.type14Reason(rs, head);
                break;
            case 15:
                QueryReason.type15Reason(rs, head);
                break;
            case 16:
            case 17:
                QueryReason.type16Reason(rs, head);
                break;
            case 18:
                QueryReason.type18Reason(rs, rp, head);
                break;
            case 19:
                QueryReason.type19Reason(rs, head);
                break;
            case 20:
                QueryReason.type20Reason(rs, head);
                break;
            case 21:
                QueryReason.type21Reason(rs, head);
                break;
            case 22:
                QueryReason.type22Reason(rs, head);
                break;
            case 23:
                QueryReason.type23Reason(rs, head);
                break;
            case 24:
                QueryReason.type24Reason(rs, head);
                break;
            case 25:
                QueryReason.type25Reason(rs, head);
                break;
            case 26:
                QueryReason.type26Reason(rs, head);
                break;
            case 27:
                QueryReason.type27Reason(rs, head);
                break;
            case 28:
                QueryReason.type28Reason(rs, head);
                break;
            case 29:
                QueryReason.type29Reason(rs, ro, head);
                break;
            case 30:
                QueryReason.type30Reason(rs, head);
                break;
            case 31:
                QueryReason.type31Reason(rs, head);
                break;
            case 32:
                QueryReason.type32Reason(rs, head);
                break;
            case 33:
                QueryReason.type33Reason(rs, ro, head);
                break;
            case 34:
                QueryReason.type34Reason(rs, head);
                break;
            case 35:
                QueryReason.type35Reason(rs, ro, head);
                break;
            case 36:
                QueryReason.type36Reason(rs, head);
                break;
            case 37:
                QueryReason.type37Reason(rs, head);
                break;
            case 38:
                QueryReason.type38Reason(rs, head);
                break;
            case 39:
                QueryReason.type39Reason(rs, ro, head);
                break;
            case 55:
                QueryReason.type55Reason(rs, ro, head);
                break;
            default:
                break;
//                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
