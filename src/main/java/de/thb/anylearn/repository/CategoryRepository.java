package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = Category.class, idClass = Integer.class)
public interface CategoryRepository extends CrudRepository<Category, Integer> {
}

