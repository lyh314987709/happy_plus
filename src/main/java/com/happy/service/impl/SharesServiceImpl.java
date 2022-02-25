package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDao;
import com.happy.domain.Shares;
import com.happy.net.ClientService;
import com.happy.service.SharesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SharesServiceImpl extends ServiceImpl<SharesDao, Shares> implements SharesService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void synchronization() {
        List<Map<String, String>> list = clientService.post(ClientService.Api.STOCK_BASIC);
        List<Shares> shares = ConvertUtil.convert(list, Shares.class);
        Lists.partition(shares, 200).forEach(item -> synchronization(item));
    }


    private void synchronization(List<Shares> list) {
        log.debug("初始化数据， {}", JSONObject.toJSONString(list));
        taskExecutor.execute(() -> saveOrUpdateBatch(list));
    }
}
