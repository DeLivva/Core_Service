package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.OrderEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderEvaluationRepository extends JpaRepository<OrderEvaluation, Long> {

    Optional<OrderEvaluation> findByOrderId(Long orderId);

    @Query("select o.courierId as courierId, avg(e.rate) as rating, count(o) as quantity from order_evaluations e join e.order o group by o.courierId order by rating desc")
    Page<Object[]> couriers(Pageable pageable);
}
