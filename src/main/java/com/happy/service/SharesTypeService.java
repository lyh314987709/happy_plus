package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.domain.SharesType;

import java.util.List;

public interface SharesTypeService extends IService<SharesType> {

    /**
     * 同步数据
     */
    void synchronization();

    List<SharesType> findByNet();
}
