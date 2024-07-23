package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.util.Date;
import java.util.UUID;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "HISTORY")
@EqualsAndHashCode
public class History {
  @Id
  private UUID id;

  @Column(name = "ACCOUNT_ID")
  private String accountId;

  @Column(name = "TRANSACTION_ID")
  private String transactionId;

  @Column(name = "CREATION_DATE")
  private Date creationDate;

  @Column(name = "WALLET_TYPE")
  private WalletType walletType;

  @Column(name = "CURRENCY")
  private Currency currency;

  @Column(name = "STATUS_TYPE")
  private StatusType statusType;

  @Version
  private Long version;
}
