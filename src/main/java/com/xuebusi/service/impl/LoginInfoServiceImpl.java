package com.xuebusi.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuebusi.common.cache.BaseDataCacheUtils;
import com.xuebusi.entity.LoginInfo;
import com.xuebusi.entity.User;
import com.xuebusi.repository.LoginInfoRepository;
import com.xuebusi.repository.UserRepository;
import com.xuebusi.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户
 * Created by SYJ on 2017/10/15.
 */
@Service
public class LoginInfoServiceImpl implements LoginInfoService {

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginInfo findOne(Integer id) {
        return loginInfoRepository.findOne(id);
    }

    @Override
    public List<LoginInfo> findAll() {
        return loginInfoRepository.findAll();
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public LoginInfo findByUsername(String username) {
        //先查缓存
        LoginInfo loginInfo = BaseDataCacheUtils.getUserCacheMap().get(username);
        if (loginInfo != null) {
            System.out.println(">>>>>> 读取缓存用户数据: " + JSON.toJSONString(loginInfo));
            return loginInfo;
        }
        return loginInfoRepository.findByUsername(username);
    }

    /**
     * 保存注册信息
     * @param loginInfo
     * @return
     */
    @Override
    public LoginInfo save(LoginInfo loginInfo) {
        LoginInfo newLoginInfo = loginInfoRepository.save(loginInfo);
        //同时生成一条用户基本信息
        User user = new User();
        user.setUsername(loginInfo.getUsername());
        userRepository.save(user);

        //更新缓存
        BaseDataCacheUtils.getUserCacheMap().put(loginInfo.getUsername(), newLoginInfo);
        System.out.println(">>>>>> 更新缓存: " + JSON.toJSONString(newLoginInfo));
        return newLoginInfo;
    }
}
