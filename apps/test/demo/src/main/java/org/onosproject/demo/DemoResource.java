/*
 * Copyright 2014-2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onlab.rest.BaseResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Rest API for demos.
 */
@Path("intents")
public class DemoResource extends BaseResource {



    @POST
    @Path("flowTest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response flowTest(InputStream input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cfg = mapper.readTree(input);
        DemoAPI demo = get(DemoAPI.class);
        return Response.ok(demo.flowTest(Optional.ofNullable(cfg)).toString()).build();
    }

    @POST
    @Path("setup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setup(InputStream input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cfg = mapper.readTree(input);
        if (!cfg.has("type")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Expected type field containing either mesh or random.").build();
        }


        DemoAPI.InstallType type = DemoAPI.InstallType.valueOf(
                cfg.get("type").asText().toUpperCase());
        DemoAPI demo = get(DemoAPI.class);
        demo.setup(type, Optional.ofNullable(cfg.get("runParams")));

        return Response.ok(mapper.createObjectNode().toString()).build();
    }

    @GET
    @Path("teardown")
    @Produces(MediaType.APPLICATION_JSON)
    public Response tearDown() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DemoAPI demo = get(DemoAPI.class);
        demo.tearDown();
        return Response.ok(mapper.createObjectNode().toString()).build();
    }

}
