package com.happy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.happy.ask.AskMoneyService;
import com.happy.dao.SharesTypeDao;
import com.happy.domain.SharesType;
import com.happy.en.AskMoneyReqEnum;
import com.happy.net.ClientService;
import com.happy.service.SharesTypeService;
import com.happy.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SharesTypeServiceImpl extends ServiceImpl<SharesTypeDao, SharesType> implements SharesTypeService {

    @Autowired
    private AskMoneyService askMoneyService;

    @Override
    public void synchronization() {
        Lists.partition(findByNet(), 200).forEach(item -> saveOrUpdateBatch(item));

    }

    @Override
    public List<SharesType> findByNet() {
        List<Map<String, String>> result = askMoneyService.defGet(ClientService.Api.ASK_MONEY_STRATEGY, AskMoneyReqEnum.SHARES_TYPE);
        return Kit.parseArray(AskMoneyReqEnum.SHARES_TYPE.result(result), SharesType.class);
    }


}
