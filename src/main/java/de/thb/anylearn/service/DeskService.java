package de.thb.anylearn.service;

import de.thb.anylearn.entity.Card;
import de.thb.anylearn.entity.CardCategory;
import de.thb.anylearn.entity.Folder;
import de.thb.anylearn.entity.Category;
import de.thb.anylearn.repository.CardCategoryRepository;
import de.thb.anylearn.repository.CardRepository;
import de.thb.anylearn.repository.CategoryRepository;
import de.thb.anylearn.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeskService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardCategoryRepository cardCategoryRepository;

    public List<Card> getAllCard(){
        return (List<Card>) cardRepository.findAll();
    }

    public List<Folder> getAllFolder() {
        return (List<Folder>) folderRepository.findAll();
    }

    public List<Category> getAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Card> getFilteredCard(int folderId, int[] cats) {
        if (folderId == 0) {
            if (cats == null) {
                return getAllCard();
            } else {
                //return null;
                //return cardRepository.findAllByCategoriesId(cat);
                return getFilteredByCategory(cats).stream().map(CardCategory::getCard).collect(Collectors.toList()); // die "schöne" Lösung (aber nicht die von mir) <-- das auch nicht
                // ändern oder verstehen
            }
        } else {
            if (cats == null) {
                return cardRepository.findAllByFolderId(folderId);
            } else {
                return null;
                //return cardRepository.findAllByFolderCategoryId(folderId, cat);
            }
        }
    }

    public List<CardCategory> getFilteredByCategory(int[] cats) {
        List<CardCategory> cardList = cardCategoryRepository.findAllByCategoryId(cats[0]);

        for( int j = 0; j < cardList.size(); j++) {
            List<CardCategory> allEntriesOfOneCard = cardCategoryRepository.findAllByCardId(cardList.get(j).getCard().getId()); //wtf
            for(int i = 1; i < cats.length; i++) {
                boolean found = false;
                for (CardCategory cardCategory : allEntriesOfOneCard) {
                    if (cardCategory.getCategory().getId() == cats[i]) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    cardList.remove(j);
                    j--;
                    break;
                }
            } // wtf bis hier ungefähr - GitHub Copilot ist echt mächtig
        }
        return cardList;

        //hilfe
    }
}
