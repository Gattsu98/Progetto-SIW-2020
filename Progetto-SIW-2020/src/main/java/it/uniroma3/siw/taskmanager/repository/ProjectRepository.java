package it.uniroma3.siw.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	public List<Project> findByMembers(User member);
	
	public List<Project> findByOwner(User owner);
	
	public Optional<Project> findById(Long id);
	
	public void deleteById(Long id);
	

	@Query(value= "select project_id from Task where id= ?1", nativeQuery= true)
	public Long findByTaskId(Long taskId);

}
