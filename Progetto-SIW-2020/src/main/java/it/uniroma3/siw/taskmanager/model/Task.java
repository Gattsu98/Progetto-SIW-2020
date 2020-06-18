package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column (nullable = false)
	private Boolean completed;
	
	@Column
	private String description;
	
	@Column(nullable=false, length=100)
	private String name;
	
	@Column(nullable=false, length=100)
	private LocalDateTime creationTimeStamp;
	
	@Column(nullable=false, length=100)
	private LocalDateTime lastUpdateTimeStamp;
	
	@ManyToMany (mappedBy= "tasks")
	private List<Tag> tags;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy="task", cascade= {CascadeType.ALL})
	private List<Comment> comments;
	
	public Task() {
		super();
		this.tags= new ArrayList<>();
	}
	public Task(String description, String name) {
		super();
		this.description = description;
		this.name = name;
	}
	public Task(String description, String name, LocalDateTime creationTimeStamp, LocalDateTime lastUpdateTimeStamp) {
		super();
		this.description = description;
		this.name = name;
		this.creationTimeStamp = creationTimeStamp;
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Task other = (Task) obj;
		return this.getName().equals(other.getName()) && this.getDescription().equals(other.getDescription());
	}
	@Override
	public String toString() {
		return "Task [Id="+id+ "description=" + description + ", name=" + name + ", creationTimeStamp=" + creationTimeStamp
				+ ", lastUpdateTimeStamp=" + lastUpdateTimeStamp + "]";
	}
	
	@PrePersist
	protected void onPersist() {
		this.creationTimeStamp= LocalDateTime.now();
		this.lastUpdateTimeStamp= LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.lastUpdateTimeStamp= LocalDateTime.now();
	}
	
	public Boolean getCompleted() {
		return this.completed;
	}
	/**
	 * @return the tags
	 */
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
		return this.tags.add(t);
	}
	
	public boolean deleteTag(Tag t) {
		return this.tags.remove(t);
	}
	/**
	 * @return the comments
	 */
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	/**
	 * @return the userAssigned
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param userAssigned the userAssigned to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public boolean addtComment(Comment comment) {
		return this.comments.add(comment);
	}

}
