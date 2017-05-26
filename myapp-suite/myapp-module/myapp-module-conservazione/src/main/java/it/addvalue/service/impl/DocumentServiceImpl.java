package it.addvalue.service.impl;

import it.addvalue.entity.Document;
import it.addvalue.repository.DocumentRepository;
import it.addvalue.service.DocumentService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService
{
    // @Autowired
    private DocumentRepository documentRepository;

    @Override
    public List<Document> getList()
    {
        List<Document> result = new ArrayList<Document>();

        Iterable<Document> list = documentRepository.findAll();

        for ( Document item : list )
        {
            result.add(item);

        }
        return result;
    }

    @Override
    public Document insertDoc(String name)
    {
        return documentRepository.save(new Document(name));

    }

    @Override
    public void deleteDoc(Document item)
    {
        documentRepository.delete(item);
    }

}
