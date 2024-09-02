package com.ntnn.service;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.WalletType;
import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.entity.Account;
import com.ntnn.repository.AccountByCustomerRepository;
import com.ntnn.wsld.CreditTransferTransaction30Status;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final DatabaseImplService service;
    private final AccountByCustomerRepository accountByCustomerRepository;

    public GenericSingleRestResponse saveTopUpTransactionAsyncAsHistory(final Document document, final String accountId, final GenericSingleRestResponse genericSingleRestResponse) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).creationDate(new Date()).version(1L).currency(Currency.USD).walletType(WalletType.DEBT).build()));
        service.saveTopUpTransaction(document, account, StatusType.PENDING)
                .forEach(transaction -> genericSingleRestResponse.getCdtTrfTxInfSts().add(
                        new CreditTransferTransaction30Status(null, transaction.getEndToEndId(), transaction.getId(), null, true, null, null)));
        return genericSingleRestResponse;
    }

}
