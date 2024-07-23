package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HISTORY")
@IdClass(HistoryKey.class)
public class History {
    @Id
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Id
    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Id
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Id
    @Column(name = "WALLET_TYPE")
    private WalletType walletType;

    @Id
    @Column(name = "CURRENCY")
    private Currency currency;

    @Id
    @Column(name = "STATUS_TYPE")
    private StatusType statusType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || obj.getClass() != getClass()) return false;

        Transaction that = (Transaction) obj;

        return new EqualsBuilder()
                .append(accountId, that.getAccountId())
                .append(transactionId, that.getTransactionId())
                .append(creationDate, that.getCreationDate())
                .append(statusType, that.getStatusType())
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
