package org.raif.delivery.core.application.queries;

import java.util.UUID;
import org.raif.delivery.core.application.queries.dto.OrderDto;
import org.raif.delivery.core.domain.kernal.Location;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GetUnfinishedOrdersQueryHandlerImpl implements GetUnfinishedOrdersQueryHandler {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GetUnfinishedOrdersQueryHandlerImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Result<GetUnfinishedOrdersResponse, Error> handle() {
        String sql = """
                SELECT o.order_id, o.location_x, o.location_y
                FROM orders o
                WHERE o.status IN ('ASSIGNED','CREATED')
                """;
        var response = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UUID orderId = UUID.fromString(rs.getString("order_id"));
            int x = rs.getInt("location_x");
            int y = rs.getInt("location_y");
            Location location = Location.create(x, y).getValue();
            return new OrderDto(orderId, location);
        });

        return Result.success(GetUnfinishedOrdersResponse.create(response).getValue());
    }
}
