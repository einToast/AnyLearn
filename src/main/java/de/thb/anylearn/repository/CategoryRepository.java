package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Category.class, idClass = Integer.class)
public interface CategoryRepository extends CrudRepository<Category, Integer> {

}

