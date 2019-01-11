//package com.linlazy.mmorpg.module.common.addition;
//
//import com.linlazy.mmorpg.module.user.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
///**
// * @author linlazy
// */
//@Component
//public class AdditionService {
//
//    @Autowired
//    private UserService userService;
//
//
//    public void addAddition(long actorId, List<Addition> additionList){
//
//        for(Addition addition : additionList){
//            switch (addition.getAdditionType()){
//                case AdditionType.PHYSICAL_ATTACK:
//                    userService.addOrRemoveAddition(actorId,addition);
//                    break;
//                    default:
//                        break;
//            }
//        }
//    }
//
//    public void removeAddition(long actorId, List<Addition> additionList){
//
//    }
//}
