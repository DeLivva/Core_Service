package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from orders o where o.status = 'CREATED' or o.status = 'REJECTED_BY_COURIER'")
    List<Order> findAllByStatus();

    @Query("select o from orders o where o.startingDestination.city = :city and (o.status = 'CREATED' or o.status = 'REJECTED_BY_COURIER')")
    Page<Order> getByStartingPoint(String city, Pageable pageable);

    @Query("select o from orders o where o.finalDestination.city = :city and (o.status = 'CREATED' or o.status = 'REJECTED_BY_COURIER')")
    Page<Order> getByEndingPoint(String city, Pageable pageable);

    @Query("select o from orders o where o.deliveryDate >= :date and (o.status = 'CREATED' or o.status = 'REJECTED_BY_COURIER')")
    Page<Order> getByDate(Timestamp date, Pageable pageable);
}
