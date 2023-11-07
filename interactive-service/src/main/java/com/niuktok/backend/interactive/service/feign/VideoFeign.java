package com.niuktok.backend.interactive.service.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.niuktok.backend.common.config.FeignConfigurer;
import com.niuktok.backend.common.controller.video.VideoController;

@FeignClient(name = "${customize.service.video.service-name}", configuration = FeignConfigurer.class)
public interface VideoFeign extends VideoController {
}
