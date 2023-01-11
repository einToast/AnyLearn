package de.thb.anylearn.service;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.controller.form.EntityFormModel;
import de.thb.anylearn.entity.*;
import de.thb.anylearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AnyLearnCreateSevice {
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

    public void addCard(CardFormModel cardFormModel) {
        Card card = new Card();
        Folder folder = folderRepository.findById(cardFormModel.getFolderId()).get();
        User owner = userRepository.findById(cardFormModel.getOwnerId()).orElse(null);

        card.setQuestion(cardFormModel.getQuestion());
        card.setAnswer(cardFormModel.getAnswer());
        card.setFolder(folder);
        card.setOwner(owner);

        cardRepository.save(card);

        if (cardFormModel.getCategoryId() != null) {
            for (int catId : cardFormModel.getCategoryId()) {
                Category category = categoryRepository.findById(catId).get();
                CardCategory cardCategory = new CardCategory(card, category);
                cardCategoryRepository.save(cardCategory);
            }
        }

        // add to card_user_table
        if (cardFormModel.getUserId() != null) {
            System.out.println("Bin drin");
            for (int userId : cardFormModel.getUserId()) {
                User user = userRepository.findById(userId).get();
                CardUser cardUser = new CardUser(card, user);
                cardUserRepository.save(cardUser);
            }
        }
    }

    public void addUser(EntityFormModel form) {
        User user = new User();
        user.setName(form.getName());
        userRepository.save(user);
    }

    public void addFolder(EntityFormModel form) {
        Folder folder = new Folder();
        folder.setName(form.getName());
        folderRepository.save(folder);
    }

    public void addCategory(EntityFormModel form) {
        Category category = new Category();
        category.setName(form.getName());
        categoryRepository.save(category);
    }
}
