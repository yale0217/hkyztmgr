package com.xiaoi.south.manager.service.impl;

import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.dao.LoginOnDao;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import com.xiaoi.south.manager.service.LoginOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LoginOnService")
public class LoginOnServiceImpl implements LoginOnService {
    @Autowired
     LoginOnDao loginOnDao;
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int getCount(String username, String password) {
        return loginOnDao.getCount(username,password);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int getCountSC(String username, String password) {
        return loginOnDao.getCount(username,password);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int getCountEN(String username, String password) {
        return loginOnDao.getCount(username,password);
    }
}
