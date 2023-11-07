package com.niuktok.backend.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.pojo.vo.BaseResponseVO;

import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ReqResUtils {
    /**
     * 解析 request 中路径参数的 varName
     *
     * @param request 请求
     * @return hctaskId
     * @throws NumberFormatException 解析异常
     */
    @SuppressWarnings("unchecked")
    public static String parsePathVar(HttpServletRequest request, String varName) throws NumberFormatException {
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathVars.get(varName);
    }

    /**
     * 返回错误响应
     *
     * @param response 响应
     * @param errMsg   错误信息
     * @throws IOException
     */
    public static void returnErrMsg(HttpServletResponse response, ResponseStatusType responseStatusType, String errMsg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        BaseResponseVO responseVO = new BaseResponseVO(responseStatusType, errMsg);
        ObjectMapper objectMapper = new ObjectMapper();
        out.append(objectMapper.writeValueAsString(responseVO));
    }
}
