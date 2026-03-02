package org.example.springbatch.domain;

import java.math.BigDecimal;

public record SaleRecord(
        String dealerId,
        String saleDate,
        String model,
        String paymentType,
        BigDecimal salePriceBrl
) {
}