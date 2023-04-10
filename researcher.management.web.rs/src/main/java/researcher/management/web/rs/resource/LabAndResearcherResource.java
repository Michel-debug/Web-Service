package researcher.management.web.rs.resource;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import researcher.management.web.rs.data.Lab;
import researcher.management.web.rs.data.Researcher;
import researcher.management.web.rs.service.LabAndResearcherService;

@Path("/LabAndResearchers")
public class LabAndResearcherResource {
	LabAndResearcherService service = new LabAndResearcherService();
	@Context
	UriInfo uriInfo;

	@POST
	@Path("/labs")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createLab(Lab lab) {
		boolean success = service.createLab(lab);
		if (success) {
			 	URI uri = uriInfo.getRequestUri();
			 	// 避免url路径中出现空格 给空格转码
			 	String Name = URLEncoder.encode(lab.getName(), StandardCharsets.UTF_8);
		        String newUri = uri.getPath() + "/" + Name;
			return Response.status(Response.Status.CREATED).entity(lab).contentLocation(uri.resolve(newUri)).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Lab with the given name already exists.").build();
		}
	}

	@POST
	@Path("/researchers")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createResearcher(Researcher researcher) {
		boolean success = service.createResearcher(researcher);
		if (success) {
			URI uri = uriInfo.getRequestUri();
	        String newUri = uri.getPath() + "/" + researcher.getId();
			return Response.status(Response.Status.CREATED).entity(researcher).contentLocation(uri.resolve(newUri)).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Researcher with the given ID already exists.")
					.build();
		}
	}

	@PUT
	@Path("/labs/{labName}/researchers/{researcherId}")
	public Response linkLabWithResearcher(@PathParam("labName") String labName,
			@PathParam("researcherId") String researcherId) {
		boolean success = service.linkLabWithResearcher(labName, researcherId);
		if (success) {
			return Response.status(Response.Status.OK).entity("Lab and researcher linked successfully.").build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Lab or researcher not found.").build();
		}
	}

	@GET
    @Path("/labs/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getLabByName(@PathParam("name") String name) {
        Lab lab = service.getLabByName(name);
        if (lab == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Link link = Link.fromUri(uriInfo.getRequestUri())
                .rel("self")
                .type("application/xml")
                .build();
        return Response.status(Response.Status.OK)
                .entity(lab)
                .links(link)
                .build();
    }

	@GET
	@Path("/labs/{labName}/researchers")
	@Produces(MediaType.APPLICATION_XML)
	public Response getResearchersByLab(@PathParam("labName") String labName) {
		List<Researcher> researchers = service.getResearcherByLab(labName);
		Researcher[] researchers2 = researchers.toArray(new Researcher[researchers.size()]);
		Link link = Link.fromUri(uriInfo.getRequestUri())
	            .rel("self")
	            .type("application/xml")
	            .build();

	    return Response.status(Response.Status.OK)
	            .entity(researchers2)
	            .links(link)
	            .build();
	}
	
	@DELETE
	@Path("/labs/{name}")
	 public Response deleteLab(@PathParam("name") String name) {
		boolean isDeleted = service.deleteLab(name);
	    if (isDeleted) {
	        Link link = Link.fromUri(uriInfo.getRequestUri())
	                .rel("self")
	                .type("application/xml")
	                .build();

	        return Response.status(Response.Status.OK)
	        		.entity("delete lab successful!")
	                .links(link)
	                .build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND)
	                .entity("Lab with the given name not found.")
	                .build();
	    }
    }
	@DELETE
	@Path("/researchers/{id}")
	 public Response deleteResearcher(@PathParam("id") String researcherId) {
		boolean isDeleted = service.deleteResearcher(researcherId);
	    if (isDeleted) {
	        Link link = Link.fromUri(uriInfo.getRequestUri())
	                .rel("self")
	                .type("application/xml")
	                .build();

	        return Response.status(Response.Status.OK)
	        		.entity("delete researcher successful!")
	                .links(link)
	                .build();
	    } else {
	        return Response.status(Response.Status.NOT_FOUND)
	                .entity("Lab with the given name not found.")
	                .build();
	    }
    }

}