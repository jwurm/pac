package com.prodyna.academy.pac.conference.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "conference")
@NamedQuery(name=Conference.SELECT_ALL, query="select c from Conference c")
public class Conference {
	public static final String SELECT_ALL="conference.selectAll";
	
	@Id
	@GeneratedValue
	private int id;

	@Basic
	private String name;

	@Basic
	private String description;
	
	@Transient
//	@Basic
	private Date start;
	
	@Transient
//	@Basic
	private Date end;
	
//@Transient
	@OneToMany(fetch = FetchType.EAGER, mappedBy="conference")
	private List<Talk> talks;

	public Conference(String name, String description, Date start, Date end) {
		super();
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
	}
	
	public Conference() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conference other = (Conference) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	public String getDescription() {
		return description;
	}

	public Date getEnd() {
		return end;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getStart() {
		return start;
	}

	public List<Talk> getTalks() {
		return talks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "Conference [id=" + id + ", name=" + name + ", description="
				+ description + ", start=" + start + ", end=" + end + "]";
	}

}
