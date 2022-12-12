package de.thb.anylearn.repository;

import de.thb.anylearn.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserRepository extends CrudRepository<User, Integer> {
}
