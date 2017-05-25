package org.openmrs.module.initializer.api.c;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.openmrs.module.initializer.api.BaseLineProcessor;
import org.springframework.util.CollectionUtils;

public class NestedConceptLineProcessor extends BaseLineProcessor<Concept, ConceptService> {
	
	protected static String HEADER_ANSWERS = "answers";
	
	protected static String HEADER_MEMBERS = "members";
	
	public NestedConceptLineProcessor(String[] headerLine, ConceptService cs) {
		super(headerLine, cs);
	}
	
	/**
	 * @param mapping The concept mapping, eg. "cambodia:123"
	 * @param cs
	 * @return The {@link Concept} instance if found, null otherwise.
	 */
	public static Concept getConceptByMapping(String mapping, ConceptService cs) {
		Concept concept = null;
		String[] parts = mapping.split(":");
		if (parts.length == 2) {
			concept = cs.getConceptByMapping(parts[1].trim(), parts[0].trim());
		}
		return concept;
	}
	
	/**
	 * Fetches a concept trying various routes for its "id".
	 * 
	 * @param id The concept mapping ("cambodia:123"), concept name or concept UUID.
	 * @param cs
	 * @return The {@link Concept} instance if found, null otherwise.
	 */
	public static Concept fetchConcept(String id, ConceptService cs) {
		Concept concept = null;
		if (concept == null) {
			concept = cs.getConceptByName(id);
		}
		if (concept == null) {
			concept = cs.getConceptByUuid(id);
		}
		if (concept == null) {
			concept = getConceptByMapping(id, cs);
		}
		return concept;
	}
	
	/**
	 * Parses a list of concepts provided as concept mappings, UUIDs or concept names into a list of
	 * {@link Concept} instances.
	 * 
	 * @param conceptList Eg.: ["cambodia:123"; "a92bf372-2fca-11e7-93ae-92361f002671";
	 *            "CONCEPT_FULLY_SPECIFIED_NAME"]
	 * @param cs
	 * @return The list of {@link Concept} that have been found, null if any error(s).
	 */
	protected static List<Concept> parseConceptList(String conceptList, ConceptService cs) throws IllegalArgumentException {
		List<Concept> concepts = new ArrayList<Concept>();
		
		String[] parts = conceptList.split(BaseLineProcessor.LIST_SEPARATOR);
		
		for (String id : parts) {
			id = id.trim();
			Concept child = fetchConcept(id, cs);
			if (child != null) {
				concepts.add(child);
			} else {
				throw new IllegalArgumentException(
				        "The concept identified by '"
				                + id
				                + "' could not be found in database. The concept with the following nested list of concepts was not created/updated: ["
				                + conceptList + "].");
			}
		}
		
		return concepts;
	}
	
	protected Concept fill(Concept concept, String[] line) throws IllegalArgumentException {
		
		if (!CollectionUtils.isEmpty(concept.getAnswers())) {
			concept.getAnswers().clear();
		}
		String childrenStr;
		childrenStr = line[getColumn(HEADER_ANSWERS)];
		if (!StringUtils.isEmpty(childrenStr)) {
			for (Concept child : parseConceptList(childrenStr, service)) {
				concept.addAnswer(new ConceptAnswer(child));
			}
		}
		
		if (!CollectionUtils.isEmpty(concept.getConceptSets())) {
			concept.getConceptSets().clear();
			concept.setSet(false);
		}
		childrenStr = line[getColumn(HEADER_MEMBERS)];
		if (!StringUtils.isEmpty(childrenStr)) {
			for (Concept child : parseConceptList(childrenStr, service)) {
				concept.addSetMember(child);
			}
			concept.setSet(true);
		}
		
		return concept;
	}
}
