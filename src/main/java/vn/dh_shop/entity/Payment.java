package vn.dh_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.dh_shop.entity.enums.PaymentMethod;
import vn.dh_shop.entity.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends CommonEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


}
