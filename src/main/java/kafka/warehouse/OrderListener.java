package kafka.warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class OrderListener {
    private static final Logger log = LoggerFactory.getLogger(OrderListener.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final StockRepository stockRepository;

    public OrderListener(KafkaTemplate<String, Object> kafkaTemplate, StockRepository stockRepository){
        this.kafkaTemplate = kafkaTemplate;
        this.stockRepository = stockRepository;
    }

    @KafkaListener(topics = "orders")
    @Transactional
    public void onOrderCreated(OrderCreated order) throws Exception{
        if (!stockRepository.markProcessed(order.orderId())){
            log.info("Skipping duplicate order {}", order.orderId());
            return;
        }

        StockResult result;
        if (stockRepository.tryReserve(order.productId(), order.quantity())){
            result = new StockResult(order.orderId(), "RESERVED", null, Instant.now());
            log.info("Stock RESERVED for {} ({} x {})", order.orderId(), order.quantity(), order.productId());
        } else {
            result = new StockResult(order.orderId(), "REJECTED", "Not enough in stock", Instant.now());
            log.info("Stock REJECTED for {}", order.orderId());
        }
        kafkaTemplate.send("warehouse",order.orderId(), result).get();
    }

}
