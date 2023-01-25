package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = Card.class, idClass = Integer.class)
public interface CardRepository extends CrudRepository<Card, Integer> {

    @Query("SELECT c FROM Card c WHERE c.folder.id = :folderId")
    List<Card> findAllByFolderId(@Param("folderId") int folderId);

    @Query("SELECT c FROM Card c WHERE c.owner.id = :ownerId")
    List<Card> findAllByOwnerId(@Param("ownerId") int ownerId);
}
