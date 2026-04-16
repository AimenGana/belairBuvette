package com.it.exalt.belair.domain.order;

import java.util.HashSet;
import java.util.Set;

public class AcquitOrderAndEstimateTimeUseCase {

    private final OrderRepository orderRepository;
    private final ArticleCatalogPort articleCatalog;
    private final PreparationTimeEstimator estimator;

    public AcquitOrderAndEstimateTimeUseCase(OrderRepository orderRepository, ArticleCatalogPort articleCatalog) {
        this(orderRepository, articleCatalog, new SimplePreparationTimeEstimator());
    }

    public AcquitOrderAndEstimateTimeUseCase(OrderRepository orderRepository, ArticleCatalogPort articleCatalog, PreparationTimeEstimator estimator) {
        this.orderRepository = orderRepository;
        this.articleCatalog = articleCatalog;
        this.estimator = estimator;
    }

    public AcquitOrderAndEstimateTimeResult handle(AcquitOrderAndEstimateTimeCommand command) {
        var order = orderRepository.findById(command.orderId());
        if (order == null) return new AcquitOrderAndEstimateTimeResult(false, 0);

        Set<ArticleType> distinctTypes = new HashSet<>();
        for (OrderLine line : order.lines()) {
            var type = articleCatalog.typeOf(line.article());
            if (type == null) {
                return new AcquitOrderAndEstimateTimeResult(false, 0);
            }
            distinctTypes.add(type);
        }

        int etaMinutes = estimator.estimateMinutes(distinctTypes);

        orderRepository.updateStatus(order.id(), OrderStatus.PRETE);

        return new AcquitOrderAndEstimateTimeResult(true, etaMinutes);
    }
}
