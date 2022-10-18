package de.thb.anylearn.service;

import de.thb.anylearn.entity.Card;
import de.thb.anylearn.repository.CardRepository;
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

    public List<Card> getAllCard(){
        return (List<Card>) cardRepository.findAll();}
    }
