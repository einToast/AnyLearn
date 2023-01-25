package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Embeddable
public class CardCategoryID implements Serializable {

    @Column(name = "card_id")
    private int cardId;

    @Column(name = "category_id")
    private int categoryId;
}