package kafka.warehouse;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class StockRepository {

    private final JdbcTemplate jdbc;

    public StockRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public boolean markProcessed(String orderId){
        try{
            jdbc.update("INSERT INTO processed_orders(order_id, processed_at) VALUES(?,?)",
                    orderId, Timestamp.from(Instant.now()));
            return true;
        }catch (DuplicateKeyException e){
            return false;
        }
    }

    public boolean tryReserve(String productId, int quantity){
        int rows = jdbc.update(
                "UPDATE stock SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?",
                quantity, productId, quantity);
        return rows == 1;
    }

}
