package de.thb.anylearn.repository;

import de.thb.anylearn.entity.CardCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryDefinition(domainClass = CardCategory.class, idClass = Integer.class)
public interface CardCategoryRepository extends CrudRepository<CardCategory, Integer> {

    @Query("SELECT c FROM CardCategory c WHERE c.card.id = :cardId")
    List<CardCategory> findAllByCardId(@Param("cardId") int cardId);

    @Query("SELECT c FROM CardCategory c WHERE c.category.id = :categoryId")
    List<CardCategory> findAllByCategoryId(@Param("categoryId") int categoryId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CardCategory c WHERE c.card.id = :cardId")
    void deleteAllByCardId(@Param("cardId") int cardId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO card_category (card_id, category_id) VALUES (:cardId, :categoryId)", nativeQuery = true)
    void saveCardCategory(@Param("cardId") int cardId, @Param("categoryId") int categoryId);
}
