package com.it.exalt.belair.domain.order;

public interface ArticleCatalogPort {
    /** Returns the article type for the given article id, or null if unknown. */
    ArticleType typeOf(String articleId);
}
