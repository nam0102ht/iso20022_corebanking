package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.WalletType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNT")
@IdClass(Account.class)
public class Account {
    @Id
    private String accountId;

    @Id
    @Column(name = "WALLET_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private WalletType walletType;

    @Id
    @Column(name = "CURRENCY")
    @Enumerated(EnumType.ORDINAL)
    private Currency currency;

    @Id
    @Column(name = "CREATION_DATE")
    private Date creationDate;


}
