package com.niuktok.backend.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;
import com.niuktok.backend.common.config.AuthConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private ExecutorService executorService;

    @Autowired
    private AuthConfigurer authConfigurer;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getPath().toString();
        for (String internalUrl : authConfigurer.getInternalUrls()) {
            if (url.matches(internalUrl)) {
                return responseErrorMessage(exchange, new BaseResponseVO(ResponseStatusType.NO_PERMISSIONS));
            }
        }
        boolean needIgnored = false;
        for (String ignoreUrl : authConfigurer.getIgnoreUrls()) {
            if (url.matches(ignoreUrl)) {
                needIgnored = true;
                break;
            }
        }

        HttpMethod method = exchange.getRequest().getMethod();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String finalUrl = "http://" + authConfigurer.getServiceName() + authConfigurer.getEntryPointUrl() + url;
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        Future<ResponseEntity> future = executorService.submit(() -> restTemplate.exchange(finalUrl, method, requestEntity, Map.class));

        try {
            ResponseEntity response = future.get();
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            Integer code = (Integer) body.get("status");
            if (!needIgnored && !ResponseStatusType.SUCCESS.getCode().equals(code)) {
                return responseErrorMessage(exchange, new BaseResponseVO(ResponseStatusType.getByCode(code)));
            }
            Object userIDObj = body.get("data");
            if (needIgnored && userIDObj == null) {
                return chain.filter(exchange);
            }
            String userID = (String) userIDObj;
            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            requestBuilder.headers(k -> k.set("userID", userID));
            ServerHttpRequest request = requestBuilder.build();
            exchange.mutate().request(request).build();
            return chain.filter(exchange);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpClientErrorException) {
                HttpClientErrorException he = (HttpClientErrorException) cause;
                String message = he.getMessage();
                log.error("ExecutionException: {}", message);
                return responseErrorMessage(exchange, BaseResponseVO.error("权限认证时出现错误"));
            }
            return responseErrorMessage(exchange, BaseResponseVO.error("权限认证时出现错误"));
        } catch (Exception e) {
            e.printStackTrace();
            return responseErrorMessage(exchange, BaseResponseVO.error("权限认证时出现错误"));
        }
    }

    private Mono<Void> responseErrorMessage(ServerWebExchange exchange, Object object) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String message;
        try {
            message = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message = "{" +
                    "\"status\": \""+ 1 + "\"," +
                    "\"message\": \""+ "认证结果序列化失败，错误信息：" + e.getMessage() +"\"," +
                    "\"timestamp\": \""+ new Date() +"\"" +
                    "\"data\": null" + "\"" +
                    "}";
        }

        return response.writeWith(Flux.just(response.bufferFactory().wrap(message.getBytes())));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}