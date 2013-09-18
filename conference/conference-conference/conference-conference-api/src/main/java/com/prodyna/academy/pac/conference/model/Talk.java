package com.prodyna.academy.pac.conference.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.prodyna.academy.pac.room.model.Room;

@Entity
@Table(name = "talk")
@NamedQuery(name = Talk.SELECT_ALL, query = "select t from Talk t")
public class Talk {
	public static final String SELECT_ALL = "talk.selectAll";

	@Id
	@GeneratedValue
	private Integer id;

	@Basic
	private String name;

	@Basic
	private String description;

	@ManyToOne()
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	private Room room;

	@ManyToOne
	@JoinColumn(name = "conference_id", referencedColumnName = "id")
	private Conference conference;

	public Talk(String name, String description, int duration,
			Conference conference, Room room) {
		super();
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.conference = conference;
		this.room = room;
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
		return "Talk [id=" + id + ", name=" + name + ", description="
				+ description + ", duration=" + duration + "]";
	}

	@Basic
	private Integer duration;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + duration;
		result = prime * result + id;
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration != other.duration)
			return false;
		if (id != other.id)
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

}
