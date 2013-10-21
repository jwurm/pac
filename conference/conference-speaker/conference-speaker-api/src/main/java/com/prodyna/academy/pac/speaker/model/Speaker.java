package com.prodyna.academy.pac.speaker.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// TODO: Auto-generated Javadoc
/**
 * The Class Speaker.
 *
 * @author jwurm The speaker entity
 */
@Entity
@Table(name = "speaker")
@NamedQuery(name = Speaker.SELECT_ALL, query = "select s from Speaker s")
public class Speaker {

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** The Constant SELECT_ALL. */
	public static final String SELECT_ALL = "speakerSelectAll";

	/** Generated id. */
	@Id
	@GeneratedValue
	private Integer id;

	/** The speaker name. */
	@Basic
	@NotNull
	@Size(min = 3, max = 45)
	private String name;

	/** The description. */
	@Basic
	@NotNull
	@Size(min = 3, max = 45)
	private String description;

	/**
	 * Instantiates a new speaker.
	 */
	public Speaker() {
		super();
	}

	/**
	 * Instantiates a new speaker.
	 *
	 * @param name the name
	 * @param description the description
	 */
	public Speaker(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Speaker other = (Speaker) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Speaker [id=" + id + ", name=" + name + ", description="
				+ description + "]";
	}

}
