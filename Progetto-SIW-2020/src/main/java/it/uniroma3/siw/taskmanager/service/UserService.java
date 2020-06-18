package it.uniroma3.siw.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;
import it.uniroma3.siw.taskmanager.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected ProjectRepository projectRepository;
	
	
	@Transactional
	public User getUser(long id) {
		Optional<User> result= this.userRepository.findById(id);
		return result.orElse(null);
	}
	

	@Transactional 
	public User saveUser(User user) throws DataIntegrityViolationException {
		return this.userRepository.save(user); 
	}
	
	
	@Transactional
	public List<User> getAllUsers() {
		List<User> result= new ArrayList<>();
		Iterable<User> iterable= this.userRepository.findAll();
		for(User u: iterable) {
			result.add(u);
		}
		return result;
	}
	
	 @Transactional
	 public List<User> getMembers(Project project) {
		 return project.getMembers();
		 
	 }
	 
	 @Transactional 
	 public List<User> retrieveUsersNotMembers(List<User> members, User loggedUser) {
		List<User> usersNotMembers= this.getAllUsers();
		usersNotMembers.remove(loggedUser);
		for(User u: members) {
			usersNotMembers.remove(u);
		}
		
		return usersNotMembers;
	 }
	 
	 @Transactional
		public List<User> getAllUsersExceptLogged(User loggedUser) {
			List<User> result= new ArrayList<>();
			Iterable<User> iterable= this.userRepository.findAll();
			for(User u: iterable) {
				if(!u.equals(loggedUser)) {
				result.add(u);
				}
			}
			return result;
		}

}
