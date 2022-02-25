package com.happy.syn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public interface SynchronizationService {

    void synchronizationShares();

    void relation();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    class Req {
        private String tsCode;

        private Set<String> typeName;
    }
}
