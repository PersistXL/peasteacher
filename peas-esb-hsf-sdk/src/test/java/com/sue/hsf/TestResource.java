package com.sue.hsf;

import com.peas.hsf.media.MediaResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * Created by duanyihui on 2017/5/25.
 */
@Path("test")
public class TestResource {

    @GET
    public String test(@Context HttpServletRequest request) {
        System.out.println(request.getRequestedSessionId());
        System.out.println(request.getSession().getId());
        return "test";
    }

    @GET
    @Path("/video/{name}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get3(@HeaderParam("Range") String range, @PathParam("name") String name) throws Exception {
        return MediaResponseBuilder.build(new File("D:\\t.mp4"), range);
    }
}
