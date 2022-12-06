package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class CardCategory{

    public CardCategory(Card card, Category category) {
        this.card = card;
        this.category = category;
        this.id = new CardCategoryID(card.getId(), category.getId());
    }

    @EmbeddedId
    CardCategoryID id;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;


}
