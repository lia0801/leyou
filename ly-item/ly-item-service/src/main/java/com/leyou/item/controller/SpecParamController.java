package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecParamController {
    @Autowired
    SpecParamService specParamService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGronp(@PathVariable("cid") Long id){
        List<SpecGroup> specGroupList=specParamService.querySpecGronp(id);
        if(null!=specGroupList&&specGroupList.size()>0){
            return ResponseEntity.ok(specGroupList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(value = "gid",required = false) Long gid,
                                                          @RequestParam(value = "cid",required = false) Long cid,
                                                          @RequestParam(value = "searching",required = false) Boolean searching,
                                                          @RequestParam(value = "generic",required = false) Boolean generic){
        List<SpecParam> specParamList= specParamService.querySpecParam(gid,cid,searching,generic);
        if(null!=specParamList&&specParamList.size()>0){
            return ResponseEntity.ok(specParamList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
