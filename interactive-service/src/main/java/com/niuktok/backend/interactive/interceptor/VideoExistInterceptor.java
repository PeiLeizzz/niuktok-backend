package com.niuktok.backend.interactive.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.pojo.vo.GenericResponseVO;
import com.niuktok.backend.common.utils.ReqResUtils;
import com.niuktok.backend.interactive.service.feign.VideoFeign;


public class VideoExistInterceptor implements HandlerInterceptor {
    @Autowired
    private VideoFeign videoFeign;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long videoID = Long.valueOf(ReqResUtils.parsePathVar(request, "videoID"));
        if (videoID == null) {
            throw new NiuktokException(ResponseStatusType.INVALID_PARAMS, "视频 ID 为空");
        }
        if (videoID < 0) {
            throw new NiuktokException(ResponseStatusType.INVALID_PARAMS, "视频 ID 为负数");
        }

        GenericResponseVO<Boolean> responseVO = videoFeign.exist(videoID);
        if (!responseVO.getData()) {
            throw new NiuktokException(ResponseStatusType.INVALID_PARAMS, "视频不存在");
        }
        return true;
    }
}
