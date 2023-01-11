package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class CardUser {

    public CardUser(Card card, User user) {
        this.card = card;
        this.user = user;
        this.id = new CardUserID(card.getId(), user.getId());
    }


    @EmbeddedId
    CardUserID id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextTime;

    private int difficulty;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;


}
