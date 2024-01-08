package com.vention.delivvacoreservice.repository;

import com.vention.delivvacoreservice.domain.OrderInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderInvitationsRepository extends JpaRepository<OrderInvitation, Long> {
    @Modifying
    @Query(value = "update order_invitations set status = 'APPROVED' where to_user_id = :courier_id and order_id = :order_id", nativeQuery = true)
    @Transactional
    void approveOrderByCourier(@Param("courier_id") Long courier_id, @Param("order_id") Long orderId);

    List<OrderInvitation> findByOrderIdOrderByCreatedAtDesc(Long orderId);

    List<OrderInvitation> findAllByToUserIdOrderByCreatedAtDesc(Long toUserId);

    @Query("select i from order_invitations i where i.orderId = :orderId")
    Optional<OrderInvitation> findApprovedByOrderId(@Param("orderId") Long orderId);
}
