package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TopupService {
    private final DatabaseService service;

    public GenericSingleRestResponse topup(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
        service.saveTopUpTransaction(document, accountId, StatusType.PENDING)
                .forEach(transaction -> genericSingleRestResponse.getCdtTrfTxInfSts().add(
                        new CreditTransferTransaction30Status(null, transaction.getEndToEndId(), transaction.getTransactionId(), null, true)));
        service.saveHistoryTransactionAsync(document, accountId, StatusType.PENDING);
        return genericSingleRestResponse;
    }

    public Flux<GenericSingleRestResponse> topupAsync(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
        Flux.from(service.saveTopUpTransactionAsync(document, accountId, StatusType.PENDING)).log().map(transaction -> {
            if (transaction != null) {
                return new CreditTransferTransaction30Status(null, transaction.getEndToEndId(), transaction.getTransactionId(), null, true);
            }
            return null;
        }).doOnEach(creditTransferTransaction30StatusSignal -> {
            genericSingleRestResponse.getCdtTrfTxInfSts().add(creditTransferTransaction30StatusSignal.get());

            service.saveHistoryTransactionAsync(document, accountId, StatusType.PENDING);
        }).subscribe();
        return Flux.just(genericSingleRestResponse);
    }
}
