/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.addvalue.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


/**
 * Domain object for an item managed by the company.
 *
 * @author Greg Turnquist
 * @author Oliver Gierke
 */
@Entity
public class Item {

	private @Id @GeneratedValue Long id;
	private final String description;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	Item() {
		this(null, null);
	}

	public Item(String description, Address address) {
		super();
		this.description = description;
		this.address = address;
	}

	public Item(String description) {
		this(description, null);
	}

	public String getDescription() {
		return description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
