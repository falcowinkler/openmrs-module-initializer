package org.openmrs.module.initializer.api.conceptsources;

import org.bahmni.module.bahmni.ie.apps.model.FormTranslation;
import org.openmrs.ConceptSource;
import org.openmrs.api.UserService;
import org.openmrs.module.initializer.api.BaseLineProcessor;
import org.openmrs.module.initializer.api.CsvLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.stereotype.Component;

@Component
public class ConceptSourceLineProcessor extends BaseLineProcessor<ConceptSource> {
	
	protected static String HEADER_HL7_CODE = "hl7 code";
	
	protected static String HEADER_DESCRIPTION = "description";

	protected static String HEADER_CREATED_BY = "created by";

	@Autowired
	private UserService userService;

	@Override
	public ConceptSource fill(ConceptSource instance, CsvLine line) throws IllegalArgumentException {
		instance.setDescription(line.get(HEADER_DESCRIPTION));
		instance.setHl7Code(line.get(HEADER_HL7_CODE));
		instance.setName(line.get(HEADER_NAME));
		instance.setCreator(userService.getUserByUsername(line.get(HEADER_CREATED_BY)));;
		return instance;
	}
}
