package com.mywuwu.service;

import com.mywuwu.entity.Test;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 13:14
 * @Description:
 */
public interface IUserService {
    Test findByUsername(Test test);
    Test findUserById(String id);
}
