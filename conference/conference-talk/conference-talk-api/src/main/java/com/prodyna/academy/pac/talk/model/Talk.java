package com.prodyna.academy.pac.talk.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.Instant;
import org.joda.time.Interval;

import com.prodyna.academy.pac.conference.model.Conference;
import com.prodyna.academy.pac.room.model.Room;

// TODO: Auto-generated Javadoc
/**
 * The Class Talk.
 *
 * @author jwurm
 * Talk entity
 */
@Entity
@Table(name = "talk")
@NamedQueries({
@NamedQuery(name = Talk.SELECT_ALL, query = "select t from Talk t"),
@NamedQuery(name = Talk.FIND_BY_ROOM, query = "select distinct t from Talk t where room_id=:roomId"),
@NamedQuery(name = Talk.FIND_BY_CONFERENCE, query = "select distinct t from Talk t where conference_id=:conferenceId")

})
public class Talk {
	
	/** The Constant SELECT_ALL. */
	public static final String SELECT_ALL = "Talk.SELECT_ALL";
	
	/** The Constant FIND_BY_ROOM. */
	public static final String FIND_BY_ROOM = "Talk.FIND_BY_ROOM";
	
	/** The Constant FIND_BY_CONFERENCE. */
	public static final String FIND_BY_CONFERENCE = "Talk.FIND_BY_CONFERENCE";

	/** Auto generated ID. */
	@Id
	@GeneratedValue
	private Integer id;

	/** The name. */
	@Basic
	@Size(min=3, max=45)
	private String name;

	/** The description. */
	@Basic
	@Size(min=3, max=45)
	private String description;
	
	/** The datetime. */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date datetime;

	/** The duration of the talk. */
	@Basic
	@NotNull
	@Min(1)
	@Max(8*60)
	private Integer duration;

	/** The room. */
	@ManyToOne()
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	@NotNull
	private Room room;

	/** The conference. */
	@ManyToOne
	@JoinColumn(name = "conference_id", referencedColumnName = "id")
	@NotNull
	private Conference conference;
	
	

	/**
	 * Instantiates a new talk.
	 *
	 * @param name the name
	 * @param description the description
	 * @param datetime the datetime
	 * @param duration the duration
	 * @param conference the conference
	 * @param room the room
	 */
	public Talk(String name, String description,  Date datetime,int duration,
			Conference conference, Room room) {
		super();
		this.name = name;
		this.datetime = datetime;
		this.description = description;
		this.duration = duration;
		this.conference = conference;
		this.room = room;
	}

	/**
	 * Gets the datetime.
	 *
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}

	/**
	 * Sets the datetime.
	 *
	 * @param datetime the new datetime
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	/**
	 * Instantiates a new talk.
	 */
	public Talk() {
		super();
	}

	/**
	 * Gets the room.
	 *
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Sets the room.
	 *
	 * @param room the new room
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * Gets the conference.
	 *
	 * @return the conference
	 */
	public Conference getConference() {
		return conference;
	}

	/**
	 * Sets the conference.
	 *
	 * @param conference the new conference
	 */
	public void setConference(Conference conference) {
		this.conference = conference;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Talk [name=" + name + ", description=" + description
				+ ", datetime=" + datetime + ", duration=" + duration
				+ ", room=" + room + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datetime == null) ? 0 : datetime.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
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
		Talk other = (Talk) obj;
		if (datetime == null) {
			if (other.datetime != null)
				return false;
		} else if (!datetime.equals(other.datetime))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
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
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

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

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	/**
	 * Returns the end datetime of the talk as a derived attribute from datetime and duration.
	 *
	 * @return the date
	 */
	public Date buildEndDateTime() {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(this.getDatetime());
		instance.add(Calendar.MINUTE, this.getDuration());
		return instance.getTime();
	}
	
	/**
	 * Returns the interval of the talk.
	 *
	 * @return the interval
	 */
	public Interval buildInterval(){
		return new Interval(new Instant(this.getDatetime()),
				new Instant(this.buildEndDateTime()));
	}

}
