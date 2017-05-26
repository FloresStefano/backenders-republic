package it.addvalue.endpoint;

import it.addvalue.entity.Document;
import it.addvalue.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

@WebService(endpointInterface = "it.addvalue.endpoint.DocumentEndpoint")
public class DocumentSoapImpl implements DocumentEndpoint
{
    @Autowired
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
