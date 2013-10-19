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

@Entity
@Table(name = "talk")
@NamedQueries({
@NamedQuery(name = Talk.SELECT_ALL, query = "select t from Talk t"),
@NamedQuery(name = Talk.FIND_BY_ROOM, query = "select distinct t from Talk t where room_id=:roomId"),
@NamedQuery(name = Talk.FIND_BY_CONFERENCE, query = "select distinct t from Talk t where conference_id=:conferenceId")

})
public class Talk {
	public static final String SELECT_ALL = "Talk.SELECT_ALL";
	public static final String FIND_BY_ROOM = "Talk.FIND_BY_ROOM";
	public static final String FIND_BY_CONFERENCE = "Talk.FIND_BY_CONFERENCE";

	@Id
	@GeneratedValue
	private Integer id;

	@Basic
	@Size(min=3, max=45)
	private String name;

	@Basic
	@Size(min=3, max=45)
	private String description;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date datetime;

	@Basic
	@NotNull
	@Min(1)
	@Max(8*60)
	private Integer duration;

	@ManyToOne()
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	@NotNull
	private Room room;

	@ManyToOne
	@JoinColumn(name = "conference_id", referencedColumnName = "id")
	@NotNull
	private Conference conference;
	
	

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

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Talk() {
		super();
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	@Override
	public String toString() {
		return "Talk [name=" + name + ", description=" + description
				+ ", datetime=" + datetime + ", duration=" + duration
				+ ", room=" + room + "]";
	}


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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	/**
	 * Returns the end datetime of the talk as a derived attribute from datetime and duration
	 * @return
	 */
	public Date buildEndDateTime() {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(this.getDatetime());
		instance.add(Calendar.MINUTE, this.getDuration());
		return instance.getTime();
	}
	
	/**
	 * Returns the interval of the talk
	 * @return
	 */
	public Interval buildInterval(){
		return new Interval(new Instant(this.getDatetime()),
				new Instant(this.buildEndDateTime()));
	}

}
