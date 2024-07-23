package com.ntnn.entity;

import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Table("TRANSACTION")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Transaction {
    @Id
    private UUID id;

    @Column("ACCOUNT_ID")
    private String accountId;

    @Column("TRANSACTION_ID")
    private String transactionId;

    @Column("STATUS_TYPE")
    private StatusType statusType;

    @Column("CREATION_DATE")
    private Date creationDate;

    @Column("AMOUNT")
    private BigDecimal amount;

    @Column("TRANSACTION_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private TransactionType transactionType;

    @Column("SOURCE_NAME")
    private String sourceName;

    @Column("SOURCE_ID")
    private String sourceId;

    @Column("DESTINATION_NAME")
    private String destinationName;

    @Column("DESTINATION_ID")
    private String destinationId;

    @Column("END_TO_END_ID")
    private String endToEndId;

    @Version
    private Long version;

}
