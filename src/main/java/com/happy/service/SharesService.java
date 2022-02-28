package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.Shares;

import java.util.Map;
import java.util.Set;

public interface SharesService extends IService<Shares> {

    /**
     * 同步数据
     */
    void synchronization();

    /**
     *
     * @param tcCodes
     * @param
     * @return
     */
    Map<String, Shares> map(Set<String> tcCodes);
}
