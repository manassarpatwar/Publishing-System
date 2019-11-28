package com.publishingsystem.gui;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.publishingsystem.mainclasses.Academic;
import com.publishingsystem.mainclasses.Article;
import com.publishingsystem.mainclasses.Author;
import com.publishingsystem.mainclasses.Database;
import com.publishingsystem.mainclasses.Hash;
import com.publishingsystem.mainclasses.Journal;
import com.publishingsystem.mainclasses.PDF;
import com.publishingsystem.mainclasses.PDFConverter;
import com.publishingsystem.mainclasses.RetrieveDatabase;
import com.publishingsystem.mainclasses.Role;

import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ListSelectionModel;

public class SubmitArticle2 {

	private JFrame frmSubmitAnArticle;
	private JTextField txtfldTitle;
	private String selectedJournalName;
	private ArrayList<Author> coAuthors;
	private ArrayList<Integer> numReviewsOfCoAuthors;
	private String pdfPath;
	private JScrollPane scrPaneAuthors;
	private DefaultListModel<String> coAuthorsModel;
    private JList<String> listOfAuthors;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubmitArticle2 window = new SubmitArticle2(new Author(1, "Dr", "kb", "kb", "Sheffield", "kb@gm.com", new Hash("9d5be6810a8de8673cf2a5b83f2030393028b71127dd034beb9bd03f3a946302")), 0);
					//SubmitArticle window = new SubmitArticle(null);
					window.frmSubmitAnArticle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SubmitArticle2(Author a, int numReviewsOfMainAuthor) {
        coAuthors = new ArrayList<Author>();
        numReviewsOfCoAuthors = new ArrayList<Integer>();
        Academic[] newRoles = new Academic[3];
		newRoles[1] = a;
		initialize(a, newRoles, numReviewsOfMainAuthor);
	}
	
	public SubmitArticle2(Academic[] roles, int numReviewsOfMainAuthor) {
		coAuthors = new ArrayList<Author>();
		numReviewsOfCoAuthors = new ArrayList<Integer>();
		Author a = null;
		if(roles[1] == null) {
			Academic aca = null;
			for(Academic role : roles) {
				if(roles != null) {
					aca = role;
					break;
				}
			}
			a = new Author(-1, aca.getTitle(), 
								aca.getForename(), 
								aca.getSurname(), 
								aca.getEmailId(), 
								aca.getUniversity(), null);
			a.setAcademicId(aca.getAcademicId());
			roles[1] = a;
		}else {
			a = (Author)roles[1];
		}
		initialize(a, roles, numReviewsOfMainAuthor);
	}

	public void addCoAuthor(Author coAuthor, int numReview) {
		this.coAuthors.add(coAuthor);
		this.numReviewsOfCoAuthors.add(numReview);
		this.coAuthorsModel.clear();
	    for (Author author : coAuthors){
        	coAuthorsModel.addElement(author.getForename()+" "+author.getSurname()+" ("+author.getEmailId()+")");
        }
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Author mainAuthor, Academic[] roles, int numReviewsOfMainAuthor) {
		frmSubmitAnArticle = new JFrame();
		frmSubmitAnArticle.setTitle("Submit an Article");
		frmSubmitAnArticle.setBounds(100, 100, 700, 552);
		frmSubmitAnArticle.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSubmitAnArticle.setVisible(true);
		selectedJournalName = null;
		JScrollPane scrollPane = new JScrollPane();

		JLabel lblSubmitANewArticle = new JLabel("Submit a New Article");
		lblSubmitANewArticle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubmitANewArticle.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblChooseAJournal = new JLabel("Choose a Journal to which Publish to:");
		lblChooseAJournal.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));

		txtfldTitle = new JTextField();
		txtfldTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtfldTitle.setColumns(10);
		
		JScrollPane scrPaneAbstract = new JScrollPane();
		scrPaneAbstract.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel lblAbstract = new JLabel("Abstract:");
		lblAbstract.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JEditorPane editPaneAbstract = new JEditorPane();
		scrPaneAbstract.setViewportView(editPaneAbstract);

        // Authors of article
        JLabel lblAuthors = new JLabel("Authors:");
        lblAuthors.setFont(new Font("Tahoma", Font.PLAIN, 15));

        JLabel lblNewLabel_2 = new JLabel("Please Register Your Article's Every Co-Author");

        JScrollPane scrPaneAuthors = new JScrollPane();
        scrPaneAuthors.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        coAuthorsModel = new DefaultListModel<>();
        listOfAuthors = new JList<>(coAuthorsModel);
        listOfAuthors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrPaneAuthors.setViewportView(listOfAuthors);

        String[] listOfAuthors = new String[coAuthors.size()];
        for (int i=0; i<coAuthors.size(); i++) {
            listOfAuthors[i] = coAuthors.get(i).getForename() + " " + coAuthors.get(i).getSurname();
        }

        SubmitArticle2 submitArticleGUI = this;
        JButton btnRegisterANew = new JButton("Register a New Co-Author");
        btnRegisterANew.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new RegistrationWindow(Role.COAUTHOR, submitArticleGUI);
            }
        });
        btnRegisterANew.setFont(new Font("Tahoma", Font.PLAIN, 15));

        // PDF
		JButton btnUploadPdf = new JButton("Upload PDF");
		JLabel lblPdfIsNot = new JLabel("PDF is not yet uploaded");
		btnUploadPdf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				FileDialog fd = new FileDialog(new JFrame());
				fd.setVisible(true);
				File[] f = fd.getFiles();

				if(f.length > 0){
					pdfPath = fd.getFiles()[0].getAbsolutePath();
					System.out.println(pdfPath);
					lblPdfIsNot.setText("PDF is successfully uploaded");
				}		
				else {
					lblPdfIsNot.setText("Try Again! PDF did not upload!");
				}
			}
		});
		btnUploadPdf.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JList listOfJournals = new JList();
		listOfJournals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listOfJournals);
		listOfJournals.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				selectedJournalName = (String)listOfJournals.getSelectedValue();
			}

		});
		System.out.println(selectedJournalName);


		ArrayList<Journal> allJournals = RetrieveDatabase.getJournals();
        String[] listContents = new String[allJournals.size()];
        for (int i=0; i<allJournals.size(); i++) {
            listContents[i] = allJournals.get(i).getJournalName();
        }

        // Submit Button
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				JOptionPane.showMessageDialog(null, "To access your Author/Reviewer roles please Log Out and Login In again to the system. Thank you!");
				String title = txtfldTitle.getText();
				String summary = editPaneAbstract.getText();
				System.out.println(summary);
				Journal journal = null;
				for (Journal item: allJournals) {
					if (item.getJournalName().equals(selectedJournalName)) {
						journal = item;
					}
				}
				if(title.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please give a title", "Error in submission", 0);
				}
				if(summary.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please give a summary", "Error in submission", 0);
				}
				if(journal == null) {
					JOptionPane.showMessageDialog(null, "Please select a journal", "Error in submission", 0);
				}
				if(pdfPath == null) {
					JOptionPane.showMessageDialog(null, "Please upload a pdf of your article", "Error in submission", 0);
				}
				
				if(title != null && summary != null && journal != null && pdfPath != null){
					byte[] pdf = PDFConverter.getByteArrayFromFile(pdfPath);
					Article article = new Article(-1, title, summary, journal);
					mainAuthor.registerCoAuthors(article, coAuthors, numReviewsOfCoAuthors);
					Calendar calendar = Calendar.getInstance();
					mainAuthor.submit(article, new PDF(-1, new java.sql.Date(calendar.getTime().getTime()), article.getSubmission()), numReviewsOfMainAuthor);
	
					coAuthors.add(mainAuthor);
					
					//ADDING TO THE DATABASE;
					Database.registerAuthors(coAuthors);
					Database.addSubmission(article, pdf);
			
			
					new AuthorMainWindow(roles);
					//This is for just adding co-authors
					frmSubmitAnArticle.dispose();
				}
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 15));

		btnRegisterANew.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(frmSubmitAnArticle.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(80)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnUploadPdf)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPdfIsNot))
						.addComponent(lblAuthors)
						.addComponent(lblAbstract)
						.addComponent(lblTitle)
						.addComponent(lblChooseAJournal)
						.addComponent(lblSubmitANewArticle, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
						.addComponent(txtfldTitle, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrPaneAuthors, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
							.addGap(50)
							.addComponent(btnRegisterANew)
							.addGap(35))
						.addComponent(scrPaneAbstract, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
					.addGap(80))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(290)
					.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 104, Short.MAX_VALUE)
					.addGap(290))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(lblSubmitANewArticle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblChooseAJournal)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTitle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtfldTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAbstract)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrPaneAbstract, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAuthors)
					.addGap(2)
					.addComponent(lblNewLabel_2)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(scrPaneAuthors, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnUploadPdf)
								.addComponent(lblPdfIsNot))
							.addGap(10))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
							.addComponent(btnRegisterANew)
							.addGap(10)))
					.addComponent(btnSubmit)
					.addGap(13))
		);

		listOfJournals.setModel(new AbstractListModel() {
			//String[] values = new String[] {"First Journal", "Second journal", "Third Journal", "", "First Journal", "Second journal", "Third Journal", "First Journal", "Second journal", "Third Journal", "First Journal", "Second journal", "Third Journal", "First Journal", "Second journal", "Third Journal", "First Journal", "Second journal", "Third Journal", "First Journal", "Second journal", "Third Journal"};
            String[] values = listContents;

		    public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		frmSubmitAnArticle.getContentPane().setLayout(groupLayout);
	}
}