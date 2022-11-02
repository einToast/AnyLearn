package de.thb.anylearn.repository;

import de.thb.anylearn.entity.CardCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

@RepositoryDefinition(domainClass = CardCategory.class, idClass = Integer.class)
public interface CardCategoryRepository extends CrudRepository<CardCategory, Integer> {
    // lol auto generated, maybe we need this later

    @Query("SELECT c FROM CardCategory c WHERE c.card.id = :cardId")
    List<CardCategory> findAllByCardId(@Param("cardId") int cardId);

    @Query("SELECT c FROM CardCategory c WHERE c.category.id = :categoryId")
    List<CardCategory> findAllByCategoryId(@Param("categoryId") int categoryId);

}
