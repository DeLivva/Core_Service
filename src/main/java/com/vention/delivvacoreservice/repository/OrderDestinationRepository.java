package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.OrderDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDestinationRepository extends JpaRepository<OrderDestination, Long> {
}
