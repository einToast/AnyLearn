package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryDefinition(domainClass = Card.class, idClass = Integer.class)
public interface CardRepository extends CrudRepository<Card, Integer> {


    @Query("SELECT c FROM Card c WHERE c.folder.id = :folderId")
    List<Card> findAllByFolderId(@Param("folderId") int folderId);

    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.question = :question, c.answer = :answer, c.folder.id = :folderId WHERE c.id = :cardId")
    void updateCard(@Param("cardId") int cardId , @Param("question") String question, @Param("answer") String answer, @Param("folderId") int folderId);


}
