package com.vention.delivvacoreservice.domain;

import com.vention.delivvacoreservice.enums.InvitationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "order_invitations")
@NoArgsConstructor
@AllArgsConstructor
public class OrderInvitation extends BaseEntity {

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private InvitationStatus status;
}
