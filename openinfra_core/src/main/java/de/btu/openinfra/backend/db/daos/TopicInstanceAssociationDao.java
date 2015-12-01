package de.btu.openinfra.backend.db.daos;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.TopicCharacteristic;
import de.btu.openinfra.backend.db.jpa.model.TopicInstance;
import de.btu.openinfra.backend.db.jpa.model.TopicInstanceXTopicInstance;
import de.btu.openinfra.backend.db.pojos.project.TopicInstanceAssociationToPojo;

/**
 * This class represents the TopicInstanceAssociation and is used to access the
 * underlying layer generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class TopicInstanceAssociationDao extends OpenInfraValueValueDao<
	TopicInstanceAssociationToPojo,
	TopicInstanceXTopicInstance,
	TopicInstance, TopicInstance> {

	/**
	 * This is the required constructor which calls the super constructor and
	 * in turn creates the corresponding entity manager.
	 *
	 * @param currentProjectId the current project id (this should be null when
	 *                         the system schema is selected)
	 * @param schema           the required schema
	 */
	public TopicInstanceAssociationDao(
			UUID currentProjectId,
			OpenInfraSchemas schema) {
		super(
				currentProjectId,
				schema,
				TopicInstanceXTopicInstance.class,
				TopicInstance.class, TopicInstance.class);
	}

	@Override
	public TopicInstanceAssociationToPojo mapToPojo(
			Locale locale,
			TopicInstanceXTopicInstance txt) {
	    return mapToPojoStatically(locale, txt,
                new MetaDataDao(currentProjectId, schema));
	}

	/**
     * This method implements the method mapToPojo in a static way.
     *
     * @param locale the requested language as Java.util locale
     * @param txt    the model object
     * @param mdDao  The meta data DAO must not be null.
     * @return       the POJO object when the model object is not null else null
     */
    public static TopicInstanceAssociationToPojo mapToPojoStatically(
            Locale locale,
            TopicInstanceXTopicInstance txt,
            MetaDataDao mdDao) {
        if (txt != null) {
            TopicInstanceAssociationToPojo pojo =
                    new TopicInstanceAssociationToPojo(txt, mdDao);
            pojo.setAssociationInstance(
                    TopicInstanceDao.mapToPojoStatically(
                    		locale,
                    		txt.getTopicInstance1Bean(),
                    		mdDao));
            pojo.setRelationshipType(
                    RelationshipTypeDao.mapToPojoStatically(
                            locale,
                            txt.getRelationshipType(),
                            mdDao));
            pojo.setAssociatedInstance(
                    TopicInstanceDao.mapToPojoStatically(
                            locale,
                            txt.getTopicInstance2Bean(),
                            mdDao));
            return pojo;
        } else {
            return null;
        }
    }

    public List<TopicInstanceAssociationToPojo> readAssociationToByTopchar(
    		Locale locale, UUID topicInstance, UUID topChar,
    		int offset, int size) {
    	return readAssociation(locale, topicInstance, topChar,
    			"TopicInstanceXTopicInstance"
    			+ ".findAssociationToByTopicInstanceAndTopicCharacteristic",
    			offset, size);
    }

    public List<TopicInstanceAssociationToPojo> readAssociationFromByTopchar(
    		Locale locale, UUID topicInstance, UUID topChar,
    		int offset, int size) {
    	return readAssociation(locale, topicInstance, topChar,
    			"TopicInstanceXTopicInstance"
    			+ ".findAssociationFromByTopicInstanceAndTopicCharacteristic",
    			offset, size);
    }

    public List<TopicInstanceAssociationToPojo> readAssociation(
    		Locale locale, UUID topicInstance, UUID topChar, String queryName,
    		int offset, int size) {
    	List<TopicInstanceXTopicInstance> tixtiList = em.createNamedQuery(
    			queryName, TopicInstanceXTopicInstance.class)
    			.setParameter("topicInstance",
    					em.find(TopicInstance.class, topicInstance))
    			.setParameter("topicCharacteristic",
    					em.find(TopicCharacteristic.class, topChar))
    			.setFirstResult(offset).setMaxResults(size).getResultList();
    	List<TopicInstanceAssociationToPojo> pojoList =
    			new LinkedList<TopicInstanceAssociationToPojo>();
    	for(TopicInstanceXTopicInstance tixti : tixtiList) {
    		pojoList.add(mapToPojo(locale, tixti));
    	}
    	return pojoList;
    }

	@Override
	public MappingResult<TopicInstanceXTopicInstance> mapToModel(
			TopicInstanceAssociationToPojo pojo,
			TopicInstanceXTopicInstance txt) {

        // TODO set the model values

        // return the model as mapping result
        return new MappingResult<TopicInstanceXTopicInstance>(
                txt.getId(), txt);
	}

}
