package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Commentators")
public class Commentator {

  @Id
  @Column(name = "CommentatorId")
  private int commentatorId;
	
  @Column(name = "CommentatorName")
  private String commentatorName;

  @Column(name = "UseThis")
  private String useThis;

public int getCommentatorId() {
	return commentatorId;
}

public void setCommentatorId(int commentatorId) {
	this.commentatorId = commentatorId;
}

public String getCommentatorName() {
	return commentatorName;
}

public void setCommentatorName(String commentatorName) {
	this.commentatorName = commentatorName;
}

public String getUseThis() {
	return useThis;
}

public void setUseThis(String useThis) {
	this.useThis = useThis;
}
  
}