package com.vention.delivvacoreservice.feign_clients;

import com.vention.delivvacoreservice.dto.response.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AuthServiceApi", url = "${cloud.auth-service.url}")
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    UserResponseDTO getUserById(@PathVariable Long userId);
}
