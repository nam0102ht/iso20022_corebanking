package com.ntnn.helper;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import com.ntnn.common.WalletType;
import com.ntnn.entity.Account;
import com.ntnn.entity.History;
import com.ntnn.entity.Transaction;
import com.ntnn.wsld.FIToFICustomerCreditTransferV07;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Iso20022TransactionHelper {
    public static Set<Transaction> populateTransaction(FIToFICustomerCreditTransferV07 model, String accountId, String transactionType, StatusType statusType) {
        Set<Transaction> transactions = new HashSet<>(model.getCdtTrfTxInf().size());
        for (int i = 0; i < model.getCdtTrfTxInf().size(); i++) {
            String sourceId = model.getCdtTrfTxInf().get(i).getDbtrAcct().getId().getOthr().getId();
            String sourceName = model.getCdtTrfTxInf().get(i).getDbtrAgt().getFinInstnId().getBICFI() == null ?
                    model.getCdtTrfTxInf().get(i).getDbtrAgt().getFinInstnId().getOthr().getId() : model.getCdtTrfTxInf().get(i).getDbtrAgt().getFinInstnId().getBICFI();
            String endToEndId = model.getCdtTrfTxInf().get(i).getPmtId().getEndToEndId();
            String destinationId = model.getCdtTrfTxInf().get(i).getCdtrAcct().getId().getOthr().getId();
            String destinationName = model.getCdtTrfTxInf().get(i).getCdtrAgt().getFinInstnId().getBICFI() == null ?
                    model.getCdtTrfTxInf().get(i).getCdtrAgt().getFinInstnId().getOthr().getId() : model.getCdtTrfTxInf().get(i).getCdtrAgt().getFinInstnId().getBICFI();

            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID().toString());
            transaction.setAccount(Account.builder().accountId(accountId).build());
            transaction.setId(model.getCdtTrfTxInf().get(i).getPmtId().getTxId());
            transaction.setTransactionType(TransactionType.valueOf(transactionType));
            transaction.setCreationDate(model.getGrpHdr().getCreDtTm().toGregorianCalendar().getTime());
            transaction.setAmount(model.getCdtTrfTxInf().get(i).getIntrBkSttlmAmt().getValue());
            transaction.setSourceId(sourceId);
            transaction.setDestinationName(destinationName);
            transaction.setDestinationId(destinationId);
            transaction.setEndToEndId(endToEndId);
            transactions.add(transaction);
            transaction.setVersion(1l);
        }
        return transactions;
    }

    public static Set<History> populateHistory(FIToFICustomerCreditTransferV07 model,
                                               String accountId,
                                               StatusType statusType
    ) {
        Set<History> transactions = new HashSet<>(model.getCdtTrfTxInf().size());
        for (int i = 0; i < model.getCdtTrfTxInf().size(); i++) {
            WalletType walletType = WalletType.valueOf(model.getCdtTrfTxInf().get(i).getChrgBr().value());
            Currency currency = Currency.valueOf(model.getCdtTrfTxInf().get(i).getIntrBkSttlmAmt().getCcy());

            History history = new History();
            history.setTransaction(Transaction.builder().id(model.getCdtTrfTxInf().get(i).getPmtId().getTxId()).build());
            history.setAccount(Account.builder().accountId(accountId).build());
            history.setId(UUID.randomUUID().toString());
            history.setCurrency(currency);
            history.setStatusType(statusType);
            history.setWalletType(walletType);
            history.setCurrency(currency);
            history.setCreationDate(new Date(model.getGrpHdr().getCreDtTm().toGregorianCalendar().toZonedDateTime().toEpochSecond()));
            history.setStatusType(statusType);
            history.setVersion(0l);

            transactions.add(History.builder()
                    .account(history.getAccount())
                    .transaction(history.getTransaction())
                    .creationDate(history.getCreationDate())
                    .walletType(history.getWalletType())
                    .currency(history.getCurrency())
                    .statusType(history.getStatusType())
                    .version(1l)
                    .build());
        }
        return transactions;
    }
}
