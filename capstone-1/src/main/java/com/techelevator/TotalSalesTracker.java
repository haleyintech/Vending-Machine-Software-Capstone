package com.techelevator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class TotalSalesTracker implements Serializable {
    private Map<String, Integer> totalSales;
    private BigDecimal totalRevenue;
    private static final long serialVersionUID = 123456789101112139L;

    public Map<String, Integer> getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalSales(Map<String, Integer> totalSales) {
        this.totalSales = totalSales;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void addToTotalRevenue(BigDecimal price) {
        totalRevenue = totalRevenue.add(price);
    }
}
