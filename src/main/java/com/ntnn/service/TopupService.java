package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupService {

  private final DatabaseImplService service;

  public GenericSingleRestResponse topup(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
    service.saveTopUpTransaction(document, accountId, StatusType.PENDING)
        .forEach(transaction -> genericSingleRestResponse.getCdtTrfTxInfSts().add(
            CreditTransferTransaction30Status.builder()
                .instrId(null)
                .endToEndId(transaction.getEndToEndId())
                .success(true)
                .build()));
    service.saveHistoryTransactionAsync(document, accountId, StatusType.PENDING);
    return genericSingleRestResponse;
  }

  @TimeLimiter(name = "topupAsync")
  public CompletableFuture<GenericSingleRestResponse> topupAsync(final Document document, final String accountId) {
    final GenericSingleRestResponse genericSingleRestResponse = new GenericSingleRestResponse();
    return CompletableFuture.supplyAsync(() -> service.saveTopUpTransactionAsync(document, accountId, StatusType.PENDING))
        .thenApplyAsync(transactions -> {
          if (transactions == null || transactions.isEmpty()) {
            return null;
          }
          return CreditTransferTransaction30Status
              .builder()
              .endToEndId(transactions.get(0).getEndToEndId())
              .success(true)
              .build();
        }).thenApplyAsync(creditTransferTransaction30StatusSignal -> {
          genericSingleRestResponse.getCdtTrfTxInfSts().add(creditTransferTransaction30StatusSignal);

          service.saveHistoryTransactionAsync(document, accountId, StatusType.PENDING);
          return genericSingleRestResponse;
        });
  }
}
