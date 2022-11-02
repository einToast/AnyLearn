package de.thb.anylearn.entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CardCategoryID implements Serializable {

    private Card card;

    private Category category;
}