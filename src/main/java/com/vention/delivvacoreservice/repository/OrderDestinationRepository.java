package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.OrderDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDestinationRepository extends JpaRepository<OrderDestination, Long> {

    Optional<OrderDestination> findByLongitudeAndLatitude(Double longitude, Double latitude);
}
