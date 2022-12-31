package de.thb.anylearn.repository;

import de.thb.anylearn.entity.CardCategory;
import de.thb.anylearn.entity.CardUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryDefinition(domainClass = CardUser.class, idClass = Integer.class)
public interface CardUserRepository extends CrudRepository<CardUser, Integer> {

    @Query("SELECT c FROM CardUser c WHERE c.card.id = :cardId")
    List<CardUser> findAllByCardId(@Param("cardId") int cardId);

    @Query(value="SELECT c FROM CardUser c WHERE c.card.id = :cardId AND c.user.id = :userId")
    List<CardUser> findByCardUserId(@Param("cardId") int cardId, @Param("userId") int userId);

}
