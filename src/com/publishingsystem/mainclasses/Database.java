package com.publishingsystem.mainclasses;
import java.sql.*;
import java.util.*;
import java.sql.Date;

public class Database {
	protected static final String CONNECTION = "jdbc:mysql://stusql.dcs.shef.ac.uk/?user=team022&password=6b78cf2f";
	protected static final String DATABASE = "team022";

	//localhost
//	protected static final String CONNECTION = "jdbc:mysql://localhost:3306/publishing_system?user=root&password=password";
//	protected static final String DATABASE = "publishing_system";

	public static String getConnectionName() {
		return CONNECTION;
	}

	public static String getDatabaseName() {
		return DATABASE;
	}

	public static void registerEditors(ArrayList<Editor> editors) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			for(Editor e : editors) {
				boolean academicExists = false;
				String query = "SELECT 1 FROM ACADEMICS WHERE emailAddress = ?";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, e.getEmailId());
					ResultSet res = preparedStmt.executeQuery();
					if (res.next())
						academicExists = true;
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
				if(academicExists)
					continue;
				query = "INSERT INTO ACADEMICS values (null, ?, ?, ?, ?, ?, ?, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, e.getTitle());
					preparedStmt.setString(2, e.getForename());
					preparedStmt.setString(3, e.getSurname());
					preparedStmt.setString(4, e.getUniversity());
					preparedStmt.setString(5, e.getEmailId());
					preparedStmt.setString(6, e.getHash().getHash());
					preparedStmt.setString(7, e.getHash().getSalt());

					preparedStmt.execute();

					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from ACADEMICS");
					while(rs.next())
						e.setAcademicId(Integer.valueOf(rs.getString("last_id")));
				}

				//Add editor to editor table
				query = "INSERT INTO EDITORS values (null, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, e.getAcademicId());
					preparedStmt.execute();

					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from EDITORS");
					while(rs.next())
						e.setEditorId(Integer.valueOf(rs.getString("last_id")));
				}catch (SQLException ex) {
					ex.printStackTrace();
				}

			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void registerAuthors(ArrayList<Author> authors) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			for(Author a : authors) {
				boolean academicExists = false;
				String query = "SELECT 1 FROM ACADEMICS WHERE emailAddress = ?";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, a.getEmailId());
					ResultSet res = preparedStmt.executeQuery();
					if (res.next())
						academicExists = true;
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
				if(academicExists)
					continue;
				
				query = "INSERT INTO ACADEMICS values (null, ?, ?, ?, ?, ?, ?, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, a.getTitle());
					preparedStmt.setString(2, a.getForename());
					preparedStmt.setString(3, a.getSurname());
					preparedStmt.setString(4, a.getUniversity());
					preparedStmt.setString(5, a.getEmailId());
					preparedStmt.setString(6, a.getHash().getHash());
					preparedStmt.setString(7, a.getHash().getSalt());

					preparedStmt.execute();
					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from ACADEMICS");
					while(rs.next())
						a.setAcademicId(Integer.valueOf(rs.getString("last_id")));

				}catch (SQLException ex) {
					ex.printStackTrace();
				}

				query = "INSERT INTO AUTHORS values (null, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, a.getAcademicId());
					preparedStmt.execute();

					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from AUTHORS");
					while(rs.next())
						a.setAuthorId((Integer.valueOf(rs.getString("last_id"))));
				}catch (SQLException ex) {
					ex.printStackTrace();
				}

			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addJournal(Journal j) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			//Add journal to journal table
			String query = "INSERT INTO JOURNALS values (?, ?, ?)";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, j.getISSN());
				preparedStmt.setString(2, j.getJournalName());
				preparedStmt.setDate(3, j.getDateOfPublication());
				preparedStmt.execute();
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			for(EditorOfJournal e : j.getBoardOfEditors()) {
				query = "INSERT INTO EDITOROFJOURNAL values (?, ?, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, e.getEditor().getEditorId());
					preparedStmt.setInt(2, j.getISSN());
					if(e.isChiefEditor())
						preparedStmt.setBoolean(3, true);
					else
						preparedStmt.setBoolean(3, false);
					preparedStmt.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}

			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addSubmission(Article article) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			//Add submission to article table
			String query = "INSERT INTO ARTICLES values (null, ?, ?, ?)";

			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, article.getJournal().getISSN());
				preparedStmt.setString(2, article.getTitle());
				preparedStmt.setString(3, article.getSummary());
				preparedStmt.execute();

				ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from ARTICLES");
				while(rs.next())
					article.setArticleId(Integer.valueOf(rs.getString("last_id")));
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			query = "INSERT INTO SUBMISSIONS values (null, ?, ?)";

			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, article.getArticleId());
				preparedStmt.setString(2, SubmissionStatus.SUBMITTED.asString());
				preparedStmt.execute();

				ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from SUBMISSIONS");
				while(rs.next())
					article.getSubmission().setSubmissionId(Integer.valueOf(rs.getString("last_id")));
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			for(AuthorOfArticle a : article.getAuthorsOfArticle()) {
				query = "INSERT INTO AUTHOROFARTICLE values (?, ?, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, a.getAuthor().getAuthorId());
					preparedStmt.setInt(2, article.getArticleId());
					if(a.isMainAuthor())
						preparedStmt.setBoolean(3, true);
					else
						preparedStmt.setBoolean(3, false);
					preparedStmt.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

			ArrayList<PDF> pdfs = article.getVersions();
			PDF pdf = pdfs.get(pdfs.size()-1);
			query = "INSERT INTO PDF values (null, ?, ?, ?)";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, article.getSubmission().getSubmissionId());
				preparedStmt.setString(2, pdf.getPdfLink());
				preparedStmt.setDate(3, pdf.getDate());

				preparedStmt.execute();
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addReviewers(ArrayList<Reviewer> reviewers) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			for(Reviewer r : reviewers) {
				String query = "INSERT INTO REVIEWERS values (null, ?)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, r.getAcademicId());
					preparedStmt.execute();

					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from REVIEWERS");
					while(rs.next())
						r.setReviewerId(Integer.valueOf(rs.getString("last_id")));
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addReview(Reviewer reviewer, Review review) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			Submission submission = review.getSubmission();
			
			String query = "INSERT INTO REVIEWS values (?, ?, ?, ?, null)";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, reviewer.getReviewerId());
				preparedStmt.setInt(2, submission.getSubmissionId());
				preparedStmt.setString(3, review.getSummary());
				preparedStmt.setString(4, review.getTypingErrors());

				preparedStmt.execute();
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			for(Criticism c : review.getCriticisms()) {
				query = "INSERT INTO CRITICISMS values (null, ?, ?, ?, null)";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setInt(1, reviewer.getReviewerId());
					preparedStmt.setInt(2, submission.getSubmissionId());
					preparedStmt.setString(3, c.getCriticism());

					preparedStmt.execute();

					ResultSet rs = preparedStmt.executeQuery("select last_insert_id() as last_id from CRITICISMS");
					while(rs.next())
						c.setCriticismId((Integer.valueOf(rs.getString("last_id"))));
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

			query = "SELECT COUNT(*) AS REVIEWS FROM REVIEWS WHERE submissionID = ?";
			int numReviews = 0;
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, submission.getSubmissionId());
				ResultSet rs = preparedStmt.executeQuery();
				while(rs.next())
					numReviews = rs.getInt("REVIEWS");
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
			
			if(numReviews == 3) {
				query = "UPDATE SUBMISSIONS SET status = ? WHERE submissionID = ?";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, SubmissionStatus.REVIEWSRECEIVED.asString());
					preparedStmt.setInt(2, submission.getSubmissionId());

					preparedStmt.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void setVerdict(Reviewer reviewer, Review r) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			Submission submission = r.getSubmission();

			String query = "UPDATE REVIEWS SET verdict = ? WHERE reviewerID = ? and submissionID = ?";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setString(1, r.getVerdict().toString());
				preparedStmt.setInt(2, reviewer.getReviewerId());
				preparedStmt.setInt(3, submission.getSubmissionId());

				preparedStmt.execute();
			}catch (SQLException ex) {
				ex.printStackTrace();
			}


			int numVerdicts = 0;
			SubmissionStatus status = null;

			query = "SELECT REVIEWS.SUBMISSIONID, COUNT(*) AS REVIEWS, STATUS FROM REVIEWS, "
					+ "SUBMISSIONS WHERE REVIEWS.submissionID = ? AND SUBMISSIONS.submissionID = ? "
					+ "AND verdict IS NOT NULL";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, submission.getSubmissionId());
				preparedStmt.setInt(2, submission.getSubmissionId());
				ResultSet rs = preparedStmt.executeQuery();
				while(rs.next()) {
					numVerdicts = rs.getInt("REVIEWS");
					status = SubmissionStatus.valueOf(rs.getString("Status"));
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			if(numVerdicts == 3 && status.equals(SubmissionStatus.REVIEWSRECEIVED)){
				query = "UPDATE SUBMISSIONS SET status = ? WHERE submissionID = ?";
				try(PreparedStatement ps = con.prepareStatement(query)){
					ps.setString(1, SubmissionStatus.INITIALVERDICT.asString());
					ps.setInt(2, submission.getSubmissionId());
					ps.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}

			}else if(numVerdicts == 3 && status.equals(SubmissionStatus.RESPONSESRECEIVED)) {
				query = "UPDATE SUBMISSIONS SET status = ? WHERE submissionID = ?";
				try(PreparedStatement ps = con.prepareStatement(query)){
					ps.setString(1, SubmissionStatus.FINALVERDICT.asString());
					ps.setInt(2, submission.getSubmissionId());
					ps.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static void addResponse(Review r) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			Submission submission = r.getSubmission();

			boolean allCriticismsAnswered = false;
			String query = "SELECT COUNT(*) AS ANSWERSLEFT FROM CRITICISMS WHERE submissionID = ? AND answer = null";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, submission.getSubmissionId());
				ResultSet rs = preparedStmt.executeQuery();
				while(rs.next()) {
					int numAnswersLeft = rs.getInt("ANSWERSLEFT");
					if(numAnswersLeft == 0)
						allCriticismsAnswered = true;
				}
			}catch (SQLException ex) {
				ex.printStackTrace();
			}

			if(allCriticismsAnswered) {
				query = "UPDATE SUBMISSIONS SET status = ? WHERE submissionID = ?";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, SubmissionStatus.RESPONSESRECEIVED.asString());
					preparedStmt.setInt(2, submission.getSubmissionId());

					preparedStmt.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

			ArrayList<Criticism> criticisms = r.getCriticisms();
			for(Criticism c : criticisms) {
				query = "UPDATE CRITICISMS SET ANSWER = ? WHERE criticismID = ?";
				try(PreparedStatement preparedStmt = con.prepareStatement(query)){
					preparedStmt.setString(1, c.getAnswer());
					preparedStmt.setInt(2, c.getCriticismId());

					preparedStmt.execute();
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void addRevisedSubmission(PDF pdf) {
		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();

			Article a = pdf.getArticle();
			String query = "INSERT INTO PDF values (null, ?, ?, ?)";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setInt(1, a.getArticleId());
				preparedStmt.setString(2, pdf.getPdfLink());
				preparedStmt.setDate(3, pdf.getDate());
	
				preparedStmt.execute();
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void acceptArticle(Submission s) {
		try (Connection con = DriverManager.getConnection(CONNECTION)) {
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			
			
			
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean academicExists(String email) {
		try (Connection con = DriverManager.getConnection(CONNECTION)) {
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			String query = "SELECT 1 FROM ACADEMICS WHERE emailAddress = ?";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setString(1, email.trim());
				ResultSet res = preparedStmt.executeQuery();
				if (res.next())
					return true;
				else
					return false;
			}catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean validateCredentials(String email, String password) {
		try (Connection con = DriverManager.getConnection(CONNECTION)) {
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();
			String query = "SELECT academicID, hash, salt FROM ACADEMICS WHERE emailAddress = ?";
			try(PreparedStatement preparedStmt = con.prepareStatement(query)){
				preparedStmt.setString(1, email.trim());
				ResultSet res = preparedStmt.executeQuery();

				int academicID = -1;
				String dbHash = null;
				String dbSalt = null;
				if (res.next()) {
					academicID = res.getInt(1);
					dbHash = res.getString(2);
					dbSalt = res.getString(3);
				}
				System.out.println(academicID + ", " + dbHash + ", " + dbSalt);

				if (academicID != -1) {
					//Generate hash based on fetched salt and entered password
					Hash newHash = new Hash(password, dbSalt);
					password = ""; //Delete password by setting it to an empty string
					return newHash.getHash().equals(dbHash);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static ArrayList<Journal> getAllJournals() {
	    ArrayList<Journal> results = new ArrayList<Journal>();
	    try (Connection con = DriverManager.getConnection(CONNECTION)) {
	        Statement statement = con.createStatement();
	        statement.execute("USE "+DATABASE+";");
	        String query = "SELECT * FROM JOURNALS";
	        ResultSet res = statement.executeQuery(query);
	        while (res.next()) {
                int resISSN = res.getInt(1);
                String resName = res.getString(2);
                Date resDate = res.getDate(3);
                results.add(new Journal(resISSN, resName, resDate));
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    for (Journal jour : results) {
	        System.out.println(jour);
	    }
	    return results;
	}
	
    public static ArrayList<Journal> getJournals(int issn, String name, Date dateOfPublication) {
        ArrayList<Journal> results = new ArrayList<Journal>();
        try (Connection con = DriverManager.getConnection(CONNECTION)) {
            Statement statement = con.createStatement();
            String query = "SELECT ISSN, name, dateOfPublication FROM JOURNALS WHERE";
            if (issn != -1) {
                query = query + "issn = " + issn + " AND ";
            }
            if (name != "") {
                query = query + "name = " + name + " AND ";
            }
            if (dateOfPublication != null) {
                query = query + "dateOfPublication = " + dateOfPublication;
            }
            ResultSet res = statement.executeQuery(query);
            if (res.next()) {
                int resISSN = res.getInt(1);
                String resName = res.getString(2);
                results.add(new Journal(resISSN, resName, null));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

	public static void main(String[] args) {
		System.out.println("\nDrivers loaded as properties:");
		System.out.println(System.getProperty("jdbc.drivers"));
		System.out.println("\nDrivers loaded by DriverManager:");
		Enumeration<Driver> list = DriverManager.getDrivers();
		while (list.hasMoreElements())
			System.out.println(list.nextElement());
		System.out.println();

		try (Connection con = DriverManager.getConnection(CONNECTION)){
			Statement statement = con.createStatement();
			statement.execute("USE "+DATABASE+";");
			statement.close();

			String query = "SHOW TABLES";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			ResultSet res = preparedStmt.executeQuery();
			while(res.next()) {
				System.out.println(res.getString(1));
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
}
