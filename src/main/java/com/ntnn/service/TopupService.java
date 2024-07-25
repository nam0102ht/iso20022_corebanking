package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    public Flux<GenericSingleRestResponse> topupAsync(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
        Flux.from(service.saveTopUpTransactionAsync(document, accountId, StatusType.PENDING)).log().map(transaction -> {
            if (transaction != null) {
                return CreditTransferTransaction30Status.builder()
                    .endToEndId(transaction.getEndToEndId())
                    .success(true)
                    .build();
            }
            return null;
        }).doOnEach(creditTransferTransaction30StatusSignal -> {
            genericSingleRestResponse.getCdtTrfTxInfSts().add(creditTransferTransaction30StatusSignal.get());

            service.saveHistoryTransactionAsync(document, accountId, StatusType.PENDING);
        }).subscribe();
        return Flux.just(genericSingleRestResponse);
    }
}
