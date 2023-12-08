package com.vention.delivvacoreservice.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openStreetMapApi", url = "${cloud.map.url}")
public interface MapClient {

    @GetMapping
    String getLocationByCoordinates(
            @RequestParam(name = "lat") double latitude,
            @RequestParam(name = "lon") double lon,
            @RequestParam(name = "format") String format
            );
}
