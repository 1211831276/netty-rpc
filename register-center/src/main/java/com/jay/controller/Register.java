package com.jay.controller;

import com.jay.data.RedisClient;
import com.jay.dto.RpcRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author jay
 * @create 2024/1/21
 **/
@RestController
@RequestMapping("/register")
public class Register {
    @Autowired
    private RedisClient redisClient;
    private Map<String, Set<String>> serviceMap = new HashMap<>();
    // 对于这个方法只注册类名，知道在那个机器上就可以了
    @PostMapping("")
    public Boolean registerRpc(@RequestBody RpcRegisterDTO registerDTO, HttpServletRequest request) {
        if (Objects.isNull(registerDTO) || StringUtils.isEmpty(registerDTO.getServiceName())) {
            throw new RuntimeException("没有服务名");
        }
        String ip = getClientIP(request);
        if (Objects.isNull(ip)) {
            throw new RuntimeException("没有ip");
        }
        //redisClient.addToSet(registerDTO.getServiceName(), ip);
        // 为了简化，就直接用memory了，不用redis了
        Set<String> ips = serviceMap.get(registerDTO.getServiceName());
        if (Objects.isNull(ips)) {
            ips = new HashSet<>();
            ips.add(ip);
            serviceMap.put(registerDTO.getServiceName(), ips);
        } else {
            ips.add(ip);
        }
        return Boolean.TRUE;
    }

    @GetMapping("/query-ip/{serviceName}")
    public String getIp(@PathVariable String serviceName) {
        Set<String> ips = serviceMap.get(serviceName);
        if (Objects.isNull(ips)) {
            throw new RuntimeException("不存在这个服务" + serviceName);
        } else {
            int size = ips.size();
            return ips.stream().findFirst().get();
        }
    }


    // 从 HttpServletRequest 中获取 IP 地址
    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 如果是多级代理，获取第一个非unknown的IP地址
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(",")).trim();
        }

        return ipAddress;
    }
}
