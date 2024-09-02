package com.ntnn.entity;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "HISTORY")
@EqualsAndHashCode
@Entity(name = "history")
public class History {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "ID")
  private String id;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "account", referencedColumnName = "ACCOUNT_ID")
  private Account account;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "transaction", referencedColumnName = "TRANSACTION_ID")
  private Transaction transaction;

  @Column(name = "CREATION_DATE")
  private Date creationDate;

  @Column(name = "WALLET_TYPE")
  @Enumerated(EnumType.STRING)
  private WalletType walletType;

  @Column(name = "CURRENCY")
  @Enumerated(EnumType.STRING)
  private Currency currency;

  @Column(name = "STATUS_TYPE")
  @Enumerated(EnumType.STRING)
  private StatusType statusType;

  @Version
  @Column(name = "VERSION")
  private Long version;
}
