package it.uniroma3.siw.taskmanager.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false, length=100)
	private String name;
	
	@Column
	private String description; 
	
	
	@ManyToOne(fetch=FetchType.EAGER)  //btw lo e di deafult
	private User owner;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<User> members;
	
	@OneToMany(cascade= {CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="project_id")
	private List<Task> tasks;
	
	@OneToMany(cascade= {CascadeType.ALL})
	private List<Tag> tags;
	
	public Project() {
		super();
		this.members= new ArrayList<>();
		this.tasks= new ArrayList<>();
		this.tags= new ArrayList<>();
	}
	
	public Project(String name, String description) {
		super();
		this.members= new ArrayList<>();
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return the members
	 */
	public List<User> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<User> members) {
		this.members = members;
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTasks(Task t) {
		this.getTasks().add(t);
	}
	
	
	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public boolean addTag(Tag t) {
		return this.getTags().add(t);
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public void addMembers(User u) {
		this.getMembers().add(u);
	}
	
	public boolean deleteMember(User u) {
		return this.members.remove(u);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [Id="+id+" name=" + name + ", description=" + description + ", owner=" + owner + ", members=" + members
				+ ", tasks=" + tasks + "]";
	}
	
	
	public List<Task> getTasksWithoutTag(Tag tag) {
		List<Task> result= new ArrayList<>();
		for(Task t: this.getTasks()) {
			if(!t.getTags().contains(tag)) {
				result.add(t);
			}
		}
		return result;
	}
}
