package com.ntnn.helper;

import com.ntnn.dto.TransferMoneyRequest;
import com.prowidesoftware.swift.model.mx.dic.CustomerCreditTransferInitiationV11;
import com.prowidesoftware.swift.model.mx.dic.PartyIdentification135;
import com.prowidesoftware.swift.model.mx.dic.PaymentInstruction40;

public class MxCreation {
    public static PaymentInstruction40 initPaymentInformation(TransferMoneyRequest transferMoneyRequest) {
        return new PaymentInstruction40()
                .setDbtr(new PartyIdentification135()
                        .setNm(transferMoneyRequest.getPartyProfile().getNm())
                        .setCtctDtls(transferMoneyRequest.getContact())
                        .setId(transferMoneyRequest.getPartyChoice())
                        .setPstlAdr(transferMoneyRequest.getPostalAddress())
                );
    }

    public static CustomerCreditTransferInitiationV11 creditTransfer(TransferMoneyRequest transferMoneyRequest) {
        return new CustomerCreditTransferInitiationV11()
                .setGrpHdr(transferMoneyRequest.getGroupHeader());
    }

}
