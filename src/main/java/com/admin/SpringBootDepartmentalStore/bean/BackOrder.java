package com.admin.SpringBootDepartmentalStore.bean;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
@Data
@Table(name = "Backorder")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class BackOrder {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long backOrderId;

        @OneToOne
        @JoinColumn(name = "orderId", referencedColumnName = "orderId")
        private Order order;

}
