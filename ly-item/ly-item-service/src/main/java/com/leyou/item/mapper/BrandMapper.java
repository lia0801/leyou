package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values (#{i},#{id})")
    void insertBrandCategory(@Param("i") Long  i,@Param("id") Long id);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteBrandcategory(@Param("id") Long id);

    @Select("select b.* from tb_brand b,tb_category_brand cb where b.id=brand_id and category_id=#{id}")
    List<Brand> queryBrandByCategory(@Param("id") Long id);

}
