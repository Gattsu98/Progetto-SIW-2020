package it.uniroma3.siw.taskmanager.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	protected ProjectRepository projectRepository;
	
	
	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result= this.projectRepository.findById(id);
		return result.orElse(null);
	}
	
	
	@Transactional
	public Project saveProject(Project p) {
		return this.projectRepository.save(p);	
	}
	
	@Transactional
	public void deleteProject(Project p) {
		this.projectRepository.delete(p);
	}
	
	@Transactional
	public Project shareProjectWithUser(Project p, User u) {
		p.addMembers(u);
		return this.projectRepository.save(p);
	}

	@Transactional
	public List<Project> retrieveProjectsOwnedBy(User loggedUser) {
		return this.projectRepository.findByOwner(loggedUser);
	}
	
	@Transactional	
	public List<Project> retrieveVisibleProjects(User loggedUser) {
		return this.projectRepository.findByMembers(loggedUser);
	}
	
	@Transactional
	public void deleteProjectById(Long id) {
		this.projectRepository.deleteById(id);
	}
	
	@Transactional
	public Long findIdProjectByTaskId(Long taskId) {
		return this.projectRepository.findByTaskId(taskId);
	}
}
