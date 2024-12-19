package com.devrezaur.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "results")
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String username;
	private int totalCorrect = 0;
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	public Result() {
		super();
	}

	public Result(int id, User user, int totalCorrect) {
        super();
        this.id = id;
        this.user = user;
        this.totalCorrect = totalCorrect;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTotalCorrect() {
		return totalCorrect;
	}

	public void setTotalCorrect(int totalCorrect) {
		this.totalCorrect = totalCorrect;
	}

}
