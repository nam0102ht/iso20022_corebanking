package com.ntnn.entity;

import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Table(name= "TRANSACTION")
@IdClass(TransactionKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Id
    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Id
    @Column(name = "STATUS_TYPE")
    private StatusType statusType;

    @Id
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TRANSACTION_TYPE", nullable = false)
    @Enumerated(EnumType.ORDINAL)
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || obj.getClass() != getClass()) return false;

        Transaction that = (Transaction) obj;

        return new EqualsBuilder()
                .append(accountId, that.accountId)
                .append(transactionId, that.transactionId)
                .append(creationDate, that.creationDate)
                .append(statusType, that.statusType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(accountId)
                .append(transactionId)
                .append(creationDate)
                .hashCode();
    }
}
