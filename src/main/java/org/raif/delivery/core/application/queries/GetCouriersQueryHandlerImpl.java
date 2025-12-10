package org.raif.delivery.core.application.queries;

import java.util.UUID;
import org.raif.delivery.core.application.queries.dto.CourierDto;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetCouriersQueryHandlerImpl implements GetCouriersQueryHandler {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GetCouriersQueryHandlerImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Result<GetCouriersResponse, Error> handle() {
        String sql = """
                SELECT c.courier_id, c.name, o.location_x, o.location_y
                FROM couriers c, orders o
                WHERE o.courier_id = c.courier_id
                AND o.status = 'ASSIGNED'
                """;
        var response = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UUID courierId = UUID.fromString(rs.getString("courier_id"));
            String name = rs.getString("name");
            int x = rs.getInt("location_x");
            int y = rs.getInt("location_y");

            Location location = Location.create(x, y).getValue();
            return new CourierDto(courierId, name, location);
        });


        return Result.success(GetCouriersResponse.create(response).getValue());
    }
}
