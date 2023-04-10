package researcher.management.web.ws.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import researcher.management.web.ws.model.*;


@WebService(name="LabAndResearcherWebService",targetNamespace="http://service.ws.web.management.researcher/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface LabAndResearcherWebService {
	@WebMethod(operationName = "CreateLab")
	boolean createLab(@WebParam(name="Lab")Lab lab);

	@WebMethod(operationName = "GetLabByName")
	 Lab getLabByName(@WebParam(name="name")String name);
    @WebMethod(operationName = "deleteLabByName")
	public boolean deleteLab(@WebParam(name="labname")String labName);
	@WebMethod
	boolean createResearcher(@WebParam(name="researcher")Researcher researcher);
	
	@WebMethod
	public boolean linkLabWithResearcher(@WebParam(name="labName")String labName, @WebParam(name="researcherId")String researcherId);
	
	
	@WebMethod
	List<Researcher> getResearcherByLab(@WebParam(name="labName")String labName);
	
	@WebMethod
	public boolean deleteResearcher(@WebParam(name="researcherId")String researcherId);
}
