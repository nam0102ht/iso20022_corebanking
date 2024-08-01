package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.WalletType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNT")
@Builder
@Entity(name = "account")
public class Account {
    @Id
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Column(name = "WALLET_TYPE")
    @Enumerated(EnumType.STRING)
    private WalletType walletType;

    @Column(name = "CURRENCY")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "CREATION_DATE")
    @CreatedDate
    private Date creationDate;

    @Version
    @Column(name = "VERSION")
    private Long version;

}
