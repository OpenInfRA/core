package de.btu.openinfra.backend.db.daos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.LocalizedCharacterString;
import de.btu.openinfra.backend.db.jpa.model.PtFreeText;
import de.btu.openinfra.backend.db.jpa.model.PtLocale;
import de.btu.openinfra.backend.db.pojos.LocalizedString;
import de.btu.openinfra.backend.db.pojos.PtFreeTextPojo;

/**
 * This class represents the PtFreeText and is used to access the underlying
 * layer generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class PtFreeTextDao	extends OpenInfraDao<PtFreeTextPojo, PtFreeText> {

	/**
	 * Represents a value which cannot be assigned to any language.
	 */
	public static String NON_LINGUISTIC_CONTENT = "xx";

	/**
	 * This is the required constructor which calls the super constructor and in
	 * turn creates the corresponding entity manager.
	 *
	 * @param currentProjectId the current project id (this should be null when
	 *                         the system schema is selected)
	 * @param schema           the required schema
	 */
	protected PtFreeTextDao(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema, PtFreeText.class);
	}

	@Override
	public PtFreeTextPojo mapToPojo(Locale locale, PtFreeText ptf) {
		// 1. Create a list of the specified object type
		List<LocalizedString> lsList = new LinkedList<LocalizedString>();

		// 2. Extract the information from the given PtFreeText object
		if(ptf != null) {
			for(LocalizedCharacterString lcs :
				ptf.getLocalizedCharacterStrings()) {
				LocalizedString ls = new LocalizedString();
				// 2.a Get the free text
				ls.setCharacterString(lcs.getFreeText());
				// 2.b Create a PtLocalePojo, map the text representation
				//     and insert it to the list of objects
				//     Use the static method in order to save response time!
				ls.setLocale(PtLocaleDao.mapToPojoStatically(
						locale,
						lcs.getPtLocale()));
				lsList.add(ls);
			} // end for
		} // end if

		// 3. Finaly, create a new instance of TypeString and add the list
		// of LocalizedStrings
		return new PtFreeTextPojo(lsList, ptf.getId(), ptf.getXmin());
	}

	/**
	 * This is a special method which maps a POJO object into a JPA object
	 * statically. When the locale is set, this method returns only the
	 * specified language and non linguistic contents. When the locale is not
	 * set (equal to null), this method returns all existing languages including
	 * non linguistic contents.
	 *
	 * @param locale the language information
	 * @param ptf    the PtFreeText object
	 * @return       a list of LocalizedStrings
	 */
	private static List<LocalizedString> mapToLocalizedStringStatically(
			Locale locale,
			PtFreeText ptf) {
		// 1. Create a list of LocalizedStrings
		List<LocalizedString> lsList = new LinkedList<LocalizedString>();

		// 2. Extract the information from the given PtFreeText object
		if(ptf != null) {
			// 2.a When the locale is not null select only this
			if(locale != null) {
				for(LocalizedCharacterString lcs :
					ptf.getLocalizedCharacterStrings()) {
					// 2.b select only the requested language
					// Caution, if the language code is of type 'xx' return it
					// in any case.
					String lc = lcs.getPtLocale().getLanguageCode()
							.getLanguageCode();
					String cc = null;
					if(lcs.getPtLocale().getCountryCode() != null) {
						cc = lcs.getPtLocale().getCountryCode()
								.getCountryCode();
					} // end if
					if(lc.equals(NON_LINGUISTIC_CONTENT) ||
							(cc.equals(locale.getCountry()) &&
									lc.equals(locale.getLanguage()))) {
						lsList.add(mapLocalizedString(lcs));
						break;
					} // end if
				} // end for
			} else {
				for(LocalizedCharacterString lcs :
					ptf.getLocalizedCharacterStrings()) {
					lsList.add(mapLocalizedString(lcs));
				} // end for
			} // end if else
		} // end if

		// 3. Finaly, return the resut list
		return lsList;
	}

	/**
	 * This method maps a PtFreeText object into a LocalizedString object.
	 *
	 * @param lcs the LocalizedCharacterString object
	 * @return    the LocalizedString object
	 */
	private static LocalizedString mapLocalizedString(
			LocalizedCharacterString lcs) {
		LocalizedString ls = new LocalizedString();
		// 2.a Get the free text
		ls.setCharacterString(lcs.getFreeText());
		// 2.b Create a PtLocalePojo, map the text representation
		//     and insert it to the list of objects
		//     Use the static method in order to save response time!
		ls.setLocale(PtLocaleDao.mapToPojoStatically(
				null,
				lcs.getPtLocale()));
		return ls;
	}

	/**
	 * Example description of the handling of free text by the database:
	 * one project ->
	 * one description (in many languages) ->
	 * has PtFreeText (a group of languages) ->
	 * has LocalizedCharacterString (free text + language)
	 *
	 */
	@Override
	public MappingResult<PtFreeText> mapToModel(
			PtFreeTextPojo pojo,
			PtFreeText ptf) {

        // return null if the pojo or the model object is null
        if (pojo != null && ptf != null) {

    		// 1. Define the List of LocalizedCharacterStrings and
    		//    a. take the existing list when the PtFreeText already has one
    		//    b. create a new one when the PtFreeText doesn't have one yet
    		List<LocalizedCharacterString> lcsList =
    				ptf.getLocalizedCharacterStrings();
    		if(lcsList == null) {
    			lcsList = new LinkedList<LocalizedCharacterString>();
    		} // end if

    		// 3. Now iterate through all LocalizedStrings and look if this
    		// locale already exists in the LocalizedCharacterStrings (from
    		// PtFreeText)
    		for(LocalizedString ls : pojo.getLocalizedStrings()) {
    			boolean exists = false;
    			// Use an iterator in order to remove items from list nicely and
    			// safely
    			Iterator<LocalizedCharacterString> it = lcsList.iterator();
    			while(it.hasNext()) {
    				LocalizedCharacterString lcs = it.next();
    				// If the locale exists replace the free text string in any
    				// case
    				if(ls.getLocale().getUuid().equals(lcs.getPtLocale()
    						.getId())) {
    					exists = true;
    					// It was agreed that the LocalizedCharacterString is
    					// deleted from database when the free text is empty.
    					if("".equals(ls.getCharacterString())) {
    						it.remove();
    					} else {
    					    // write string and escape it to prevent XSS
    						lcs.setFreeText(escapeString(
    								ls.getCharacterString()));
    					} // end if else
    					break;
    				} // end if
    			} // end while

    			// If the locale doesn't exists create a new
    			// LocalizedCharacterString
    			if(!exists && !"".equals(ls.getCharacterString())) {
    				LocalizedCharacterString lcs =
    						new LocalizedCharacterString();
    				lcs.setPtLocale(em.find(
    						PtLocale.class,
    						ls.getLocale().getUuid()));
    				lcs.setFreeText(escapeString(ls.getCharacterString()));
    				lcs.setPtFreeText(ptf);
    				lcsList.add(lcs);
    			} // end if
    		}

    		// 4. Finally, add the changed List of LocalizedCharacterStrings to
    		//    the PtFreeTextObject
    		ptf.setLocalizedCharacterStrings(lcsList);

    		// return the model as mapping result
    		return new MappingResult<PtFreeText>(ptf.getId(), ptf);
        } else {
            return null;
        }
	}

	/**
	 * This method provides different escape steps for strings. This method is
	 * highly relevant for security issues. It is absolutely essential to test
	 * all kind of XSS possibilities against this method.
	 *
	 * @param input the string that should be escaped
	 * @return      the escaped string
	 */
	private String escapeString(String input) {
	    return StringEscapeUtils.escapeHtml4(input);
	}

	/**
	 * This method returns the text of the PtFreeTextDao for the given
	 * PtFreeText pojo.
	 *
	 * @param text the PtFreeText pojo
	 * @return     the PtFreeText or null if it not exists
	 */
	protected PtFreeText getPtFreeTextModel(PtFreeTextPojo text) {
	    // check if the given PtFreeTextDao exists and its text is not null
	    if(text != null && text.getLocalizedStrings() != null) {
	        // return the jpa model of the PtFreeText
	        return this.mapToModel(text, this.createModelObject(
                            text.getUuid())).getModelObject();
        }
        return null;
    }

	public static PtFreeTextPojo mapToPojoStatically(
			Locale locale,
			PtFreeText ptf) {
		UUID id = null;
		int trid = -1;
		if(ptf != null) {
			id = ptf.getId();
			trid = ptf.getXmin();
		} // end if
		return new PtFreeTextPojo(
				mapToLocalizedStringStatically(locale, ptf),
				id,
				trid);
	}
}
