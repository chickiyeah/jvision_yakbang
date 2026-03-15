package com.deco.yakbang.config;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.cmmn.trace.handler.DefaultTraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.handler.TraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import org.egovframe.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class EgovConfigCommon {

    @Bean
    public LeaveaTrace leaveaTrace() {
        LeaveaTrace leaveaTrace = new LeaveaTrace();
        leaveaTrace.setTraceHandlerServices(new TraceHandlerService[] { traceHandlerService() });
        return leaveaTrace;
    }

    @Bean
    public DefaultTraceHandleManager traceHandlerService() {
        DefaultTraceHandleManager traceHandleManager = new DefaultTraceHandleManager();
        traceHandleManager.setReqExpMatcher(new AntPathMatcher());
        traceHandleManager.setPatterns(new String[] { "*" });
        traceHandleManager.setHandlers(new TraceHandler[] { defaultTraceHandler() });
        return traceHandleManager;
    }

    @Bean
    public DefaultTraceHandler defaultTraceHandler() {
        return new DefaultTraceHandler();
    }
    
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // egovMap 별칭을 명시적으로 등록
            configuration.getTypeAliasRegistry().registerAlias("egovMap", org.egovframe.rte.psl.dataaccess.util.EgovMap.class);
        };
    }
}