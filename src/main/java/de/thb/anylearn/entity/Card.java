package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;

    private String answer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextTime;

    private int difficulty;

    @ManyToOne
    private Folder folder;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    private List<CardCategory> cardCategories;
}
