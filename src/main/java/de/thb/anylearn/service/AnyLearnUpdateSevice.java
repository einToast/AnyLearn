package de.thb.anylearn.service;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.controller.form.EntityFormModel;
import de.thb.anylearn.entity.*;
import de.thb.anylearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service

public class AnyLearnUpdateSevice {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardCategoryRepository cardCategoryRepository;

    @Autowired
    private CardUserRepository cardUserRepository;

    @Autowired
    private UserRepository userRepository;

    public void updateUser(EntityFormModel form) {
        User user = userRepository.findById(form.getId()).get();
        user.setName(form.getName());
        userRepository.save(user);
    }

    public void updateFolder(EntityFormModel form) {
        Folder folder = folderRepository.findById(form.getId()).get();
        folder.setName(form.getName());
        folderRepository.save(folder);
    }

    public void updateCategory(EntityFormModel form) {
        Category category = categoryRepository.findById(form.getId()).get();
        category.setName(form.getName());
        categoryRepository.save(category);
    }

    public void updateCard(CardFormModel cardFormModel) {

        // Card erstellen und cardRepository.save()
        Card card = cardRepository.findById(cardFormModel.getId()).get();
        Folder folder = folderRepository.findById(cardFormModel.getFolderId()).get();
        User owner = userRepository.findById(cardFormModel.getOwnerId()).orElse(null);
        List<CardCategory> cardCategories = cardCategoryRepository.findAllByCardId(cardFormModel.getId());
        List<CardUser> cardUsers = cardUserRepository.findAllByCardId(cardFormModel.getId());

        card.setQuestion(cardFormModel.getQuestion());
        card.setAnswer(cardFormModel.getAnswer());
        card.setFolder(folder);
        card.setOwner(owner);

        cardRepository.save(card);

        cardCategoryRepository.deleteAll(cardCategories);

        if (cardFormModel.getCategoryId() != null) {
            for (int catId : cardFormModel.getCategoryId()) {
                Category category = categoryRepository.findById(catId).get();
                CardCategory cardCategory = new CardCategory(card, category);
                cardCategoryRepository.save(cardCategory);
            }
        }

        // Update card_user_table
        cardUserRepository.deleteAll(cardUsers);

        if (cardFormModel.getUserId() != null) {
            for (int userId : cardFormModel.getUserId()) {
                User user = userRepository.findById(userId).get();
                CardUser cardUser = new CardUser(card, user);
                cardUserRepository.save(cardUser);
            }
        }

    }

    public void updateCardTime(int cardId, int answer_difficulty, int userId) {
        Calendar cal = Calendar.getInstance();
        Optional<Card> optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            CardUser cardUser = optionalCard.get().cardUserByUserId(userId);
            int currDifficulty = cardUser.getDifficulty();
            if (currDifficulty == 0) {
                //enhanched Switch
                switch (answer_difficulty) {
                    case 0 -> {
                        cal.add(Calendar.MINUTE, 1);        // 1 min
                        cardUser.setDifficulty(0);
                    }
                    case 1 -> {
                        cal.add(Calendar.MINUTE, 10);    // 10 min + Difficulty hoch
                        cardUser.setDifficulty(1);
                    }
                    case 2 -> {                                   // Nächster Tag + Difficulty Hoch
                        cal.add(Calendar.DATE, 1);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 1);
                        cal.set(Calendar.MILLISECOND, 0);

                        cardUser.setDifficulty(1);
                    }
                }
            } else {
                if (answer_difficulty == 0) {
                    cal.add(Calendar.MINUTE, 1);        // 1 min + difficulty auf 0
                    cardUser.setDifficulty(0);
                } else {                                        // entsprechend später
                    cal.add(Calendar.DATE, currDifficulty * answer_difficulty);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    cardUser.setDifficulty(currDifficulty * answer_difficulty);
                }
            }
            cardUser.setNextTime(cal.getTime());

            cardUserRepository.save(cardUser);
        }
    }
}
