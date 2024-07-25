package com.ntnn.controller;

import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.dto.TransferMoneyResponse;
import com.ntnn.concurrent.MdcAwareCallableService;
import com.ntnn.helper.XmlParser;
import com.ntnn.service.TopupService;
import com.ntnn.wsld.Document;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransferMoneyController {
    private static final Logger LOG = LoggerFactory.getLogger(TransferMoneyController.class);
    private final TopupService topupService;
    private final XmlParser xmlParser;

    @PostMapping(value = "/topup/{accountId}/async", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    private Flux<GenericSingleRestResponse> topupAsync(@RequestBody String xmlDocument, @PathVariable("accountId") String accountId) throws JAXBException {
        Document document = xmlParser.transformXmlToObject(xmlDocument);
        LOG.info("document size: {}", document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().size());
        GenericSingleRestResponse genericSingleRestResponse = xmlParser.prepareResponseDocument(document);
        return topupService.topupAsync(document, accountId, genericSingleRestResponse);
    }

    @PostMapping(value = "/topup/{accountId}", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    private GenericSingleRestResponse topup(@RequestBody String xmlDocument, @PathVariable("accountId") String accountId) throws JAXBException {
        Document document = xmlParser.transformXmlToObject(xmlDocument);
        LOG.info("document size: {}", document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().size());
        GenericSingleRestResponse genericSingleRestResponse = xmlParser.prepareResponseDocument(document);
        return topupService.topup(document, accountId, genericSingleRestResponse);
    }

    @PostMapping(value = "/history/{accountId}/{start}/{end}/{row}/{page}", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    private GenericSingleRestResponse transactionHistory(@RequestBody String xmlDocument, @PathVariable("accountId") String accountId,
                                                         @Param("start") long startDate, @Param("end") long endDate,
                                                         @Param("row") int row, @Param("page") int page) throws JAXBException {
        Document document = xmlParser.transformXmlToObject(xmlDocument);
        LOG.info("document size: {}", document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().size());
        GenericSingleRestResponse genericSingleRestResponse = xmlParser.prepareResponseDocument(document);
        return topupService.topup(document, accountId, genericSingleRestResponse);
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_XML_VALUE)
    public Callable<ResponseEntity<TransferMoneyResponse>> transferMoney() {
        return new MdcAwareCallableService().wrap(() -> {
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

}
