package it.uniroma3.siw.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.repository.TagRepository;

@Service
public class TagService {

	@Autowired
	TagRepository tagRepository;
	
	@Transactional
	public Tag getTag(long id) {
		Optional<Tag> result= this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public List<Tag> getAllTags() {
		Iterable<Tag> iterable= this.tagRepository.findAll();
		List<Tag> result= new ArrayList<>();
		for(Tag t: iterable) {
			result.add(t);
		}
		return result;
	}
	
	@Transactional
	public void deleteTag(Tag tag) {
		this.tagRepository.delete(tag);
	}
	
	
	
}
