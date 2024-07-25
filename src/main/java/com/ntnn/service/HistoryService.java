package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final DatabaseImplService service;

    public GenericSingleRestResponse saveTopUpTransactionAsyncAsHistory(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
        service.saveTopUpTransaction(document, accountId, StatusType.PENDING)
                .forEach(transaction -> genericSingleRestResponse.getCdtTrfTxInfSts().add(
                        new CreditTransferTransaction30Status(null, transaction.getEndToEndId(), transaction.getTransactionId(), null, true, null, null)));
        return genericSingleRestResponse;
    }

}
