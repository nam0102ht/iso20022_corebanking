package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.WalletType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("ACCOUNT")
public class Account {
    @Id
    private String accountId;

    @Column("WALLET_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private WalletType walletType;

    @Column("CURRENCY")
    @Enumerated(EnumType.ORDINAL)
    private Currency currency;

    @Column("CREATION_DATE")
    private Date creationDate;

    @Version
    private Long version;

}
