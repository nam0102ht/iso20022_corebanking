package com.ntnn.entity;

import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TRANSACTIONS")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@Entity(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account", referencedColumnName = "ACCOUNT_ID")
    private Account account;

    @Column(name = "STATUS_TYPE")
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(name = "CREATION_DATE")
    @CreatedDate
    private Date creationDate;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "SOURCE_NAME")
    private String sourceName;

    @Column(name = "SOURCE_ID")
    private String sourceId;

    @Column(name = "DESTINATION_NAME")
    private String destinationName;

    @Column(name = "DESTINATION_ID")
    private String destinationId;

    @Column(name = "END_TO_END_ID")
    private String endToEndId;

    @Version
    @Column(name = "VERSION")
    private Long version;

}
