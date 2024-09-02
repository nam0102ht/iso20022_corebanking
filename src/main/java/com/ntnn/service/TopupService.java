package com.ntnn.service;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.entity.Account;
import com.ntnn.entity.Transaction;
import com.ntnn.exception.TechnicalException;
import com.ntnn.repository.AccountByCustomerRepository;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupService {

  private final DatabaseImplService service;
  private final AccountByCustomerRepository accountByCustomerRepository;

  @Transactional(rollbackFor = {TechnicalException.class})
  public GenericSingleRestResponse topup(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
    List<Transaction> transactionList = new ArrayList<>();
    Account account;
    try {
      account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).creationDate(new Date()).version(1L).currency(Currency.USD).walletType(WalletType.DEBT).build()));
      transactionList = service.saveTopUpTransaction(document, account, StatusType.COMMIT);

      transactionList.forEach(transaction -> genericSingleRestResponse.getCdtTrfTxInfSts().add(
              CreditTransferTransaction30Status.builder()
                  .instrId(null)
                  .endToEndId(transaction.getEndToEndId())
                  .success(true)
                  .build()));
      transactionList.forEach(v -> {
        service.saveHistoryTransactionAsync(document, account, v, StatusType.COMMIT);
      });
    } catch (Exception exception) {
      throw new TechnicalException("999", exception.getMessage());
    }
    return genericSingleRestResponse;
  }

  @TimeLimiter(name = "topupAsync")
  public CompletableFuture<GenericSingleRestResponse> topupAsync(final Document document, final String accountId) {
    final GenericSingleRestResponse genericSingleRestResponse = new GenericSingleRestResponse();
    final Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).build()));
    return CompletableFuture.supplyAsync(() -> service.saveTopUpTransactionAsync(document, accountId, StatusType.COMMIT))
        .thenApplyAsync(transactions -> {
          if (transactions == null || transactions.isEmpty()) {
            return null;
          }
          transactions.forEach(v -> {
            service.saveHistoryTransactionAsync(document, account, v, StatusType.COMMIT);
          });
          return CreditTransferTransaction30Status
              .builder()
              .endToEndId(transactions.get(0).getEndToEndId())
              .success(true)
              .build();
        }).thenApplyAsync(creditTransferTransaction30StatusSignal -> {
          genericSingleRestResponse.getCdtTrfTxInfSts().add(creditTransferTransaction30StatusSignal);

          return genericSingleRestResponse;
        });
  }
}
