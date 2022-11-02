package de.thb.anylearn.service;

import de.thb.anylearn.entity.Card;
import de.thb.anylearn.entity.Folder;
import de.thb.anylearn.entity.Category;
import de.thb.anylearn.repository.CardRepository;
import de.thb.anylearn.repository.CategoryRepository;
import de.thb.anylearn.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Card> getAllCard(){
        return (List<Card>) cardRepository.findAll();
    }

    public List<Folder> getAllFolder() {
        return (List<Folder>) folderRepository.findAll();
    }

    public List<Category> getAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Card> getFilteredCard(int folderId, int[] cat) {
        if (folderId == 0){
            if (cat == null) {
                return getAllCard();
            } else {
                return null;
                //return cardRepository.findAllByCategoriesId(cat);
            }
        } else {
            if (cat == null) {
                return cardRepository.findAllByFolderId(folderId);
            } else {
                return null;
                //return cardRepository.findAllByFolderCategoryId(folderId, cat);
            }
        }
    }
}
