package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Card.class, idClass = Long.class)
public interface CardRepository extends CrudRepository<Card, Long> {


}
