package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户操作
 * check  检查是否注册过
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX="user:code:phone:";

    //注册时检查用户名是否注册过
    public Boolean check(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
        };
        return userMapper.selectCount(user)!=1; //true
    }

    public Boolean sendVerifyCode(String phone) {
        //产生验证码
        String s = NumberUtils.generateCode(5);

        //把验证码放到redis
        redisTemplate.opsForValue().set(KEY_PREFIX+phone,s);
        //发短信
        //调用第三方接口
        return  true;
    }

    //用户注册
    public Boolean createUser(User user, String code) {
        String s=redisTemplate.opsForValue().get(KEY_PREFIX+user.getPhone());
        //验证取值没有
        if(null==s){
            return false;
        }
        //去到数据，但是填错了
        if(!code.equals(s)){
            return false;
        }
        //插入数据
        //生成盐
        String s1 = CodecUtils.generateSalt();
        user.setSalt(s1);
        //加密
        String newPassword = CodecUtils.md5Hex(user.getPassword(), s1);
        //设置密码
        user.setPassword(newPassword);
        //时间
        user.setCreated(new Date());
        boolean flag=userMapper.insertSelective(user)==1;
        if (flag){
            redisTemplate.delete(s);
        }
        return flag;
    }

    public User queryUser(String username, String password) {
        User user = new User();
        user.setUsername(username);

        User user1 = userMapper.selectOne(user);
        if(null==user1){
            return null;
        }
        //取盐
        String salt = user1.getSalt();
        //加密密码和数据库密码对比
        String newpassword = CodecUtils.md5Hex(password, salt);
        if(!user1.getPassword().equals(newpassword)){
            return null;
        }
        return user1;
    }
}
