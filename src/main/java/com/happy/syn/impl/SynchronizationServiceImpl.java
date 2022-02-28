package com.happy.syn.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.happy.domain.SharesIntermediate;
import com.happy.net.ClientService;
import com.happy.service.SharesIntermediateService;
import com.happy.service.SharesService;
import com.happy.service.SharesTypeService;
import com.happy.syn.SynchronizationService;
import com.happy.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SynchronizationServiceImpl implements SynchronizationService {

    @Autowired
    private SharesService sharesService;

    @Autowired
    private SharesTypeService sharesTypeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SharesIntermediateService sharesIntermediateService;

    @Autowired
    private TaskExecutor taskExecutor;


    @Override
    public void synchronizationShares() {
        //  基础数据落库
        log.debug("开始同步基础数据");
        sharesService.synchronization();
        log.debug("同步基础数据结束");

        log.debug("开始同步类型");
        //  类型落库
        sharesTypeService.synchronization();
        log.debug("同步类型结束");
    }

    @Override
    public void relation() {

        log.debug("开始绑定数据关系");
        //  从问财重新获取数据
        List<Map<String, String>> result = clientService.askMoneyGet(ClientService.Api.ASK_MONEY_STRATEGY, clientService.getDefAskMoney());

        List<Req> relations = result.stream().filter(item -> !StringUtils.isEmpty(item.get(Constant.DEF_KEY))).map(item -> Req.builder().tsCode(item.get("股票代码"))
                .typeName(ImmutableSet.copyOf(Splitter.on("；").trimResults().omitEmptyStrings().split(item.get(Constant.DEF_KEY)))).build())
                .collect(Collectors.toList());
        Lists.partition(relations, 200).forEach(item -> relation(item));
        log.debug("绑定数据关系结束");
    }

    private void relation(List<Req> list) {
        taskExecutor.execute(() -> {
            log.debug("开始绑定数据关系 list");
            List<SharesIntermediate> sharesIntermediates = list.stream().map(item -> item.getTypeName().stream()
                    .map(i -> SharesIntermediate.builder().tsCode(item.getTsCode()).sharesTypeName(i).build())
                    .collect(Collectors.toList()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            sharesIntermediateService.saveOrUpdateBatch(sharesIntermediates);
        });

    }
}
