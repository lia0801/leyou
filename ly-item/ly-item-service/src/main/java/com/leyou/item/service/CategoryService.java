package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService{

    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> queryByParentId(Long id) {
        Category category = new Category();
        category.setParentId(id);
        return categoryMapper.select(category) ;
    }

    //品牌编辑 查一个
    public List<Category> queryByBrandId(Long id) {
       return categoryMapper.queryByBrandId(id);
    }

    public List<String> queryNameByIds(List<Long> asList) {
        List<String> list = new ArrayList<>();
        List<Category> categories = categoryMapper.selectByIdList(asList);
        categories.forEach(t->{
            list.add(t.getName());
        });
        return list;
    }
}
