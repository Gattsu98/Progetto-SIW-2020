package it.uniroma3.siw.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Tag {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@Column (nullable= false)
	private String name;
	
	@Column (nullable= false)
	private String color;
	
	@Column
	private String description;
	
	@ManyToMany
	private List<Task> tasks;
	
	public Tag() { 
		super();
		this.tasks= new ArrayList<>();
	}

	public Tag(String name, String color, String description) {
		super();
		this.name=name;
		this.color = color;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	
	public boolean deleteTask(Task t) {
		return this.tasks.remove(t);
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	
	public void addTasks(Task t) {
		this.tasks.add(t);
	}
	
	public boolean removeTasks(Task t) {
		return this.tasks.remove(t);
	}
	
	
}
