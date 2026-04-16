package com.it.exalt.belair.domain.order;

import java.util.Set;

public interface PreparationTimeEstimator {
    /**
     * Estimate preparation time in minutes for the given set of distinct article types.
     */
    int estimateMinutes(Set<ArticleType> distinctTypes);
}
