package pnnl.goss.powergrid.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;

import pnnl.goss.powergrid.datamodel.Powergrid;

public interface PowergridService {

	/**
     * Returns a list of Powergrid objects that are available.  The properties
     * specified are used in the other service functions available.
     *
     * @return A list of {@link Powergrid} objects.
     */
    public List<Powergrid> getPowergrids(String identifier);

    /**
     * Returns the powergrid model for the specified mrid.
     *
     * @param mrid
     * @return A {@link PowergridModel} object
     */
    public PowergridModel getPowergridModel(String mrid, String identifier);

    /**
     * Returns the {@link PowergridModel} for the specified mrid
     * at the specified time.
     *
     * @param mrid
     * @param timestep
     * @return
     */
    public PowergridModel getPowergridModel(String mrid, String timestep, String identifier);
}
