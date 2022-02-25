package com.happy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDao;
import com.happy.domain.Shares;
import com.happy.net.ClientService;
import com.happy.service.SharesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SharesServiceImpl extends ServiceImpl<SharesDao, Shares> implements SharesService {

    @Autowired
    private ClientService clientService;

    @Override
    public void synchronization() {
        List<Map<String, String>> list = clientService.post(ClientService.Api.STOCK_BASIC);
        List<Shares> shares = ConvertUtil.convert(list, Shares.class);
        Lists.partition(shares, 200).forEach(item -> saveOrUpdateBatch(item));
    }
}
