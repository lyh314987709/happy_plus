package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.happy.convert.ConvertUtil;
import com.happy.dao.SharesDao;
import com.happy.domain.Shares;
import com.happy.net.ClientService;
import com.happy.service.SharesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SharesServiceImpl extends ServiceImpl<SharesDao, Shares> implements SharesService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TaskExecutor taskExecutor;

    private static Map<String, Shares> MAP = new HashMap<>();

    @Override
    public void synchronization() {
        List<Map<String, String>> list = clientService.post(ClientService.Api.STOCK_BASIC);
        List<Shares> shares = ConvertUtil.convert(list, Shares.class);
        Lists.partition(shares, 200).forEach(item -> synchronization(item));
    }

    @Override
    public Map<String, Shares> map(Set<String> tcCodes) {

        if(MAP.isEmpty()) {
            List<Shares> list = list();
            MAP = Maps.uniqueIndex(list, Shares::getTsCode);
        }

        if(CollectionUtils.isEmpty(tcCodes)) {
            return MAP;
        }
        return MAP.entrySet().stream()
                .filter(item -> tcCodes.contains(item.getKey()))
                .collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()));
    }


    private void synchronization(List<Shares> list) {
        log.debug("初始化数据， {}", JSONObject.toJSONString(list));
        taskExecutor.execute(() -> saveOrUpdateBatch(list));
    }
}
