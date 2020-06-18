package it.uniroma3.siw.taskmanager;


import org.junit.Before;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.taskmanager.repository.ProjectRepository;
import it.uniroma3.siw.taskmanager.repository.TaskRepository;
import it.uniroma3.siw.taskmanager.repository.UserRepository;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@SpringBootTest
@RunWith(SpringRunner.class)
class TaskmanagerApplicationTests {


	@SuppressWarnings("unused")
	@Autowired
	private TaskService taskService;

	@SuppressWarnings("unused")
	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Before
	public void deleteAll() {
		System.out.println("Deleting all data in DB...");
		userRepository.deleteAll();
		taskRepository.deleteAll();
		projectRepository.deleteAll();
		System.out.println("DONE");
	}

}
