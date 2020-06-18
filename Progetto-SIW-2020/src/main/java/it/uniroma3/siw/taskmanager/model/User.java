package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(updatable=false, nullable=false)
	private LocalDateTime creationTimeStamp;
	
	@Column(nullable=false)
	private LocalDateTime lastUpdateTimeStamp;
	
	@OneToMany(mappedBy="owner", cascade= {CascadeType.REMOVE})  // fetch=FetchType.LAZY di default-> OneToMany
	List<Project> ownedProjects;
	
	@ManyToMany(mappedBy="members") // fetch=FetchType.LAZY di default-> Manytomany
	List<Project> visibleProjects;
	
	@OneToMany(mappedBy="user")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Task> tasks;
	
	public User() {
		super();
		this.ownedProjects= new ArrayList<>();
		this.visibleProjects= new ArrayList<>();
	}
	
	public User(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the creationTimeStamp
	 */
	public LocalDateTime getCreationTimeStamp() {
		return creationTimeStamp;
	}

	/**
	 * @param creationTimeStamp the creationTimeStamp to set
	 */
	public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	/**
	 * @return the lastUpdateTimeStamp
	 */
	public LocalDateTime getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	/**
	 * @param lastUpdateTimeStamp the lastUpdateTimeStamp to set
	 */
	public void setLastUpdateTimeStamp(LocalDateTime lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	/**
	 * @return the ownedProjects
	 */
	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	/**
	 * @param ownedProjects the ownedProjects to set
	 */
	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	/**
	 * @return the visibleProjects
	 */
	public List<Project> getVisibleProjects() {
		return visibleProjects;
	}

	/**
	 * @param visibleProjects the visibleProjects to set
	 */
	public void setVisibleProjects(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}
	
	public void addOwnedProject(Project p) {
		this.getOwnedProjects().add(p);
	}
	
	public void addVisibleProject(Project p) {
		this.getVisibleProjects().add(p);
	}
	
	public boolean deleteVisibleProject(Project p ) {
		return this.visibleProjects.remove(p);
	}

	
	@PrePersist
	protected void onPersist() {
		this.creationTimeStamp= LocalDateTime.now();
		this.lastUpdateTimeStamp=LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTimeStamp=LocalDateTime.now();
	}

	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
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
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", creationTimeStamp="
				+ creationTimeStamp + ", lastUpdateTimeStamp=" + lastUpdateTimeStamp + ", ownedProjects="
				+ ownedProjects + ", visibleProjects=" + visibleProjects + "]";
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
	
	public boolean addTask(Task task) {
		return this.tasks.add(task);
	}
	
	public void setCompletedTask(Task task, User user) {
		for(Task t: user.getTasks()) {
			if(t.equals(task)) {
				t.setCompleted(true);
			}
		}
	}
}
