package kafka.warehouse;

import java.time.Instant;

public record StockResult(
        String orderId,
        String status,
        String reason,
        Instant timestamp
) {}
