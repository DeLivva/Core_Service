package com.vention.delivvacoreservice.feign_clients;

import com.vention.delivvacoreservice.dto.UserDetailsDTO;
import com.vention.general.lib.dto.response.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "AuthServiceApi", url = "${cloud.auth-service.url}")
public interface AuthServiceClient {

    @GetMapping("/api/v1/users/{userId}")
    UserResponseDTO getUserById(@PathVariable Long userId);

    @GetMapping("/api/v1/security-credentials")
    UserDetailsDTO getUserByEmail(@RequestParam String email);

    @GetMapping("/api/v1/users/all-ids")
    List<Long> getUsersIdList(@RequestParam Boolean isCourier);

    @GetMapping("/api/v1/users/active-users")
    Long getAllActiveUsersCount();
}
