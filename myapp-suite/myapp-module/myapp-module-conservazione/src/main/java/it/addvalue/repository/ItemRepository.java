/*
 * Copyright 2014 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package it.addvalue.repository;

import it.addvalue.entity.Item;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This repository shows interface and method-level security. The entire repository requires ROLE_USER, while certain operations require ROLE_ADMIN.
 *
 * @author Greg Turnquist
 * @author Oliver Gierke
 */
// @PreAuthorize("isAuthenticated()")

@RepositoryRestResource(path = "item", itemResourceRel = "item")
public interface ItemRepository extends PagingAndSortingRepository<Item, Long>
{

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#save(S)
     */
    @Override
    <S extends Item> S save(S s);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
     */
    @Override
    void delete(Long aLong);

    List<Item> findByDescription(@Param(value = "description") String description);
}
