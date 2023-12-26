package com.vention.delivvacoreservice.feign_clients;

import com.vention.delivvacoreservice.dto.UserIdDTO;
import com.vention.delivvacoreservice.dto.response.LatAndLongByTrackNumberDTO;
import com.vention.delivvacoreservice.dto.response.PathByUserIdDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "gedServiceApi", url = "${cloud.geo-service.url}/api/v1/geo")
public interface GeoServiceClient {

    @RequestMapping(method = GET, value = "/{trackNumber}")
    ResponseEntity<LatAndLongByTrackNumberDTO> getPathByTrackNumber(@PathVariable String trackNumber);

    @RequestMapping(method = GET, value = "")
    ResponseEntity<PathByUserIdDTO> getPathByUserId(@RequestBody UserIdDTO dto);
}
