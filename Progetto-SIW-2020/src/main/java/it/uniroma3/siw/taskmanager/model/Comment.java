package it.uniroma3.siw.taskmanager.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column (nullable= false)
	private String text;
	
	@ManyToOne	
	@JoinColumn(name="task_id")
	private Task task;
	
	@Column(nullable=false, length=100)
	private LocalDateTime creationTimeStamp;
	
	@Column(nullable=false, length=100)
	private LocalDateTime lastUpdateTimeStamp;
	
	public Comment() { super(); }

	public Comment(String text, Task task) {
		super();
		this.text = text;
		this.task = task;
	}

	
	public Long getId() { return id; }

	
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
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
	
	
	
	
}
