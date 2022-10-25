package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Card.class, idClass = Long.class)
public interface CardRepository extends CrudRepository<Card, Long> {


    @Query("SELECT c FROM Card c WHERE c.folder.id = :folderId")
    List<Card> findAllByFolderId(@Param("folderId") int folderId);

}
