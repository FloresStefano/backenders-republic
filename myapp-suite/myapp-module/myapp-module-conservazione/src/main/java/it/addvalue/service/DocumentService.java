package it.addvalue.service;



import it.addvalue.entity.Document;

import java.util.List;

public interface DocumentService
{
   
    List<Document> getList();

    
    Document insertDoc(String name);

   
    void deleteDoc(Document item);
}
