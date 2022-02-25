package com.happy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.happy.dao.SharesTypeDao;
import com.happy.domain.SharesType;
import com.happy.net.ClientService;
import com.happy.service.SharesTypeService;
import com.happy.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SharesTypeServiceImpl extends ServiceImpl<SharesTypeDao, SharesType> implements SharesTypeService {

    @Autowired
    private ClientService clientService;

    @Override
    public void synchronization() {
        Lists.partition(findByNet(), 200).forEach(item -> saveOrUpdateBatch(item));

    }

    @Override
    public List<SharesType> findByNet() {
        List<Map<String, String>> result = clientService.askMoneyGet(ClientService.Api.ASK_MONEY_STRATEGY, clientService.getDefAskMoney());

        List<String> types = result.stream()
                .map(item -> item.get(Constant.DEF_KEY))
                .collect(Collectors.toList());

        String join = Joiner.on("；").skipNulls().join(types);

        Set<String> typeResult = ImmutableSet.copyOf(Splitter.on("；").trimResults().omitEmptyStrings().split(join));

        return typeResult.stream().map(item -> SharesType.builder()
                .typeName(item)
                .build()).collect(Collectors.toList());
    }


}
