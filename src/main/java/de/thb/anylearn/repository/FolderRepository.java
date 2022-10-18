package de.thb.anylearn.repository;

import de.thb.anylearn.entity.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Folder.class, idClass = Long.class)
public interface FolderRepository extends CrudRepository<Folder, Long> {

}
