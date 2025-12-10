package org.raif.delivery.adapters.out.grpc;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.raif.delivery.core.domain.kernel.Location;
import org.raif.delivery.core.ports.GeoClient;
import org.raif.libs.errs.Error;
import org.raif.libs.errs.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class GeoClientImpl implements GeoClient {

    @Autowired
    @Qualifier("geoServiceStub")
    private GeoGrpc.GeoBlockingStub stub;


    @Override
    public Result<Location, Error> getLocation(String street) {

        if (stub == null) {
            return Result.failure(Error.of("","gRPC stub is not initialized"));
        }
        try {
            GeoProto.GetGeolocationReply response = stub
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getGeolocation(GeoProto.GetGeolocationRequest.newBuilder()
                            .setStreet(street)
                            .build());

            GeoProto.Location location = response.getLocation();

            return Result.success(Location.create(location.getX(), location.getY()).getValue());

        } catch (StatusRuntimeException e) {
            return Result.failure(Error.of("call.failed", "gRPC call failed: " + e.getStatus()));
        } catch (Exception e) {
            return Result.failure(Error.of("unexpected", "Unexpected error: " + e.getMessage()));
        }
    }
}

