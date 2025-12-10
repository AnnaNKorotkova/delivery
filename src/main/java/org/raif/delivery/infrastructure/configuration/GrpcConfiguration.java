package org.raif.delivery.infrastructure.configuration;

import clients.geo.GeoGrpc;
import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

    @Autowired
    private GrpcProperties grpcProperties;

    @Bean
    public ManagedChannel geoServiceChannel() {
        return ManagedChannelBuilder.forAddress(grpcProperties.getHost(), grpcProperties.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public GeoGrpc.GeoBlockingStub geoServiceStub(ManagedChannel geoServiceChannel) {
        return GeoGrpc.newBlockingStub(geoServiceChannel);
    }

    @Bean
    public CallOptions callOptions() {
        return CallOptions.DEFAULT;
    }
}
