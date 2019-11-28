package com.publishingsystem.mainclasses;
import java.util.ArrayList;

public class Reviewer extends Author{
	private ArrayList<ReviewerOfSubmission> reviewerOfSubmissions;
	private int reviewerId;
	
	public Reviewer(int authorId, int reviewerId, String title ,String forename, String surname, String emailId, String university, Hash hash) {
		super(authorId, title, forename, surname, emailId, university, hash);
		this.reviewerOfSubmissions = new ArrayList<ReviewerOfSubmission>();
		this.reviewerId = reviewerId;
	}
	
	//---POSSIBLY DELETE LATER---
	public Reviewer(Author a) {
		super(a.getAuthorId(), a.getTitle(), a.getForename(), a.getSurname(), a.getEmailId(), a.getUniversity(), a.getHash());
		setAcademicId(a.getAcademicId());
		this.reviewerOfSubmissions = new ArrayList<ReviewerOfSubmission>();
	}
	
	public void addSubmissionsToReview(ArrayList<Submission> submissionsToReview) {
		for(Submission s : submissionsToReview) {
			this.reviewerOfSubmissions.add(new ReviewerOfSubmission(this, s));
		}
	}
	
	public void addReviewerOfSubmission(ReviewerOfSubmission ros) {
		this.reviewerOfSubmissions.add(ros);
	}
	
	public ArrayList<ReviewerOfSubmission> getReviewerOfSubmissions() {
		return reviewerOfSubmissions;
	}

	public void setReviewerId(int id) {
		this.reviewerId = id;
	}
	
	public int getReviewerId() {
		return this.reviewerId;
	}
	
	public void setVerdict(Review r, Verdict v) {
		r.setVerdict(v);
	}
}
