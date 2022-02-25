package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.Shares;

public interface SharesService extends IService<Shares> {

    /**
     * 同步数据
     */
    void synchronization();
}
