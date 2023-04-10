package researcher.management.client;

import java.awt.print.Printable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.cxf.jaxrs.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import researcher.management.web.*;
import researcher.management.web.rs.data.Lab;
import researcher.management.web.rs.data.Researcher;
import researcher.management.web.ws.service.LabAndResearcherWebService;
import researcher.management.web.ws.service.LabAndResearcherWebServiceImpl;

public class Test {

	/**
	 * ---------------------------------------------------RS----------------------------------------------------------------
	 */
	// le website local
	private static final String REST_SERVICE_URL = "http://localhost:8080/researcher.management.web.rs/api/LabAndResearchers/";
	// le url external api
	private static final String SEMANTIC_SCHOLAR_API = "https://api.semanticscholar.org/graph/v1/author";
	// stocker temporairement des noms de lab venant de api.semanticscholar.org
	private static ArrayList<String> lab_name_arrayList = new ArrayList<>();

	// la fonctoin createLab
	private static Boolean createLab(Lab lab) {
		System.out.println("Create lab:" + lab.getName());
		WebClient client = WebClient.create(REST_SERVICE_URL).path("labs");
		client.type(MediaType.APPLICATION_XML);
		int status = client.post(lab).getStatus();

		if (status == 200 || status == 201) {
			System.out.println("ok. bien creer");
			return true;
		} else {
			System.out.println("oops");
			return false;
		}
	}

	// la fonction createResearcher
	private static Boolean createResearcher(Researcher researcher) {
		System.out.println("Create researcher_ID " + researcher.getId());
		WebClient client = WebClient.create(REST_SERVICE_URL).path("researchers");
		client.type(MediaType.APPLICATION_XML);
		int status = client.post(researcher).getStatus();
		if (status == 200 || status == 201) {
			System.out.println("ok. bien creer");
			return true;
		} else {
			System.out.println("oops");
			return false;
		}
	}

	// la fonction deletelab
	private static Boolean Deletelab(String lab_name) {
		System.out.println("delete lab" + lab_name);
		WebClient client = WebClient.create(REST_SERVICE_URL).path("labs/" + lab_name);
		client.type(MediaType.APPLICATION_XML);
		int status = client.delete().getStatus();
		if (status == 200 || status == 201) {
			System.out.println("ok. bien delete");
			return true;
		} else {
			System.out.println("oops");
			return false;
		}
	}

	private static Boolean deleteResearcher(String researcherid) {
		System.out.println("Delete rows_researcher_ID:" + researcherid);
		WebClient client = WebClient.create(REST_SERVICE_URL).path("researchers/" + researcherid);
		client.type(MediaType.APPLICATION_XML);
		int status = client.delete().getStatus();
		if (status == 200 || status == 201) {
			System.out.println("ok. bien delete");
			return true;
		} else {
			System.out.println("oops");
			return false;
		}
	}

	private static Lab getLabbyName(String name) {
		System.out.println("getting Lab :" + name + "...");
		WebClient client = WebClient.create(REST_SERVICE_URL).path("labs/" + name);
		client.type(MediaType.APPLICATION_XML);
		Lab lab = null;
		try {
			lab = client.get(Lab.class);
			System.out.println(lab.toString());
		} catch (NotFoundException e) {
			// TODO: handle exception
			System.out.println("oops");
		}
		return lab;
	}

	private static Researcher[] getResearchersByLab(String labName) {
		System.out.println("getting reseachers  from :" + labName);
		WebClient client = WebClient.create(REST_SERVICE_URL).path("labs/" + labName + "/researchers");
		client.type(MediaType.APPLICATION_XML);

		Researcher[] researcher_arrlist = null;
		try {
			researcher_arrlist = client.get(Researcher[].class);
			System.out.println(researcher_arrlist);
		} catch (NotFoundException e) {
			// TODO: handle exception
			System.out.println("oops");
		}
		return researcher_arrlist;
	}

	private static Boolean updateResearcherLab(String labname, String researchId) {
		System.out.println("update la lab de rechercher " + researchId + " lab_name :" + labname);
		WebClient client = WebClient.create(REST_SERVICE_URL).path("labs/" + labname + "/researchers/" + researchId);
		client.type(MediaType.APPLICATION_XML);
		Response response = client.put(null);
		int status = response.getStatus();
		if (status == 200 || status == 201) {
			System.out.println("Lab and researcher linked successfully.");
			return true;
		} else {
			System.out.println("Lab or researcher not found");
			return false;
		}
	}

	/*
	 * En utilisant l'API officielle, nous avons obtenu une URL contenant plusieurs
	 * paramètres relatifs à l'auteur , puis nous avons appelé une requête pour
	 * récupérer ces valeurs.
	 */
	private static void GetExterneResearcherInfo(String researcherId) {
		// En utilisant l'API officielle, nous avons obtenu une URL contenant plusieurs
		// paramètres relatifs à l'auteur, puis nous avons appelé une requête pour
		// récupérer ces valeurs.
		String field = "name,url,affiliations,paperCount";
		WebClient client = WebClient.create(SEMANTIC_SCHOLAR_API).path(researcherId).query("fields", field);
		client.accept(MediaType.APPLICATION_JSON);

		try {
			String jsonResponse = client.get(String.class);
			System.out.println(jsonResponse);
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> authorData = objectMapper.readValue(jsonResponse, Map.class);

			String name = (String) authorData.get("name");
			List<String> affiliations = (List<String>) authorData.get("affiliations");
			String url = (String) authorData.get("url");

			Integer paperCount = (Integer) authorData.get("paperCount");
			String lab_name = "";
			for (String string : affiliations) {
				lab_name = lab_name.concat(string + " ");
			}
			lab_name_arrayList.add(lab_name);
			Lab lab = new Lab(lab_name, "CE LAB VIENS DE api.semanticscholar.org");
			createLab(lab);
			Researcher researcher = new Researcher(researcherId, name, url, lab_name, paperCount);
			createResearcher(researcher);
			System.out.println("Name: " + name);
			System.out.println("Email: " + url);
			System.out.println("Lab : " + lab_name);
			System.out.println("Number of Publications: " + paperCount);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		// Test pour jax ws
		/* Le test pour JAX-WS */
		
		System.out.println(		"-----------------------------------------Ws-test---------------------------------------------------");
		try {
			researcher.management.web.ws.model.Lab lab_f = new researcher.management.web.ws.model.Lab("Frenche_lab",
					"client test-ws");
			researcher.management.web.ws.model.Lab lab_c = new researcher.management.web.ws.model.Lab("Chinese_lab",
					"client test-ws");
			URL wsdlURL = new URL(
					"http://localhost:8080/researcher.management.web.ws/services/LabAndResearcherWebServiceImplPort?wsdl");

			QName serviceName = new QName("http://service.ws.web.management.researcher/",
					"LabAndResearcherWebServiceImpl");

			Service service = Service.create(wsdlURL, serviceName);
			LabAndResearcherWebService proxy = service.getPort(LabAndResearcherWebService.class);
			// Tester la fonction createLab
			Boolean bol = proxy.createLab(lab_c);
			System.out.println("le lab" + lab_c.getName() + " est crée: " + bol);
			// Tester la fonction createResearcher
			researcher.management.web.ws.model.Researcher researcher_China = new researcher.management.web.ws.model.Researcher(
					"0077", "Junjie CHEN", "Michel@gmail.com", "Chinese_lab", 23);
			bol = proxy.createResearcher(researcher_China);
			System.out.println("A " + researcher_China.getName() + " est crée: " + bol);
			// Tester la fonction deleteLab
			bol = proxy.deleteLab(lab_c.getName());
			System.out.println("A " + lab_c.getName() + " est supprimé: " + bol);
			// Tester la fonction deleteresearcher
			bol = proxy.deleteResearcher(researcher_China.getId());
			System.out.println("A " + researcher_China.getName() + " est supprimé: " + bol);

			List<researcher.management.web.ws.model.Researcher> researchers = proxy
					.getResearcherByLab("French Nantional Research Center");
			for (researcher.management.web.ws.model.Researcher researcher : researchers) {
				System.out.println(researcher);
			}
			researcher.management.web.ws.model.Lab rechercheLab = proxy
					.getLabByName("French Nantional Research Center");
			System.out.println(rechercheLab);
			bol = proxy.linkLabWithResearcher("Chinese Nantional Research Center", "10");
			System.out.println("update info : " + bol);

		} catch (MalformedURLException e) {
			System.out.println("Error: " + e.getMessage());
		}

		System.out.println(	"-----------------------------------------Rs-test---------------------------------------------------");
		
		

		// Test pour jax rs
		Lab lab_f = new Lab("Frenche_lab_rs", "client test-rs");
		Lab lab_c = new Lab("Chinese_lab_rs", "client test-rs");

		Researcher researcher1 = new Researcher("0006", "Junjie CHEN", "Michel@gmail.com", "Chinese_lab_rs", 25);
		Researcher researcher2 = new Researcher("0012", "FENG Jiaqi", "724915929@qq.com", "Frenche_lab_rs", 30);
		Researcher researcher3 = new Researcher("0013", "WANG Jianguo", "724915929@qq.com", "Frenche_lab_rs", 35);
		Researcher researcher4 = new Researcher("0014", "ZHANG SAN", "724915929@qq.com", "Frenche_lab_rs", 40);
		Researcher researcher5 = new Researcher("0015", "LI Si", "724915929@qq.com", "Frenche_lab_rs", 45);

		// La manipulation local // Create labs 
		createLab(lab_f);
		createLab(lab_c);

		// Create researchers 
		createResearcher(researcher1);
		createResearcher(researcher2);
		createResearcher(researcher3);
		createResearcher(researcher4);
		createResearcher(researcher5);

		// Get lab by name Lab retrievedLab = getLabbyName("Frenche lab"); // Get
		// researchers by lab name

		Researcher[] researchers = getResearchersByLab("Frenche_lab_rs");
		for (Researcher researcher : researchers) {
			System.out.println(researcher);
		}

		// Update researcher's lab
		boolean updated = updateResearcherLab("Chinese_lab_rs", "0011");
		System.out.println("updated : " + updated);
		// Delete lab
		Deletelab("Frenche_lab_rs");
		Deletelab("Chinese_lab_rs");
		// Delete researcher
		deleteResearcher("0006");
		deleteResearcher("0012");
		deleteResearcher("0013");
		deleteResearcher("0014");
		deleteResearcher("0015");

		// Appel d’API externes
		String[] researcherId = new String[] { "145612610", "48808806", "82676859", "46212260", "143967880", "1694037",
				"1857797" };
		for (String string : researcherId) {
			GetExterneResearcherInfo(string);
		}
		// supprimer les recherches and labs dans notre base de donnees;

		for (String string : researcherId) {
			deleteResearcher(string);
		}
		for (String string : lab_name_arrayList) {
			Deletelab(string);
		}

	}

}
