package com.prodyna.academy.pac.conference.conference.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.Instant;
import org.joda.time.Interval;

/**
 * Entity Conference
 * 
 * @author jwurm
 */
@Entity
@Table(name = "conference")
@NamedQuery(name = Conference.SELECT_ALL, query = "select c from Conference c")
public class Conference {
	public static final String SELECT_ALL = "conference.selectAll";

	/**
	 * Generated ID
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Name of the conference
	 */
	@Basic
	@NotNull
	@Size(min = 3, max = 45)
	private String name;

	/**
	 * Description of the conference.
	 */
	@Basic
	@NotNull
	@Size(min = 3, max = 200)
	private String description;

	/**
	 * Start date of the conference.
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date start;

	/**
	 * End date of the conference.
	 */
	@Basic
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date end;

	public Conference() {
		super();
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param name
	 * @param description
	 * @param start
	 * @param end
	 */
	public Conference(String name, String description, Date start, Date end) {
		super();
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the interval of the conference. The end instant is 00:00 of the
	 * day after the last day of the conference
	 * 
	 * @return null if prerequisites are not met
	 */
	public Interval buildInterval() {
		if (start == null || end == null) {
			return null;
		}

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(end);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Instant endInstant = new Instant(calendar.getTime());

		return new Interval(new Instant(this.getStart()), endInstant);
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

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getStart() {
		return start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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

	public void setId(Integer id) {
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
