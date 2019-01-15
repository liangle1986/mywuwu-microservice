package com.mywuwu.service.Impl;

import com.mywuwu.entity.Test;
import com.mywuwu.mapper.TestMapper;
import com.mywuwu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 13:14
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    /**
     * 访问数据类
     */
    @Autowired
    TestMapper testMapper;

    @Override
    public Test findByUsername(Test test) {
        return testMapper.findByTestname(test.getName());
    }

    @Override
    public Test findUserById(String id) {
        return testMapper.findTestById(id);
    }
}
