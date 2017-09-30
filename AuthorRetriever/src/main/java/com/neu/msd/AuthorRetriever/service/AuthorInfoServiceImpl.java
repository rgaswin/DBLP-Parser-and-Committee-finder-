package com.neu.msd.AuthorRetriever.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.neu.msd.AuthorRetriever.dao.SearchAuthorDao;
import com.neu.msd.AuthorRetriever.dao.SearchAuthorDaoImpl;
import com.neu.msd.AuthorRetriever.dao.SearchConferenceDao;
import com.neu.msd.AuthorRetriever.dao.SearchConferenceDaoImpl;
import com.neu.msd.AuthorRetriever.dao.SearchPaperDao;
import com.neu.msd.AuthorRetriever.dao.SearchPaperDaoImpl;
import com.neu.msd.AuthorRetriever.model.Author;
import com.neu.msd.AuthorRetriever.model.AuthorPaper;
import com.neu.msd.AuthorRetriever.model.Conference;

public class AuthorInfoServiceImpl implements AuthorInfoService {

	private SearchPaperDao searchPaperDao = new SearchPaperDaoImpl();
	private SearchAuthorDao searchAuthorDao = new SearchAuthorDaoImpl();
	private SearchConferenceDao searchConfDao = new SearchConferenceDaoImpl();
	
	/**
	 * Retrieves an author's profile for the given authorId
	 * @param authorId Id of an author
	 * @return author's profile information 
	 */
	
	@Override
	public Author getAuthorProfile(int authorId) {
		List<Author> authors = new ArrayList<Author>();
		String queryString = "SELECT * FROM author WHERE author.id = "+authorId;
		try {
			authors = searchAuthorDao.searchAuthorsByCriteria(queryString);
		} catch (SQLException e) {
			return null;
		}
		
		return authors.isEmpty()? null :authors.get(0);
	}

	/**
	 * Retrieves a list of published papers by author for the given authorId
	 * @param authorId Id of an author
	 * @return list of published papers
	 */
	
	@Override
	public List<AuthorPaper> getAuthorPapers(int authorId) throws SQLException {
		
		List<AuthorPaper> authorPapers = new ArrayList<AuthorPaper>();
		
		String queryString = "SELECT * FROM paper WHERE paper.paper_id IN (SELECT paper_Id FROM author_paper_mapping WHERE author_paper_mapping.Author_id = "+authorId+")";
		System.out.println(queryString);
		authorPapers = searchPaperDao.retrievePapers(queryString);
		
		return authorPapers;
	}

	/**
	 * Retrieves a list of conferences attended by author for the given authorId
	 * @param authorId Id of an author
	 * @return list of conference papers
	 */
	
	@Override
	public List<Conference> getAuthorConferenceServed(int authorId) throws SQLException {
		
		List<Conference> authorConfServed = new ArrayList<Conference>();
		
		String queryString = "SELECT * FROM conference WHERE ID IN (SELECT confid FROM conference_editor_mapping where editorid IN (SELECT editorId FROM editor where Author_Id = "+authorId +"))";

		
		authorConfServed = searchConfDao.retrieveConference(queryString);
		return authorConfServed;
	}

}
