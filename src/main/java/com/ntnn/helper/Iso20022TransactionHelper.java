package com.ntnn.helper;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import com.ntnn.common.WalletType;
import com.ntnn.entity.History;
import com.ntnn.entity.HistoryKey;
import com.ntnn.entity.Transaction;
import com.ntnn.entity.TransactionKey;
import com.ntnn.wsld.FIToFICustomerCreditTransferV07;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
            TransactionKey key = new TransactionKey(model.getCdtTrfTxInf().get(i).getPmtId().getTxId(),
                    accountId,
                    model.getGrpHdr().getCreDtTm().toGregorianCalendar().getTime(),
                    statusType);
            transactions.add(new Transaction(key.getAccountId(),
                    key.getTransactionId(),
                    statusType,
                    key.getCreationDate(),
                    model.getCdtTrfTxInf().get(i).getIntrBkSttlmAmt().getValue(),
                    TransactionType.valueOf(transactionType),
                    sourceName,
                    sourceId,
                    destinationName,
                    destinationId,
                    endToEndId
                    ));
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
            HistoryKey key = new HistoryKey(model.getCdtTrfTxInf().get(i).getPmtId().getTxId(),
                    accountId,
                    model.getGrpHdr().getCreDtTm().toGregorianCalendar().getTime(),
                    walletType,
                    currency,
                    statusType);
            History history = new History();
            history.setTransactionId(key.getTransactionId());
            history.setAccountId(key.getAccountId());
            history.setCurrency(currency);
            history.setCreationDate(new Date(model.getGrpHdr().getCreDtTm().toGregorianCalendar().toZonedDateTime().toEpochSecond()));
            history.setStatusType(statusType);
            transactions.add(new History(
                    history.getAccountId(),
                    history.getTransactionId(),
                    history.getCreationDate(),
                    history.getWalletType(),
                    history.getCurrency(),
                    history.getStatusType()
            ));
        }
        return transactions;
    }
}
