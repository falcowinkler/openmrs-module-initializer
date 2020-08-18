package org.openmrs.module.initializer.api.conceptsources;

import org.openmrs.ConceptSource;
import org.openmrs.module.initializer.api.loaders.BaseCsvLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConceptSourcesLoader extends BaseCsvLoader<ConceptSource, ConceptSourceCsvParser> {
	
	@Autowired
	public void setParser(ConceptSourceCsvParser parser) {
		this.parser = parser;
	}
}
