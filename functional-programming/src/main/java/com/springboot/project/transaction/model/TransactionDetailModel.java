package com.springboot.project.transaction.model;

import com.springboot.project.transaction.generated.dto.DomainEnumDto;
import com.springboot.project.transaction.generated.dto.PaymentMethodEnumDto;
import com.springboot.project.transaction.generated.dto.TransactionStatusEnumDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailModel {

    private UUID id;

    private Long sequenceNumber;

    private String transactionNumber;

    private LocalDate date;

    private DomainEnumDto domain;

    private String location;

    private BigDecimal value;

    private TransactionStatusEnumDto status;

    private PaymentMethodEnumDto paymentMethod;

    private BigDecimal taxAmount;

    private BigDecimal netValue;

    private UUID bankAccountId;
}
