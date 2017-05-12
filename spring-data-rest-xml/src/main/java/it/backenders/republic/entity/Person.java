package it.backenders.republic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="PERSON")
public class Person implements Serializable
{

		private static final long serialVersionUID = -5378396373373165919L;
		
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long pId;
		
		@Column
		private String personName;
		
		@Column
		private double personAge;

		public Long getpId()
		{
				return pId;
		}

		public void setpId(Long pId)
		{
				this.pId = pId;
		}

		public String getPersonName()
		{
				return personName;
		}

		public void setPersonName(String personName)
		{
				this.personName = personName;
		}

		public double getPersonAge()
		{
				return personAge;
		}

		public void setPersonAge(double personAge)
		{
				this.personAge = personAge;
		}

		@Override
		public String toString()
		{
				return "Person [pId=" + pId + ", personName=" + personName + ", personAge=" + personAge + "]";
		}
}
