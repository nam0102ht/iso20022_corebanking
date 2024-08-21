package com.ntnn.controller;

import com.ntnn.dto.GenericSingleRestResponse;
import com.ntnn.helper.XmlParser;
import com.ntnn.service.TopupService;
import com.ntnn.service.ValidateSignatureService;
import com.ntnn.wsld.Document;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@CrossOrigin
public class TransferMoneyController {
    private static final Logger LOG = LoggerFactory.getLogger(TransferMoneyController.class);
    private final TopupService topupService;
    private final ValidateSignatureService validateSignatureService;
    private final XmlParser xmlParser;

    @RateLimiter(name = "externalService")
    @PostMapping(value = "/topup/{accountId}", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    public Callable<ResponseEntity<GenericSingleRestResponse>> topup(@RequestHeader HttpHeaders httpServletRequest, @RequestBody String xmlDocument, @PathVariable("accountId") String accountId) {
        validateSignatureService.verifyRequest(httpServletRequest);
        return () -> {
            Document document = xmlParser.transformXmlToObject(xmlDocument);
            LOG.info("document size: {}", document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().size());
            GenericSingleRestResponse genericSingleRestResponse = xmlParser.prepareResponseDocument(document);
            return new ResponseEntity<>(topupService.topup(document, accountId, genericSingleRestResponse), HttpStatus.OK);
        };
    }

    @RateLimiter(name = "externalService")
    @PostMapping(value = "/history/{accountId}/{start}/{end}/{row}/{page}", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    public GenericSingleRestResponse transactionHistory(@RequestHeader HttpServletRequest httpServletRequest, @RequestBody String xmlDocument, @PathVariable("accountId") String accountId,
                                                         @Param("start") long startDate, @Param("end") long endDate,
                                                         @Param("row") int row, @Param("page") int page) throws JAXBException {
        httpServletRequest.getHeader("Authorization");
        Document document = xmlParser.transformXmlToObject(xmlDocument);
        LOG.info("document size: {}", document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().size());
        GenericSingleRestResponse genericSingleRestResponse = xmlParser.prepareResponseDocument(document);
        return topupService.topup(document, accountId, genericSingleRestResponse);
    }


}
