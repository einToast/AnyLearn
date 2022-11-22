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

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            if (cats[0] == 0)  {
                return getAllCard();
            } else {
                //return null;
                //return cardRepository.findAllByCategoriesId(cat);
                return getFilteredByCategory(cats).stream().map(CardCategory::getCard).collect(Collectors.toList());
                // Durch methode getFilteredByCategory() kommen Card-Category-Paare, mit Karten die in allen cats sind
                // diese werden jetzt "gestream" in eine map, welche für jedes Paar die zugehörige Karte in eine Liste packt
            }
        } else {
            if (cats[0] == 0) {
                return cardRepository.findAllByFolderId(folderId);
            } else {
                //return null;
                //return cardRepository.findAllByFolderCategoryId(folderId, cat);
                List<Card> cards_by_cat = getFilteredByCategory(cats).stream().map(CardCategory::getCard).collect(Collectors.toList());
                for(int i = 0; i < cards_by_cat.size(); i++) {
                    if (folderId != cards_by_cat.get(i).getFolder().getId()) {
                        cards_by_cat.remove(i);
                        i--;
                    }
                }
            return cards_by_cat;
            }
        }
    }

    public List<CardCategory> getFilteredByCategory(int[] cats) {
        List<CardCategory> cardList = cardCategoryRepository.findAllByCategoryId(cats[0]);  // nur Card-Category-Paare, mit Paaren, wo KategorieId = cat[0] ist

        for( int j = 0; j < cardList.size(); j++) {
            // Jetzt suchen wir alle Card-Category-Tupel, bei denen KartenId = (j. KartenId von vorheriger Liste)
            // --> Liste mit Card-Category-Tupeln einer einzigen Karten, bei denen die Karte mind. in Katergorie cat[0] ist
            List<CardCategory> allEntriesOfOneCard = cardCategoryRepository.findAllByCardId(cardList.get(j).getCard().getId());
            // jetzt checken, ob die Karte auch zu den anderen gewünschten Kategorien gehört
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
            }
        }
        return cardList;
    }

    /**
     *
     * @param folderId
     * @param categories
     * @return Id of the nextCard or 0 if no more cards
     */
    public int getNextCardIdToLearn(int folderId, int[] categories) {
        List<Card> cardList = getFilteredCard(folderId, categories);   // gets filtered Cards ORDERED BY nextTime
        Card nextCard;
        if (cardList.size() > 0)
            nextCard = cardList.get(0);                               // gets nextCard
        else
            return 0;

        Date now = new Date();
        now.setTime(now.getTime() + 20 * 60 * 1000);    // add 20 Minutes to current DateTime
        if (nextCard.getNextTime() == null)
            nextCard.setNextTime(now);

        // if now + 20 minutes smaller/equals nextTime of the next Card
        if (now.compareTo(nextCard.getNextTime()) >= 0) {
            return nextCard.getId();
        } else {
            return 0;       //0 means: no Cards to learn for now
        }
    }

    public Card getCardById(int cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        return card.orElse(null);
    }
}
