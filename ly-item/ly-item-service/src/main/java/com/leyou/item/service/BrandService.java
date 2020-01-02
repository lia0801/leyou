package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.po.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 品牌
 */
@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> pageQuery(int page, int rows, String sortBy, Boolean desc, String key) {
        //开启分页
        PageHelper.startPage(page,rows);
        //查询条件
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            Example.Criteria criteria=example.createCriteria();
            criteria.andLike("name","%"+key+"%");
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            //order by 添加空格
            example.setOrderByClause(sortBy+(desc?" desc":" asc"));
        }
        //查询
        Page<Brand> brands = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(brands.getTotal(),new Long(brands.getPages()),brands.getResult());
    }

    //品牌添加数据
    @Transactional //事务
    public void addBrand(Brand brand, List<Long> cids) {
        //插入tb_brand
        brandMapper.insertSelective(brand);
        //插入关系表中数据tb_category_brand
        for (Long i:cids){
            brandMapper.insertBrandCategory(i,brand.getId());
        }
    }
}
