package de.thb.anylearn.service;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.entity.*;
import de.thb.anylearn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private UserRepository userRepository;

    public List<Card> getAllCard(){
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
        List<Card> cards;
        if (folderId == 0) {
            if (cats[0] == 0)  {
                cards = getAllCard();
            } else {
                //return null;
                //return cardRepository.findAllByCategoriesId(cat);
                cards = new ArrayList<>(getFilteredByCategory(cats).stream().map(CardCategory::getCard).toList());
                // Durch methode getFilteredByCategory() kommen Card-Category-Paare, mit Karten die in allen cats sind
                // diese werden jetzt "gestream" in eine map, welche für jedes Paar die zugehörige Karte in eine Liste packt
            }
        } else {
            if (cats[0] == 0) {
                cards = cardRepository.findAllByFolderId(folderId);
            } else {
                //return null;
                //return cardRepository.findAllByFolderCategoryId(folderId, cat);
                cards = getFilteredByCategory(cats).stream().map(CardCategory::getCard).collect(Collectors.toList());
                for(int i = 0; i < cards.size(); i++) {
                    if (folderId != cards.get(i).getFolder().getId()) {
                        cards.remove(i);
                        i--;
                    }
                }

            }
        }
        if (userId != 0)
            cards.removeIf(card -> (card.getOwner() != null && card.getOwner().getId() != userId));
        return cards;
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
     * @param folderId id of the folder
     * @param categories list of categories
     * @return Id of the nextCard or 0 if no more cards
     */
    public int getNextCardIdToLearn(int folderId, int[] categories, int userId) {
        Date now = new Date();
        List<Card> cardList = getFilteredCard(folderId, categories, userId);
        for(Card c : cardList) {
            if(c.getNextTime() == null) {
                c.setNextTime(now);
            }
        }
        cardList.sort(Comparator.comparing(Card::getNextTime));
        Card nextCard;
        if (cardList.size() > 0)
            nextCard = cardList.get(0);                               // gets nextCard
        else
            return 0;


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


    public void rescheduleCard(int cardId, int answer_difficulty) {
        Calendar cal = Calendar.getInstance();
        Optional<Card> optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            int currDifficulty = card.getDifficulty();
            System.out.println(currDifficulty);
            System.out.println(answer_difficulty);
            if (currDifficulty == 0) {
                System.out.println("Vor Switch");
                //enhanched Switch
                switch (answer_difficulty) {
                    case 0 -> {
                        cal.add(Calendar.MINUTE, 1);        // 1 min
                        System.out.println("Set Diff");
                        card.setDifficulty(0);
                    }
                    case 1 -> {
                        cal.add(Calendar.MINUTE, 10);    // 10 min + Difficulty hoch
                        card.setDifficulty(1);
                    }
                    case 2 -> {                                   // Nächster Tag + Difficulty Hoch
                        cal.add(Calendar.DATE, 1);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        card.setDifficulty(1);
                    }
                }
            } else {
                System.out.println("in else");
                if (answer_difficulty == 0) {
                    cal.add(Calendar.MINUTE, 1);        // 1 min + difficulty auf 0
                    card.setDifficulty(0);
                } else {                                        // entsprechend später
                    cal.add(Calendar.DATE, currDifficulty * answer_difficulty);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    card.setDifficulty(currDifficulty * answer_difficulty);
                }
            }
            card.setNextTime(cal.getTime());

            cardRepository.save(card);
        }
    }

    public int[] getAllCategoryIdFromCard(int cardId) {
        return getAllCategoriesFromCard(cardId).stream().mapToInt(Category::getId).toArray();
    }
    public List<Category> getAllCategoriesFromCard(int cardId){
        return cardCategoryRepository.findAllByCardId(cardId).stream().map(CardCategory::getCategory).collect(Collectors.toList());
    }

    //überarbeiten, könnte eleganter gelöst werden
    public void updateCard(CardFormModel cardFormModel) {

        // Card erstellen und cardRepository.save()
        Card card = cardRepository.findById(cardFormModel.getId()).get();
        Folder folder = folderRepository.findById(cardFormModel.getFolderId()).get();
        User owner = userRepository.findById(cardFormModel.getOwnerId()).orElse(null);
        List<CardCategory> cardCategories = cardCategoryRepository.findAllByCardId(cardFormModel.getId());

        card.setQuestion(cardFormModel.getQuestion());
        card.setAnswer(cardFormModel.getAnswer());
        card.setFolder(folder);
        card.setOwner(owner);

        cardRepository.save(card);

        cardCategoryRepository.deleteAll(cardCategories);

        if(cardFormModel.getCategoryId() != null){
            for (int catId : cardFormModel.getCategoryId()) {
                Category category = categoryRepository.findById(catId).get();
                CardCategory cardCategory = new CardCategory(card, category);
                cardCategoryRepository.save(cardCategory);
            }
        }
    }
}
