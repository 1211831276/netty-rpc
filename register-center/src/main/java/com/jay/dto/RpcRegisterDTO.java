package com.jay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jay
 * @create 2024/1/21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRegisterDTO {
    private String serviceName;
}
