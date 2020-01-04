package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Spu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    SpuMapper spuMapper;
    @Autowired
    SpuDetailMapper detailMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandMapper brandMapper;

    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //开启分页
        PageHelper.startPage(page,rows);
        //查询对象
        Example example=new Example(Spu.class);
        //获取Criteria
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(null!=saleable){
            criteria.andEqualTo("saleable",saleable);
        }
        Page<Spu> spuPage= (Page<Spu>) spuMapper.selectByExample(example);
        List<Spu> result = spuPage.getResult();
        List<SpuBo> spuBos = new ArrayList<>();
        for (Spu spu:result){
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
            // 通过分类id得到 分类名称                              把数组变成list
            List<String> names=categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            //拼接
            String join=StringUtils.join(names,"/");
            //分类名称
            spuBo.setCname(join);
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            spuBos.add(spuBo);
            spuBos.add(spuBo);
        }
        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),spuBos);
    }
}
