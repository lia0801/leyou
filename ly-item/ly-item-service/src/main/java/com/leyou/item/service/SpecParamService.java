package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    SpecParamMapper specParamMapper;

    @Autowired
    SpecGroupMapper specGroupMapper;

    public List<SpecGroup> querySpecGronp(Long id) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(id);
        //通过分类的Id查询规格参数
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        //查询规格参数的规格组
        specGroups.forEach(t->{
            Long id1 = t.getId();
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(id1);
            List<SpecParam> specParamList = specParamMapper.select(specParam);
        });
        return specGroups;
    }

    public List<SpecParam> querySpecParam( Long gid, Long cid, Boolean searching,Boolean generic) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        return specParams;
    }
}
