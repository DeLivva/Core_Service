package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from orders o where o.status = 'CREATED' or o.status = 'REJECTED_BY_COURIER'")
    List<Order> findAllByStatus();
}
