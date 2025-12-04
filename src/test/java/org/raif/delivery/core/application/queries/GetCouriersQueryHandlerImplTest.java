package org.raif.delivery.core.application.queries;

import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GetCouriersQueryHandlerImplTest extends BaseTest {

    @Test
    @Sql(scripts = "/get_couriers.sql")
    void shouldReturnListOfCouriers() {
        var handler = new GetCouriersQueryHandlerImpl(jdbcTemplate);

        var handle = handler.handle();

        assertTrue(handle.isSuccess());
        assertThat(handle.getValue().couriers()).hasSize(1);
    }

}