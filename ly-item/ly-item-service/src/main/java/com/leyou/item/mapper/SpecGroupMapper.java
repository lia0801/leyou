package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SpecGroupMapper extends Mapper<SpecGroup> {

    @Insert("insert into tb_spec_group values(#{id},#{cid},#{name})")
    void addGroup(@Param("id") Long id,@Param("cid") Long cid,@Param("name") String name);

    @Update("update tb_spec_group set name=#{name} where id=#{id}")
    void updateGroup(@Param("id") Long id,@Param("name") String name);


}
