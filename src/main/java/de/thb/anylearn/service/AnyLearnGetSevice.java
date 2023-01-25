package de.thb.anylearn.service;

import de.thb.anylearn.entity.*;
import de.thb.anylearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class AnyLearnGetSevice {
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

    public List<Card> getAllCard() {
        return (List<Card>) cardRepository.findAll();
    }

    public List<Folder> getAllFolder() {
        return (List<Folder>) folderRepository.findAll();
    }

    public List<Category> getAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    public List<Card> getFilteredCard(int folderId, int[] cats, int userId) {
        return getFilteredCard(folderId, cats, userId, false);
    }

    public List<Card> getFilteredCard(int folderId, int[] cats, int userId, boolean onlyLearning) {
        List<Card> cards;
        if (folderId == 0) {
            if (cats[0] == 0) {
                cards = getAllCard();
            } else {
                cards = new ArrayList<>(getFilteredByCategory(cats).stream().map(CardCategory::getCard).toList());
                // Durch methode getFilteredByCategory() kommen Card-Category-Paare, mit Karten die in allen cats sind
                // diese werden jetzt "gestream" in eine map, welche für jedes Paar die zugehörige Karte in eine Liste packt
            }
        } else {
            if (cats[0] == 0) {
                cards = cardRepository.findAllByFolderId(folderId);
            } else {
                cards = getFilteredByCategory(cats).stream().map(CardCategory::getCard).collect(Collectors.toList());
                for (int i = 0; i < cards.size(); i++) {
                    if (folderId != cards.get(i).getFolder().getId()) {
                        cards.remove(i);
                        i--;
                    }
                }

            }
        }
        if (onlyLearning || userId != 0) {
            Iterator<Card> iterCards = cards.iterator();
            while (iterCards.hasNext()) {
                Card card = iterCards.next();
                if (!onlyLearning && card.getOwner() != null && card.getOwner().getId() == userId)
                    continue;
                if (card.getCardUsers().isEmpty())
                    iterCards.remove();
                else {
                    boolean isShared = false;
                    for (CardUser cardUser : card.getCardUsers()) {
                        if (cardUser.getUser().getId() == userId) {
                            isShared = true;
                            break;
                        }
                    }
                    if (!isShared)
                        iterCards.remove();
                }
            }
        }
        return cards;
    }

    public List<CardCategory> getFilteredByCategory(int[] cats) {
        List<CardCategory> cardList = cardCategoryRepository.findAllByCategoryId(cats[0]);  // nur Card-Category-Paare, mit Paaren, wo KategorieId = cat[0] ist

        for (int j = 0; j < cardList.size(); j++) {
            // Jetzt suchen wir alle Card-Category-Tupel, bei denen KartenId = (j. KartenId von vorheriger Liste)
            // --> Liste mit Card-Category-Tupeln einer einzigen Karten, bei denen die Karte mind. in Katergorie cat[0] ist
            List<CardCategory> allEntriesOfOneCard = cardCategoryRepository.findAllByCardId(cardList.get(j).getCard().getId());
            // jetzt checken, ob die Karte auch zu den anderen gewünschten Kategorien gehört
            for (int i = 1; i < cats.length; i++) {
                boolean found = false;
                for (CardCategory cardCategory : allEntriesOfOneCard) {
                    if (cardCategory.getCategory().getId() == cats[i]) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cardList.remove(j);
                    j--;
                    break;
                }
            }
        }
        return cardList;
    }

    /**
     * @param folderId   id of the folder
     * @param categories list of categories
     * @return Id of the nextCard or 0 if no more cards
     */
    public int getNextCardIdToLearn(int folderId, int[] categories, int userId) {
        Date now = new Date();
        List<Card> cardList = getFilteredCard(folderId, categories, userId, true);

        if (cardList.size() == 0)
            return 0;

        List<CardUser> cardUserList = new ArrayList<>(Collections.singletonList(cardList.get(0).cardUserByUserId(userId)));
        for(Card card : cardList) {
            if(!cardUserList.contains(card.cardUserByUserId(userId))) {
                cardUserList.add(card.cardUserByUserId(userId));

            }
        }
        for (CardUser cardUser : cardUserList) {
            if(cardUser.getNextTime() == null) {
                cardUser.setNextTime(now);
            }
        }

        cardUserList.sort(Comparator.comparing(CardUser::getNextTime));
        CardUser nextCardUser = cardUserList.get(0);

        now.setTime(now.getTime() + 20 * 60 * 1000);    // add 20 Minutes to current DateTime

        // if now + 20 minutes smaller/equals nextTime of the next Card
        if (now.compareTo(nextCardUser.getNextTime()) >= 0) {
            return nextCardUser.getCard().getId();
        } else {
            return 0;       //0 means: no Cards to learn for now
        }
    }

    public Card getCardById(int cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        return card.orElse(null);
    }

    public int[] getAllCategoryIdFromCard(int cardId) {
        return getAllCategoriesFromCard(cardId).stream().mapToInt(Category::getId).toArray();
    }

    public List<Category> getAllCategoriesFromCard(int cardId) {
        return cardCategoryRepository.findAllByCardId(cardId).stream().map(CardCategory::getCategory).collect(Collectors.toList());
    }

    public int[] getAllUserIdFromCard(int cardId) {
        return getAllUsersFromCard(cardId).stream().mapToInt(User::getId).toArray();
    }

    public List<User> getAllUsersFromCard(int cardId) {
        return cardUserRepository.findAllByCardId(cardId).stream().map(CardUser::getUser).collect(Collectors.toList());
    }

    public User getUserById(int id) {
        //findById gibt nur Optional<> zurück, deshalb orElse(null)
        return userRepository.findById(id).orElse(null);
    }

    public Folder getFolderById(int id) {
        return folderRepository.findById(id).orElse(null);
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
