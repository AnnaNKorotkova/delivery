package org.raif.delivery.core.application.queries;

import org.junit.jupiter.api.Test;
import org.raif.delivery.BaseTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetUnfinishedOrdersQueryHandlerImplTest extends BaseTest {

    @Test
    @Sql(scripts = "/get_orders.sql")
    void testGetShortBasket() {
        var handler = new GetUnfinishedOrdersQueryHandlerImpl(jdbcTemplate);

        var handle = handler.handle();

        assertTrue(handle.isSuccess());
        assertThat(handle.getValue().orders()).hasSize(5);
    }


}