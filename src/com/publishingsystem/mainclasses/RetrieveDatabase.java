package com.publishingsystem.mainclasses;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RetrieveDatabase extends Database{
	
	public static ArrayList<Journal> getJournals() {
		try (Connection con = DriverManager.getConnection(CONNECTION)) {
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			String query = "SELECT * FROM JOURNALS";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				ResultSet res = preparedStmt.executeQuery();
				ArrayList<Journal> journals = new ArrayList<Journal>();
				while(res.next()) {
					int issn = res.getInt("ISSN");
					String name = res.getString("name");
					Date date = res.getDate("dateOfPublication");
					journals.add(new Journal(issn, name, date));
				}
				return journals;
					
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Academic> getRoles(String email){
		try (Connection con = DriverManager.getConnection(CONNECTION)) {
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			ArrayList<Academic> roles = new ArrayList<Academic>();
			
			String query = "SELECT academicID, title, forename, surname, university, emailAddress FROM ACADEMICS WHERE emailAddress = ?";
			int academicId = -1;
			String title = "", forename = "", surname = "", emailId = "", university = "";
			
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setString(1, email);
				ResultSet res = preparedStmt.executeQuery();
				if(res.next()) {
					academicId = res.getInt("academicID");
					title = res.getString("title");
					forename = res.getString("forename");
					surname = res.getString("surname");
					emailId = res.getString("emailAddress");
					university = res.getString("university");
				}else {
					return null;
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			query = "SELECT E.EDITORID, CHIEFEDITOR, J.ISSN, NAME, DATEOFPUBLICATION "
					+ "FROM EDITORS E, EDITOROFJOURNAL EJ, JOURNALS J "
					+ "WHERE J.ISSN = EJ.ISSN AND E.EDITORID = EJ.EDITORID AND E.ACADEMICID = ?";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, academicId);
				ResultSet res = preparedStmt.executeQuery();
				Editor editor = null;
				while(res.next()) {
					if(editor == null)
						editor = new Editor(res.getInt("editorID"), title, forename, surname, emailId, university, null);
					Journal journal = new Journal(res.getInt("ISSN"), res.getString("NAME"), res.getDate("dateOfPublication"));
					boolean chiefEditor = res.getBoolean("chiefEditor");
					EditorOfJournal editorOfJournal = new EditorOfJournal(journal, editor, chiefEditor);
					journal.addEditorToBoard(editorOfJournal);
					editor.addEditorOfJournal(editorOfJournal);
				}
				roles.add(editor);
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			query = "SELECT Aut.AUTHORID, MAINAUTHOR, Art.articleID, J.ISSN, name,"
					+ " dateOfPublication, title, summary, submissionID, status "
					+ "FROM AUTHORS Aut, AUTHOROFARTICLE Aoa, ARTICLES Art, Submissions S, JOURNALS J "
					+ "WHERE Art.ARTICLEID = Aoa.ARTICLEID "
					+ "AND Aut.AUTHORID = Aoa.AUTHORID "
					+ "AND S.articleID = Art.articleID "
					+ "AND J.ISSN = Art.ISSN "
					+ "AND academicId = ?";
			
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, academicId);
				ResultSet res = preparedStmt.executeQuery();
				Author author = null;
				while(res.next()) {
					if(author == null)
						author = new Author(res.getInt("authorID"), title, forename, surname, emailId, university, null);
					Journal journal = new Journal(res.getInt("ISSN"), res.getString("NAME"), res.getDate("dateOfPublication"));
					Article article = new Article(res.getInt("ARTICLEID"), res.getString("TITLE"), res.getString("SUMMARY"), journal);
					boolean mainAuthor = res.getBoolean("MAINAUTHOR");
					if(mainAuthor)
						article.submit(new Submission(article, res.getInt("SUBMISSIONID")));
					AuthorOfArticle authorOfArticle = new AuthorOfArticle(article, author, mainAuthor);
					article.addAuthorOfArticle(authorOfArticle);
					author.addAuthorOfArticle(authorOfArticle);
				}
				if(author != null) {
					query = "SELECT S.SUBMISSIONID, REVIEWERID, R.SUMMARY, TYPINGERRORS, VERDICT "
							+ "FROM AUTHOROFARTICLE Aoa, ARTICLES Art, SUBMISSIONS S, REVIEWS R "
							+ "WHERE Art.ARTICLEID = Aoa.ARTICLEID "
							+ "AND Aoa.MAINAUTHOR = 1 "
							+ "AND Art.ARTICLEID = S.ARTICLEID "
							+ "AND S.SUBMISSIONID = R.SUBMISSIONID "
							+ "AND authorID = ?";
	
					try(PreparedStatement preparedStmt1 = con.prepareStatement(query)){
						preparedStmt1.setInt(1, author.getAuthorId());
						ResultSet reviewRes = preparedStmt1.executeQuery();
						while(reviewRes.next()) {
							int reviewerID = reviewRes.getInt("REVIEWERID");
							for(AuthorOfArticle aoa : author.getAuthorOfArticles()) {
								Submission s = aoa.getArticle().getSubmission();
								if(s != null) {
									query = "SELECT CRITICISMID, CRITICISM, ANSWER FROM CRITICISMS WHERE SUBMISSIONID = ? AND REVIEWERID = ?";
									try(PreparedStatement preparedStmt2 = con.prepareStatement(query)){
										preparedStmt2.setInt(1, s.getSubmissionId());
										preparedStmt2.setInt(2, reviewerID);
										ResultSet crticismRes = preparedStmt2.executeQuery();
										ArrayList<Criticism> criticisms = new ArrayList<Criticism>();
										while(crticismRes.next()) {
											criticisms.add(new Criticism(crticismRes.getString("CRITICISM"), crticismRes.getString("ANSWER")));
										}
										Review review = new Review(s, reviewRes.getString("SUMMARY"), reviewRes.getString("TYPINGERRORS"), criticisms);
										s.addReview(review);
									}catch (SQLException ex) {
										ex.printStackTrace();
									}
								}
							}
						}
						roles.add(author);
					}catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			query = "SELECT reviewerID FROM REVIEWERS WHERE academicID = ?";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, academicId);
				ResultSet res = preparedStmt.executeQuery();
				if(res.next()) {
					
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			return roles;
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
			
	}
	
	public static void main(String[] args) {
		ArrayList<Academic> roles = RetrieveDatabase.getRoles("j.smith@uniofsmith.ac.uk");
		for(Academic a : roles) {
			if(a instanceof Editor) {
				Editor e = (Editor)a;
				System.out.println(e);
				for(EditorOfJournal eoj : e.getEditorOfJournals()){
					System.out.println(eoj.getEditor().getEditorId() +" "+ eoj.getJournal().getISSN()+" ChiefEditor: "+eoj.isChiefEditor());
				};
			}
		}
		System.out.println();
		ArrayList<Academic> roles1 = RetrieveDatabase.getRoles("j.doe@uniofdoe.ac.uk");
		for(Academic a : roles1) {
			if(a instanceof Author) {
				Author author = (Author)a;
				System.out.println(author);
				for(AuthorOfArticle aoa : author.getAuthorOfArticles()){
					System.out.println(aoa.getAuthor().getAuthorId()+" "+aoa.getArticle().getArticleId()+" MainAuthor: "+aoa.isMainAuthor());
					Article article = aoa.getArticle();
					Submission s = article.getSubmission();
					if(s != null)
						for(Review r : s.getReviews())
							System.out.println(r);
				};
			}
		}
	}
}
