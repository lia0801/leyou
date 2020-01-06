package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    SkuMapper skuMapper;
    @Autowired
    StockMapper stockMapper;;

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

    //商品添加
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());
        //保存数据
        spuMapper.insertSelective(spuBo);
        Long id=spuBo.getId();
        SpuDetail spuDetail=spuBo.getSpuDetail();
        spuDetail.setSpuId(id);
        detailMapper.insert(spuDetail);
        List<Sku> skus = spuBo.getSkus();
        //保存sku和库存表
        saveskus(spuBo,skus);
    }

    //保存sku表
    private void saveskus(SpuBo spuBo, List<Sku> skus) {
        for (Sku s:skus){
            s.setSpuId(spuBo.getId());
            s.setCreateTime(new Date());
            s.setLastUpdateTime(new Date());
            //保存
            skuMapper.insertSelective(s);
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            stock.setStock(s.getStock());
            stockMapper.insert(stock);
        }
    }

    public SpuDetail querySpuDetailBySpuId(Long id) {
        return  detailMapper.selectByPrimaryKey(id);
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList=skuMapper.select(sku);
        for (Sku s:skuList){
            Long id1 = s.getId();
            Stock stock = stockMapper.selectByPrimaryKey(id1);
            s.setStock(stock.getStock());
        }
        return skuList;
    }

    public void updateGoods(SpuBo spuBo) {

//spu，spuDetail可以直接更新
        spuBo.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spuBo);

        detailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //先删除当前spu对应的所有的sku，然后重新添加
        //先查，再根据查到的结果删除
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        //从数据库中查询到的skus
        List<Sku> skus = skuMapper.select(record);
        //根据sku信息，删除对应数据库中的信息
        //删除库存数据
        for (Sku s:skus){
            stockMapper.deleteByPrimaryKey(s.getId());
            this.skuMapper.delete(s);
        }
       //新增
        saveskus(spuBo, spuBo.getSkus());
    }
}