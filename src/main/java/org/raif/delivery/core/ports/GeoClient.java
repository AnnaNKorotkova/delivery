package org.raif.delivery.core.ports;

import org.raif.delivery.core.domain.kernel.Location;
import org.raif.libs.errs.Result;
import org.raif.libs.errs.Error;

public interface GeoClient {
    Result<Location, Error> getLocation(String street);
}
