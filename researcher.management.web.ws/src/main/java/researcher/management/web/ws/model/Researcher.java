package researcher.management.web.ws.model;

public class Researcher {
	private String id;
	private String name;
	private String email;
	private String labName;
	private int Total_article;


	public int getTotal_article() {
		return Total_article;
	}

	public void setTotal_article(int total_article) {
		Total_article = total_article;
	}

	public Researcher() {
		super();
	}
	
	public Researcher(String id, String name, String email, String labName,int total_article) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.labName = labName;
		this.Total_article = total_article;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}

	@Override
	public String toString() {
		return "Researcher [id=" + id + ", name=" + name + ", email=" + email + ", labName=" + labName
				+ ", Total_article=" + Total_article + "]";
	}
	
	
}
