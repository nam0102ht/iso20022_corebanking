package com.ntnn.entity;

import com.ntnn.common.StatusType;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionKey implements Serializable {
    private String accountId;
    private String transactionId;
    private Date creationDate;
    private StatusType statusType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransactionKey that = (TransactionKey) obj;
        return new EqualsBuilder()
                .append(transactionId, that.transactionId)
                .append(accountId, that.accountId)
                .append(statusType, that.statusType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(transactionId)
                .append(accountId)
                .append(statusType)
                .toHashCode();
    }
}
