package com.it.exalt.belair.domain.order;

import java.util.Set;

public class SimplePreparationTimeEstimator implements PreparationTimeEstimator {

    @Override
    public int estimateMinutes(Set<ArticleType> distinctTypes) {
        // Business rule (minimal): 2 minutes per distinct article type, at least 1 minute
        return Math.max(1, distinctTypes.size()) * 2;
    }
}
