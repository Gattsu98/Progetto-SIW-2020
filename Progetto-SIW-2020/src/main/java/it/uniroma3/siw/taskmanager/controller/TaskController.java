package it.uniroma3.siw.taskmanager.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;


	@RequestMapping(value= {"/projects/{projectId}/addTask"}, method= RequestMethod.GET)
	public String showAddTaskForm (Model model, @PathVariable Long projectId) {
		Project projFromDb= this.projectService.getProject(projectId);
		model.addAttribute("project", projFromDb);
		model.addAttribute("task", new Task());
		return "addTask";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/addTask"}, method= RequestMethod.POST)
	public String addTask(Model model,@Valid @ModelAttribute("task") Task task, @PathVariable Long projectId) {
		Project projFromDb= this.projectService.getProject(projectId);
		task.setCompleted(false);
		projFromDb.addTasks(task);
		this.projectService.saveProject(projFromDb);
		return "redirect:/projects/" + projFromDb.getId() ;
	}
	
	@RequestMapping(value= {"/projects/{projectId}/tasks/{taskId}"}, method= RequestMethod.GET)
	public String task (Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project projFromDb= this.projectService.getProject(projectId);
		Task taskFromDb= this.taskService.getTask(taskId);
		List<Tag> tags= taskFromDb.getTags();
		
		Credentials credentialsAssigned=new Credentials();;
		if(taskFromDb.getUser()!=null) {
		credentialsAssigned=this.credentialsService.getCredentialsByUserId(taskFromDb.getUser().getId());
		}
		
		model.addAttribute("credentialsAssigned", credentialsAssigned);
		model.addAttribute("comment", new Comment());
		model.addAttribute("loggedUser", sessionData.getLoggedUser());
		model.addAttribute("project", projFromDb);
		model.addAttribute("task", taskFromDb);
		model.addAttribute("tags", tags);
		return "task";
	}
	
	@RequestMapping(value= {"/tasks"}, method= RequestMethod.GET)
	public String myTasks(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Task> tasks= loggedUser.getTasks();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("tasks", tasks);
		return "myTasks";
	}
	
	@RequestMapping(value= {"/tasks/{taskId}"}, method= RequestMethod.GET)
	public String taskHandler(Model model, @PathVariable("taskId") Long taskId) {
		Task task = this.taskService.getTask(taskId);
		Project project= this.projectService.getProject(this.projectService.findIdProjectByTaskId(taskId));
		
		return "redirect:/projects/"+project.getId()+"/tasks/"+task.getId();
	}
	
/**************************************************************MODIFICA TASK*****************************************************************/
	
	@RequestMapping(value= {"/projects/task/{taskId}/editTask"}, method= RequestMethod.GET)
	public String editTaskForm (Model model, @PathVariable Long taskId) {
		Task taskFromDb= this.taskService.getTask(taskId);
		model.addAttribute("task", taskFromDb);
		return "editTask";
	}
	
	@RequestMapping(value= {"/projects/task/{taskId}/editTask"}, method= RequestMethod.POST)
	public String editTask (Model model, @PathVariable Long taskId, @Valid @ModelAttribute("task") Task task) {
		Task taskFromDb= this.taskService.getTask(taskId);
		taskFromDb.setLastUpdateTimeStamp(LocalDateTime.now());
		taskFromDb.setName(task.getName());
		taskFromDb.setDescription(task.getDescription());
		this.taskService.saveTask(taskFromDb);
		return "redirect:/tasks/" + taskId;
	}
	
/**************************************************CANCELLA TASK***************************************************************/
	
	@RequestMapping(value= {"/projects/task/{taskId}/deleteTask"}, method= RequestMethod.POST)
	public String deleteTask(Model model, @PathVariable Long taskId) {
		Task task= this.taskService.getTask(taskId);
		Long idProject= this.projectService.findIdProjectByTaskId(taskId);
		for(Tag t: task.getTags()) {
			t.deleteTask(task);
		}
		
		this.taskService.deleteTask(task);
		
		Project projFromDb= this.projectService.getProject(idProject);
		
		return "redirect:/projects/" + projFromDb.getId();
	}
	
/*********************************************ASSEGNA TASK*****************************************/
	
	@RequestMapping(value= {"/projects/task/{taskId}/assignTask"}, method= RequestMethod.GET)
	public String showMembersForAssignment(Model model, @PathVariable Long taskId) {
		Task task= this.taskService.getTask(taskId);
		Long idProject= this.projectService.findIdProjectByTaskId(taskId);
		Project projFromDb= this.projectService.getProject(idProject);
		
		List<User> availableUsersList= projFromDb.getMembers();
		List<Credentials> availableCredentialsList= this.credentialsService.getCredentialsByUsers(availableUsersList);

		model.addAttribute("task", task);
		model.addAttribute("availableCredentialsList", availableCredentialsList);
		return "assignTask";
	}
	
	
	@RequestMapping(value= {"/task/{taskId}/assignTask/{userId}"}, method= RequestMethod.POST)
	public String assignTask(Model model, @PathVariable("userId") Long userId, @PathVariable("taskId") Long taskId) {
		Task task= this.taskService.getTask(taskId);
		User userFromDb= this.userService.getUser(userId);
		
		userFromDb.addTask(task);
		task.setUser(userFromDb);
		this.taskService.saveTask(task);
		this.userService.saveUser(userFromDb);
		return "redirect:/tasks/" + taskId;
	}
	
	
/**************************************************COMPLETA TASK****************************************************/
	@RequestMapping(value="/projects/task/{taskId}/completeTask", method = RequestMethod.POST)
	public String completeTask (Model model, @PathVariable("taskId") Long taskId ) {
		Task taskFromDb= this.taskService.getTask(taskId);
		User loggedUser= sessionData.getLoggedUser();
	
		loggedUser.setCompletedTask(taskFromDb, loggedUser);
		taskFromDb.setCompleted(true);
		
		this.taskService.saveTask(taskFromDb);
		
		return "redirect:/tasks/" + taskId;
	}
}
