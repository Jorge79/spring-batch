package org.example.springbatch.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ReportLine(
        String dealerName,
        String model,
        int unitsSold,
        BigDecimal revenueBrl
) {

    public ReportLine(String dealerName, String model) {
        this(dealerName, model, 0, BigDecimal.ZERO);
    }

    public ReportLine addSale(BigDecimal salePrice) {
        return new ReportLine(dealerName, model, unitsSold + 1, revenueBrl.add(salePrice));
    }

    public String toCsv() {
        BigDecimal rounded = revenueBrl.setScale(2, RoundingMode.HALF_UP);
        return dealerName + "," + model + "," + unitsSold + "," + rounded.toPlainString();
    }
}