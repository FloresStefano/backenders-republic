package it.addvalue.service.impl;

import it.addvalue.entity.Item;
import it.addvalue.repository.ItemRepository;
import it.addvalue.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService
{

	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public List<Item> getItemList()
	{
		List<Item> result = new ArrayList<Item>();

		Iterable<Item> findAll = itemRepository.findAll();
		for (Item item : findAll) {
			result.add(item);
		}
		return result;
	}

}
