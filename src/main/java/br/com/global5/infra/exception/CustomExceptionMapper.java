package br.com.global5.infra.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by j r zielinski on 12/20/14.
 */
@Provider
@SuppressWarnings({"rawtypes"})
public class CustomExceptionMapper implements ExceptionMapper<CustomException> {

    @SuppressWarnings("unchecked")
	@Override
    public Response toResponse(CustomException e) {
		Map map = new HashMap();
        map.put("message", e.getMessage());

        if (e.getMessage().equals("Access forbidden")) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).entity(map).build();
        }
        if (e.getMessage().equals("Access denied")) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(map).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(map).build();
    }
}
