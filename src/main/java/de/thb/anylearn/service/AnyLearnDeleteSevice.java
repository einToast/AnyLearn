package de.thb.anylearn.service;

import de.thb.anylearn.entity.Card;
import de.thb.anylearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AnyLearnDeleteSevice {
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

    public void deleteUser(int id) {
        for(Card card : cardRepository.findAllByOwnerId(id)) {
            card.setOwner(null);
            cardRepository.save(card);
        }
        cardUserRepository.deleteAll(cardUserRepository.findAllByUserId(id));
        userRepository.deleteById(id);
    }

    public boolean deleteFolder(int id) {
        if (cardRepository.findAllByFolderId(id).size() == 0) {
            folderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteCategory(int id) {
        cardCategoryRepository.deleteAll(cardCategoryRepository.findAllByCategoryId(id));
        categoryRepository.deleteById(id);
    }

    public void deleteCard(int id) {
        cardCategoryRepository.deleteAll(cardCategoryRepository.findAllByCardId(id));
        cardUserRepository.deleteAll(cardUserRepository.findAllByCardId(id));
        cardRepository.deleteById(id);
    }
}
