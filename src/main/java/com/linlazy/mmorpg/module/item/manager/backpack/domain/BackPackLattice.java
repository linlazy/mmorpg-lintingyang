//package com.linlazy.mmorpg.module.item.manager.backpack.domain;
//
//import com.linlazy.mmorpg.module.item.manager.backpack.response.BackPackLatticeDTO;
//import lombok.Data;
//
//import java.util.Objects;
//
///**
// * 背包格子
// * @author linlazy
// */
//@Data
//public class BackPackLattice {
//    /**
//     * 格子位置
//     */
//    private int index;
//
//
//    private int backpackType;
//    /**
//     * 道具
//     */
//    ItemDo itemDo;
//
//    public BackPackLattice(ItemDo itemDo) {
//        this.index = itemDo.getBackPackIndex();
//        this.itemDo = itemDo;
//    }
//
//    public BackPackLattice(int index, ItemDo itemDo) {
//        this.index = index;
//        this.itemDo = itemDo;
//    }
//
//    public BackPackLattice(int backPackLatticeIndex) {
//        this.index =backPackLatticeIndex;
//    }
//
//
//    public BackPackLatticeDTO convertBackPackLatticeDTO(){
//        BackPackLatticeDTO backPackLatticeDTO = new BackPackLatticeDTO(this);
//        backPackLatticeDTO.setIndex(this.index);
//        backPackLatticeDTO.setCount(this.itemDo.getCount());
//        backPackLatticeDTO.setItemId(this.itemDo.getItemId());
//        return backPackLatticeDTO;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        BackPackLattice that = (BackPackLattice) o;
//        return index == that.index;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(index);
//    }
//}
