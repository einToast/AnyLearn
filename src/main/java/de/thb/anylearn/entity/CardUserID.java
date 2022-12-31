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
public class CardUserID implements Serializable {

    @Column(name = "card_id")
    private int cardId;

    @Column(name = "user_id")
    private int userId;
}