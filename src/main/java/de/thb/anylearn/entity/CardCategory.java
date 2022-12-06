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
@IdClass(CardCategoryID.class)
public class CardCategory {

    @Id
    @ManyToOne
    private Card card;

    @Id
    @ManyToOne
    private Category category;
}
