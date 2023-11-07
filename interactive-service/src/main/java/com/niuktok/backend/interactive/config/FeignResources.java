package com.niuktok.backend.interactive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.niuktok.backend.interactive.service.feign.VideoFeign;

@Component
public class FeignResources {
    @Autowired
    private VideoFeign videoFeign;
}
