package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.WalletType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class AccountKey {
    private String accountId;
    private WalletType walletType;
    private Currency currency;
    private Date creationDate;
}
