package it.uniroma3.siw.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Comment;
import it.uniroma3.siw.taskmanager.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Transactional
	public Comment getComment(long id) {
		Optional<Comment> result= this.commentRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Comment saveComment(Comment comment) {
		return this.commentRepository.save(comment);
	}
	
	@Transactional
	public List<Comment> getAllComments() {
		Iterable<Comment> iterable= this.commentRepository.findAll();
		List<Comment> result= new ArrayList<>();
		for(Comment c : iterable ) {
			result.add(c);
		}
		return result;
	}
	
	@Transactional
	public void deleteComment(Comment comment) {
		this.commentRepository.delete(comment);
	}
	
	
}
