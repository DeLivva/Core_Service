package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.OrderEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEvaluationRepository extends JpaRepository<OrderEvaluation, Long> {
}
