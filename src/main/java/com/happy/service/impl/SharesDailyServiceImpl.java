package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDailyDao;
import com.happy.domain.SharesDaily;
import com.happy.net.ClientService;
import com.happy.service.SharesDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SharesDailyServiceImpl extends ServiceImpl<SharesDailyDao, SharesDaily> implements SharesDailyService {

    @Autowired
    private ClientService clientService;

    @Override
    public List<SharesDaily> synchronization(SharesDailyReq req) {
        List<Map<String, String>> list = clientService.post(ClientService.Api.DAILY, JSONObject.parseObject(JSONObject.toJSONString(req)));
        List<SharesDaily> sharesDailies = ConvertUtil.convert(list, SharesDaily.class);
        sharesDailies = sharesDailies.stream().map(item -> item.build()).collect(Collectors.toList());
        Lists.partition(sharesDailies, 200).forEach(item -> saveOrUpdateBatch(item));
        return sharesDailies;
    }
}
