package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<CardUser> cardUsers;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER)
    private List<CardCategory> cardCategories;
//    @ManyToMany
//        @JoinTable(name = "card_category", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name ="course_id"))
//    Set<Category> categories;
}
