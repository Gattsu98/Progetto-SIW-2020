package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class TagController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired 
	private TaskService taskService;
	
	@Autowired 
	private TagService tagService;
	
	
/************************************TAG FOR PROJECT*********************************************************/
	
	@RequestMapping(value= {"/projects/{projectId}/addTag"}, method=RequestMethod.GET)
	public String showAddTagForProject(Model model, @PathVariable("projectId") Long projectId ) {
		Project projFromDb= this.projectService.getProject(projectId);
		model.addAttribute("tag", new Tag());
		model.addAttribute("entity", projFromDb);
		return "addTag";
	}
	
	@RequestMapping(value= {"/target/{id}/tags/addTag"}, method= RequestMethod.POST)
	public String addTag(Model model, @PathVariable("id") Long id, @Valid @ModelAttribute("tag") Tag tag) {
		Project projFromDb= this.projectService.getProject(id);
			projFromDb.addTag(tag);
			this.projectService.saveProject(projFromDb);
			
		return "redirect:/projects/" + projFromDb.getId(); 					
	}
	
	
/**********************************************TAG FOR TASKS*****************************************************/	
	
	@RequestMapping(value= {"/projects/{projectId}/tag/{tagId}/assignTag"}, method=RequestMethod.GET)
	public String goToassignTag(Model model, @PathVariable("projectId") Long projectId, @PathVariable("tagId") Long tagId) {
		Tag tagFromDb= this.tagService.getTag(tagId);
		Project projFromDb= this.projectService.getProject(projectId);
		List<Task> availableTasks = projFromDb.getTasksWithoutTag(tagFromDb);
		model.addAttribute("tag", tagFromDb);
		model.addAttribute("project", projFromDb);
		model.addAttribute("availableTask", availableTasks);
		return "assignTag";
	}
	
	@RequestMapping(value= {"/tasks/{taskId}/tag/{tagId}/assignTag"}, method=RequestMethod.POST)
	public String assignTag(Model model, @PathVariable("taskId") Long taskId, @PathVariable("tagId") Long tagId) {
		Tag tagFromDb= this.tagService.getTag(tagId);
		Task taskFromDb= this.taskService.getTask(taskId);
		
		taskFromDb.addTag(tagFromDb);
		tagFromDb.addTasks(taskFromDb);
		this.taskService.saveTask(taskFromDb);
		this.tagService.saveTag(tagFromDb);
		return "redirect:/tasks/"+ taskId;
	}
}
