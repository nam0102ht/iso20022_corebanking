package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryKey implements Serializable {
    private String accountId;
    private String transactionId;
    private Date creationDate;
    private WalletType walletType;
    private Currency currency;
    private StatusType statusType;
}
