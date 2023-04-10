package researcher.management.web.rs.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lab {
	private String name;
	private String description;
	public Lab() {
		
	}
	public Lab(String name, String description) {
		this.name = name;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Lab [name=" + name + ", description=" + description + "]";
	}
	
}
