package com.leyou.item.bo;

import com.leyou.item.pojo.Spu;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class SpuBo extends Spu {

    @Transient
    private  String cname;//分类名称
    @Transient
    private  String bname;//品牌名称

}
