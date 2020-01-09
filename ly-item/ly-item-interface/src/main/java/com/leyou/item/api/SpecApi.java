package com.leyou.item.api;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface SpecApi {
    @GetMapping("spec/groups/{cid}")
    public List<SpecGroup> querySpecGroups(@PathVariable("cid") Long id);

    @GetMapping("spec/params")
    public List<SpecParam> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                          @RequestParam(value = "cid",required = false) Long cid,
                                          @RequestParam(value = "searching",required = false) Boolean searching,
                                          @RequestParam(value = "generic",required = false) Boolean generic);
    //    /spec/group
    //添加规格参数
    @PostMapping("spec/group")
    public Void addGroup(@RequestBody SpecGroup specGroup);
    //修改规格参数
    @PutMapping("spec/group")
    public Void updateGroup(@RequestBody SpecGroup specGroup);

    //删除
    @DeleteMapping("spec/group/{id}")
    public Void deleteGroup(@PathVariable("id") Long id);
}
