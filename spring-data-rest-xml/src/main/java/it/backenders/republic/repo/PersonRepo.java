package it.backenders.republic.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import it.backenders.republic.entity.Person;

@RepositoryRestResource
public interface PersonRepo extends CrudRepository<Person, Long>
{
		@RestResource(path="byName") 
		public List<Person> findByPersonName(@Param("name") String personName);
}
